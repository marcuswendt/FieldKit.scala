/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "Vision.h"
#include "OpenCVCamera.h"
#include "CVBlobDetector.h"

using namespace field;

//--------------------------------------------------------------------------------------------
const int SCREEN_WIDTH = 1920;
const int SCREEN_HEIGHT = 480;

//--------------------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
	LOG("************************************************");
	LOG("TestOpenCVCapture");
	LOG("************************************************");
	LOG(" ");
	
	Vision *v = new Vision();
	
	LOG("init camera");
	v->setCamera(new OpenCVCamera(0));
	
	LOG("init frame processor");	
	CVBlobDetector *proc = new CVBlobDetector();
	proc->setStageEnabled(true);
	proc->setCameraSource(1);
	v->setProcessor(proc);
	
	LOG("** init vision **");
	v->init();
	
	LOG("** set sliders **");
	// sliders must be set after Vision was initialized
	proc->set(CVBlobDetector::SLIDER_BACKGROUND, 0.25f);
	proc->set(CVBlobDetector::SLIDER_THRESHOLD, 0.10f);
	proc->set(CVBlobDetector::SLIDER_DILATE, 0.15f);
	proc->set(CVBlobDetector::SLIDER_ERODE, 0.06f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_MIN, 0.005f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_MAX, 1.0f);
	proc->set(CVBlobDetector::SLIDER_CONTOUR_REDUCE, 0.5f);
	proc->set(CVBlobDetector::SLIDER_TRACK_RANGE, 0.5f);
//	 proc->setWarp(0, 0,
//	 320, 0,
//	 320, 239,
//	 0, 240);	
//	proc->setROI(320, 240, 320, 240);
	
	LOG("** init gui **");
	CvSize windowSize = cvSize(v->getProcessor()->getROI().width, v->getProcessor()->getROI().height);
	if(windowSize.height > 240) windowSize.height = 240;
	int tmpX = 0;
	int tmpY = 45;
	
	char *windows[CVBlobDetector::STAGE_MAX];
	for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
		char *windowName = (char *) malloc(50);
		sprintf(windowName, "STAGE %i", i);
		windows[i] = windowName;
		
		cvNamedWindow(windowName, NULL); //CV_WINDOW_AUTOSIZE);
		cvResizeWindow(windowName, windowSize.width, windowSize.height);
		
		if(tmpX + windowSize.width > SCREEN_WIDTH) {
			tmpX = 0;
			tmpY += windowSize.height + 23;
		}
		
		cvMoveWindow(windowName, tmpX, tmpY);		
		tmpX += windowSize.width;
		
	}
	
	// start vision
	LOG("** starting ** ");	
	v->start();
	bool updateStages = true;
	while(true) {
		v->update();
		
		if(updateStages) {
			for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
				IplImage *image = v->getProcessor()->getImage(i);
				if(image!=NULL) {
					cvShowImage(windows[i], image);
				}
			}
		}
		
		// pause thread
        int key = cvWaitKey(10);
        if (key == 'q' || key == 'Q')
            break;
		
        if (key == 'u' || key == ' ')
			updateStages = !updateStages;
	}
	
	LOG("** stopping **");	
	for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
		cvDestroyWindow(windows[i]);
	}
	
	v->stop();	
	delete v;
	
	LOG("** finished **");
	return 0;
}