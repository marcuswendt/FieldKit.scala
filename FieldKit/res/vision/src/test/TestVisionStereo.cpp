/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "FieldVision.h"
#include "PTGreyBumblebee2.h"
#include "CVBlobDetector.h"
#include "CVStereoBlobDetector.h"

using namespace field;

//--------------------------------------------------------------------------------------------
const int SCREEN_WIDTH = 1400;
const int SCREEN_HEIGHT = 480;

//--------------------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
	LOG("************************************************");
	LOG("TestVisionStereo");
	LOG("************************************************");
	LOG(" ");
	
	LOG("** init processor **");	
	CVStereoBlobDetector *proc = new CVStereoBlobDetector();
//	proc->setSize(320, 240);
	proc->setSize(640, 480);

	proc->setROI(180, 90, 320, 240);
//	proc->setROI(320, 240, 320, 240); // works
//	proc->setROI(120, 80, 400, 260); // works
	
	proc->setStageEnabled(true);
	
	LOG("** init vision **");	
	FieldVision *v = new FieldVision();
	v->setCamera(new PTGreyBumblebee2());
	v->setProcessor(proc);
	v->init();
	v->getCamera()->setFramerate(24);
	
	LOG("** set sliders **");
	// sliders must be set after Vision was initialized
	proc->set(CVBlobDetector::SLIDER_BACKGROUND, 0.25f);
	proc->set(CVBlobDetector::SLIDER_THRESHOLD, 0.175f);
	proc->set(CVBlobDetector::SLIDER_DILATE, 0.15f);
	proc->set(CVBlobDetector::SLIDER_ERODE, 0.025f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_MIN, 0.001f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_MAX, 1.0f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_REDUCE, 0.25f);
	proc->set(CVBlobDetector::SLIDER_TRACK_RANGE, 0.75f);

	/*
	proc->setWarp(0, 0,
				  320, 0,
				  320, 239,
				  0, 240);	
	*/
	
	LOG("** init gui **");
	CvSize windowSize = cvSize(v->getProcessor()->getROI().width, v->getProcessor()->getROI().height);
	if(windowSize.height > 240) windowSize.height = 240;
	int tmpX = 0;
	int tmpY = 45;
	
	char *windows[CVStereoBlobDetector::STAGE_MAX];
	for(int i=0; i<CVStereoBlobDetector::STAGE_MAX; i++) {
		char *windowName = (char *) malloc(50);
		sprintf(windowName, "STAGE %i", i);
		windows[i] = windowName;
		
		cvNamedWindow(windowName, NULL); //CV_WINDOW_AUTOSIZE);
		cvResizeWindow(windowName, windowSize.width, windowSize.height);
		
		if(tmpX + windowSize.width > SCREEN_WIDTH) {
			tmpX = 0;
			tmpY += windowSize.height + 23;
		}
		
		printf("moving window %i to %i | %i \n", i, tmpX, tmpY);
		cvMoveWindow(windowName, tmpX, tmpY);		
		tmpX += windowSize.width;		
	}
	
	// start vision
	LOG("** starting ** ");	
	v->start();
	
	proc->setSize(640, 480);
	
	bool updateImages = true;
	bool resetBackground = true;
	while(true) {
		v->update();
		
		if(updateImages) {
			for(int i=0; i<CVStereoBlobDetector::STAGE_MAX; i++) {
				IplImage *image = v->getProcessor()->getImage(i);
				if(image!=NULL) {
					cvShowImage(windows[i], image);
				}
			}
		}
		
		// pause thread
        int key = cvWaitKey(10);
        if(key == 'q' || key == 'Q')
            break;
		
		if(key == ' ' || key == 'u')
			updateImages = !updateImages;

		if(key == 'r') {
			proc->setROI(120, 80, 400, 260);
		}
		
		if(key == 'b') {
			float bgVal = resetBackground ? 1.0f : 0.0f;
			resetBackground = !resetBackground;
			proc->set(CVBlobDetector::SLIDER_BACKGROUND, bgVal);
		}
	}
	
	LOG("** stopping **");	
	for(int i=0; i<CVStereoBlobDetector::STAGE_MAX; i++) {
		cvDestroyWindow(windows[i]);
	}
	
	v->stop();	
	delete v;
	
	LOG("** finished **");
	return 0;
}