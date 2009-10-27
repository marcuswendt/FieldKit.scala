/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "FieldVision.h"
#include "OpenCVCamera.h"

using namespace field;

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
	
	camera->setSize(320, 240);
	camera->setFramerate(24);
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