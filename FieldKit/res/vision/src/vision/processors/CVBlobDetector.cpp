/*
 *  CVBlobDetector.cpp
 *  vision2
 *
 *  Created by Marcus Wendt on 6/20/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#include "CVBlobDetector.h"

namespace Vision 
{
	// ---------------------------------------------------------------------------------
	#pragma mark -- Constructor --
	// ---------------------------------------------------------------------------------	
	CVBlobDetector::CVBlobDetector()
	{
		initStages(STAGE_MAX);
		cvInitFont(&font,CV_FONT_HERSHEY_SIMPLEX, 0.5, 0.5, 0, 1);
		useAdaptiveTresholding = false;
	}

	// ---------------------------------------------------------------------------------
	#pragma mark -- Destructor --
	// ---------------------------------------------------------------------------------	
	CVBlobDetector::~CVBlobDetector()
	{
		cvReleaseMat(&warpMatrix);	
		//free(&font);
	}

	// ---------------------------------------------------------------------------------
	#pragma mark -- Init --
	// ---------------------------------------------------------------------------------	
	Error CVBlobDetector::init()
	{
		// sliders --------------------------------------------------------------------------------------
		setSlider(SLIDER_BACKGROUND, new Slider(0, 1));
		setSlider(SLIDER_THRESHOLD, new Slider(0, 255));
		setSlider(SLIDER_DILATE, new Slider(0, 35));
		setSlider(SLIDER_ERODE, new Slider(0, 35));
		setSlider(SLIDER_CONTOUR_MIN, new Slider(0, 1));
		setSlider(SLIDER_CONTOUR_MAX, new Slider(0, 1));
		setSlider(SLIDER_CONTOUR_REDUCE, new Slider(0, 10));
		setSlider(SLIDER_TRACK_RANGE, new Slider(0, 1));

		// warp ----------------------------------------------------------------------------------------
		warpMatrix = cvCreateMat(3, 3, CV_32FC1);

		setWarp(0, 0,
				size.width, 0,
				size.width, size.height,
				0, size.height);

		// blobs ---------------------------------------------------------------------------------------
		blobNum = BLOB_MAX_COUNT;
		foundBlobs = new Blob*[blobNum];
		trackedBlobs = new Blob*[blobNum];
		for (int i=0; i<blobNum; i++) {
			foundBlobs[i] = new Blob(i);
			trackedBlobs[i] = new Blob(i);
		}
		
		// background
		doResetBackground = true;
		
		// contours -------------------------------------------------------------------------------------
		contourStorage = cvCreateMemStorage(0);
		return SUCCESS;
	};
	
	Error CVBlobDetector::update(Camera *camera)
	{
		// images --------------------------------------------------------------------------------------
		srcImage32F = cache->getTmp(IMAGE_SRC32F, roiSize, IPL_DEPTH_32F, 1);
		dstImage32F = cache->getTmp(IMAGE_DST32F, roiSize, IPL_DEPTH_32F, 1);
		srcImage8U = cache->getTmp(IMAGE_SRC8U, roiSize, IPL_DEPTH_8U, 1);
		dstImage8U = cache->getTmp(IMAGE_DST8U, roiSize, IPL_DEPTH_8U, 1);
		
		// input ---------------------------------------------------------------------------------------
		// put camera data into inputImage
		IplImage* inputImage = cache->getTmp(IMAGE_INPUT, cvSize(camera->getWidth(), camera->getHeight()));
		cvSetImageData(inputImage, camera->getImage(cameraSource), camera->getWidth());
		
		// resize inputImage if necessary
		IplImage* inputImageResized = cache->getTmp(IMAGE_INPUT_RESIZED, size);
		if(camera->getWidth() != size.width || camera->getHeight() != size.height) {
			cvResize(inputImage, inputImageResized, CV_INTER_LINEAR);
		} else {
			inputImageResized = inputImage;
		}

		copyStage(STAGE_INPUT, inputImageResized);	
		
		// crop region of interest out of inputImageResized into srcImage
		setMode(MODE_8U);
		if(inputImageResized->width > roi.width && inputImageResized->height > roi.height) {
			cvGetRectSubPix(inputImageResized, srcImage, cvPoint2D32f(roi.x + roi.width / 2, roi.y + roi.height / 2));
		} else {
			//srcImage = inputImageResized;
			cvCopy(inputImageResized, srcImage);
		}
				
		// warp ----------------------------------------------------------------------------------------
		if(warpEnabled) {
			setMode(MODE_8U);
			cvWarpPerspective(srcImage, dstImage, warpMatrix, CV_WARP_FILL_OUTLIERS, cvScalarAll(0));
			copyStage(STAGE_WARP, dstImage);
			swap();
		}
		
		// background ----------------------------------------------------------------------------------
		setMode(MODE_32F);
		float bgValue = doResetBackground ? 1.0 : get(SLIDER_BACKGROUND);
		IplImage *bgImage = cache->getTmp(IMAGE_BG, roiSize, IPL_DEPTH_32F, 1);
		//IplImage *bgImage = cache->getTmp(IMAGE_BG, srcImage);

		/*
		printf("bgImage %i %i \n", bgImage->width, bgImage->height);
		printf("srcImage %i %i \n", srcImage->width, srcImage->height);
		
		if(bgImage->roi != NULL) {
			printf("bgImage roi %i %i \n", bgImage->roi->width, bgImage->roi->height);
		}
		
		if(srcImage->roi != NULL) {
			printf("srcImage roi %i %i \n", srcImage->roi->width, srcImage->roi->height);
		}
		*/
		
		if (bgValue > 0) {
			cvRunningAvg(srcImage, bgImage, bgValue);
			copyStage(STAGE_BACKGROUND, bgImage);
			doResetBackground = false;
		}
		
		// difference ---------------------------------------------------------------------------------
		setMode(MODE_32F);
		cvAbsDiff(srcImage, bgImage, dstImage);
		copyStage(STAGE_DIFFERENCE, dstImage);
		swap();

		// threshold -----------------------------------------------------------------------------------
		if(useAdaptiveTresholding) {
			setMode(MODE_8U);
			// adaptive treshold works with 8U images, we need to convert the source image first
			//cvAdaptiveThreshold(srcImage8U, dstImage8U, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY, 5, -7);
			cvAdaptiveThreshold(srcImage, dstImage, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 9, -5);
		} else {
			cvThreshold(srcImage, dstImage, get(SLIDER_THRESHOLD), 255, CV_THRESH_BINARY);			
		}
		copyStage(STAGE_THRESHOLD, dstImage);
		swap();
		
		// dilate --------------------------------------------------------------------------------------
		float dilate = get(SLIDER_DILATE);
		if(dilate > 0) {
			setMode(MODE_8U);
			cvDilate(srcImage, dstImage, NULL, dilate);
			copyStage(STAGE_DILATE, dstImage);	
			swap();
		}
		
		// erode ---------------------------------------------------------------------------------------
		float erode = get(SLIDER_ERODE);
		if(erode > 0) {
			setMode(MODE_8U);
			cvErode(srcImage, dstImage, NULL, erode);
			copyStage(STAGE_ERODE, dstImage);	
			swap();
		}
		
		// contours ------------------------------------------------------------------------------------
		setMode(MODE_8U);
		findContours();
		drawContours();
		swap();
		
		// blobs ---------------------------------------------------------------------------------------
		findBlobs();
		drawBlobs(STAGE_DETECTION, foundBlobs);
		
		trackBlobs();
		drawBlobs(STAGE_TRACKING, trackedBlobs);
		
		return SUCCESS;
	}

	
	// --------------------------------------------------------------------------------------------------	
	// CONTOURS
	// --------------------------------------------------------------------------------------------------
	#pragma mark ---- Contours ----
	void CVBlobDetector::findContours()
	{
		cvClearMemStorage(contourStorage);
		cvFindContours(srcImage, contourStorage, &contourFirst, sizeof(CvContour), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
		//cvFindContours(srcImage, contourStorage, &contourFirst, sizeof(CvContour), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
	}
	
	// draws the contours onto dstImage
	void CVBlobDetector::drawContours()
	{
		if(!getStage(STAGE_CONTOUR)->isEnabled) return;
				
		cvZero(dstImage);
		
		// no contours found
		if(contourFirst != NULL) {
			CvSeq* contour = contourFirst;
			CvSeq *approxContour;
			for( ; contour != 0; contour = contour->h_next ) {
				approxContour = cvApproxPoly(contour, sizeof(CvContour), contourStorage, CV_POLY_APPROX_DP, get(SLIDER_CONTOUR_REDUCE));
				cvDrawContours(dstImage, approxContour, cGrey, cDarkGrey, 3, 2);
			}
		}
		copyStage(STAGE_CONTOUR, dstImage);
	}

	
	// --------------------------------------------------------------------------------------------------	
	// BLOBS
	// --------------------------------------------------------------------------------------------------
	#pragma mark ---- Blobs ----
	void CVBlobDetector::findBlobs()
	{
		// reset active blobs
		for(int i=0; i<BLOB_MAX_COUNT; i++) {
			foundBlobs[i]->init();
		}

		// no contours found
		if(contourFirst == NULL) return;

		int imageArea = size.width * size.height;
		int minArea = get(SLIDER_CONTOUR_MIN) * imageArea;
		int maxArea = get(SLIDER_CONTOUR_MAX) * imageArea;
		
		CvSeq *approxContour;	
		CvMoments moments;
			
		// find new blobs
		int foundBlobCount=0;
		
		CvSeq* contour = contourFirst;
		for( ; contour != 0; contour = contour->h_next ) {
			approxContour = cvApproxPoly(contour, sizeof(CvContour), contourStorage, CV_POLY_APPROX_DP, get(SLIDER_CONTOUR_REDUCE));
			float area = fabs(cvContourArea(approxContour));
			
			if (area > minArea && area < maxArea) {
				if(foundBlobCount == BLOB_MAX_COUNT) {
					LOG("Too many contours");
					return;
				}
				// get blob properties
				cvContourMoments(approxContour, &moments);	
				
				// update blob
				Blob *b = foundBlobs[foundBlobCount++];
				b->isActive = true;
				b->position = cvPoint(moments.m10/moments.m00, moments.m01/moments.m00);
				b->position64f = cvPoint2D64f(moments.m10/moments.m00, moments.m01/moments.m00);
				b->bounds = cvBoundingRect(approxContour, 0);
				b->contour = approxContour;
			}
		}
	}
	
	void CVBlobDetector::trackBlobs()
	{
		Blob *found, *tracked, *match;
		float dist, distClosest;
		
		int imageArea = size.width * size.height;
		float distMax = get(SLIDER_TRACK_RANGE) * imageArea;
		distMax *= distMax;
		
		// loop through tracked blobs
		for (int i=0; i<BLOB_MAX_COUNT; i++) {
			tracked = trackedBlobs[i];
			//if(!tracked->isActive()) continue;
			match = NULL;
			distClosest = distMax;
			
			// try to find a match among the new found blobs within the slider range
			for (int j=0; j<BLOB_MAX_COUNT; j++) {
				found = foundBlobs[j];
				if(!found->isActive || found->isAssigned) continue;
				dist = ptDistanceSquared(found->position, tracked->position);
				if(dist < distClosest) {
					distClosest = dist;
					match = found;
				}
			}
			
			if(match == NULL) {
				tracked->isActive = false;
				
			} else {
				//printf("found %i => %i \n", tracked->id, match->id);
				tracked->isActive = true;
				tracked->set(match);
				match->isAssigned = true;
			}
		}
		
		
	}
	
	void CVBlobDetector::drawBlobs(int stage, Blob* blobs[])
	{
		if(!getStage(stage)->isEnabled) return;
		
		cvZero(dstImage);
		
		char* label = (char *) malloc(10);
		
		for(int i=0; i<BLOB_MAX_COUNT; i++) {
			Blob *b = blobs[i];
			
			if(b->isActive) {
				//CvScalar c = cvScalarAll(255 - i * (255/ blobNum) );
				CvScalar c = cvScalarAll(i * (255/ blobNum) );
				CvRect br = b->bounds;
				cvRectangle(dstImage, cvPoint(br.x, br.y), cvPoint(br.x + br.width, br.y + br.height), cWhite);
				//cvCircle(dstImage, b->position, 2, cWhite, 2, CV_AA );
				
				sprintf(label, "%i", i);
				cvPutText(dstImage, label, b->position, &font, cWhite);		
			}
		}
		
		free(label);
		copyStage(stage, dstImage);
	}
	
	
	// --------------------------------------------------------------------------------------------------	
	// HELPERS
	// --------------------------------------------------------------------------------------------------		
	#pragma mark ---- Helpers ----
	void CVBlobDetector::swap()
	{
		IplImage *swap;

		swap = srcImage32F;
		srcImage32F = dstImage32F;
		dstImage32F = swap;
		
		swap = srcImage8U;
		srcImage8U = dstImage8U;
		dstImage8U = swap;
		
		setMode(this->mode);
	}
	
	
	void CVBlobDetector::setMode(Mode mode) 
	{
		// convert current working image to new format
		if(this->mode != mode) {
			switch(mode) {
				case MODE_8U:
					cvConvert(srcImage32F, srcImage8U);
					break;
					
				case MODE_32F:		
					cvConvert(srcImage8U, srcImage32F);
					break;
			}
		}
		
		// set image pointers depending on format
		switch(mode) {
			case MODE_8U:
				this->srcImage = srcImage8U;
				this->dstImage = dstImage8U;
				break;
				
			case MODE_32F:
				this->srcImage = srcImage32F;
				this->dstImage = dstImage32F;
				break;
		}
		
		this->mode = mode;
	}
	
	
	// --------------------------------------------------------------------------------------------------	
	// SETTERS
	// --------------------------------------------------------------------------------------------------
	#pragma mark ---- Setters ----	
	void CVBlobDetector::setWarp(float sx1, float sy1,
								 float sx2, float sy2,
								 float sx3, float sy3,
								 float sx4, float sy4)
	{
		// init source matrix
		CvMat* src = cvCreateMat(4, 2, CV_32FC1);
		src->data.fl[0]=sx1;
		src->data.fl[1]=sy1;
		src->data.fl[2]=sx2;
		src->data.fl[3]=sy2;
		src->data.fl[4]=sx3;
		src->data.fl[5]=sy3;
		src->data.fl[6]=sx4;
		src->data.fl[7]=sy4;
		
		// init destination matrix
		CvMat* dst = cvCreateMat(4, 2, CV_32FC1);
		dst->data.fl[0]=0;
		dst->data.fl[1]=0;
		dst->data.fl[2]=size.width;
		dst->data.fl[3]=0;
		dst->data.fl[4]=size.width;
		dst->data.fl[5]=size.height;
		dst->data.fl[6]=0;
		dst->data.fl[7]=size.height;

		// check if warp is necessary
		if (src->data.fl[0] == dst->data.fl[0] && 
			src->data.fl[1] == dst->data.fl[1] && 
			src->data.fl[2] == dst->data.fl[2] && 
			src->data.fl[3] == dst->data.fl[3] && 
			src->data.fl[4] == dst->data.fl[4] && 
			src->data.fl[5] == dst->data.fl[5] && 
			src->data.fl[6] == dst->data.fl[6] && 
			src->data.fl[7] == dst->data.fl[7]) 
		{
			//LOG("warp NOT enabled");
			warpEnabled = false;
			//free(src);
			//free(dst);
			
		} else {
			//LOG("warp enabled");
			warpEnabled = true;
			cvFindHomography(src, dst, warpMatrix);
		}
	}
	
	
	// --------------------------------------------------------------------------------------------------	
	// GETTERS
	// --------------------------------------------------------------------------------------------------
	#pragma mark ---- Getters ----	
	Blob** CVBlobDetector::getBlobs()
	{
		return trackedBlobs;
	}
}