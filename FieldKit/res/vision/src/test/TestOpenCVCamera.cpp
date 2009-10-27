/*
 *  TestOpenCVCaptureBasic.cpp
 *  FieldVision
 *
 *  Created by Marcus Wendt on 26/10/2009.
 *  Copyright 2009 FIELD. All rights reserved.
 *
 */

#include "Vision2.h"
#include "OpenCVCamera.h"

using namespace Vision;

//--------------------------------------------------------------------------------------------
const int SCREEN_WIDTH = 1920;
const int SCREEN_HEIGHT = 480;

//--------------------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
	LOG("************************************************");
	LOG("TestOpenCVCaptureBasic");
	LOG("************************************************");
	LOG(" ");
	
	OpenCVCamera* camera = new OpenCVCamera(0);
	
	camera->init();
	camera->start();
	
	cvNamedWindow("mywindow", CV_WINDOW_AUTOSIZE);
	
	while( 1 ) {
		
		camera->update();
		IplImage* frame = camera->getIplImage();
		
		cvShowImage("mywindow", frame);
		
		//If ESC key pressed, Key=0x10001B under OpenCV 0.9.7(linux version),
		//remove higher bits using AND operator
		if( (cvWaitKey(10) & 255) == 27 ) break;
	}
	
	// Release the capture device housekeeping
	camera->close();
	cvDestroyWindow("mywindow");
}