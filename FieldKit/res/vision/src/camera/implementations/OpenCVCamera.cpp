/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "OpenCVCamera.h"

namespace Vision 
{
	OpenCVCamera::OpenCVCamera(int cameraIndex) {
		this->cameraIndex = cameraIndex;
	}
	
	// ------------------------------------------------------------------------------------------------
	// INIT
	// ------------------------------------------------------------------------------------------------
	Error OpenCVCamera::init()
	{
		capture = cvCreateCameraCapture(cameraIndex);
		
		if(!capture)
			return ERR_CAMERA_INIT;
			
		IplImage* image = cvRetrieveFrame(capture);
		width = image->width;
		height = image->height;
		
		return SUCCESS;
	}
	
	// ------------------------------------------------------------------------------------------------
	// START
	// ------------------------------------------------------------------------------------------------
	Error OpenCVCamera::start()
	{
		return SUCCESS;
	}
	
	// ------------------------------------------------------------------------------------------------
	// STOP
	// ------------------------------------------------------------------------------------------------
	Error OpenCVCamera::stop()
	{
		return SUCCESS;
	}
	
	// ------------------------------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------------------------------
	Error OpenCVCamera::close()
	{
		cvReleaseCapture(&capture);
		return SUCCESS;
	}
	
	// ------------------------------------------------------------------------------------------------
	// UPDATE
	// ------------------------------------------------------------------------------------------------
	Error OpenCVCamera::update()
	{
		if(cvGrabFrame(capture) == 1) {
			return SUCCESS;
		} else {
			return ERR_CAMERA_UPDATE;
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	// HELPERS
	// ------------------------------------------------------------------------------------------------
	#pragma mark -- Helpers --
	ImagePtr OpenCVCamera::getImage(int channel)
	{
		IplImage* image = cvRetrieveFrame(capture);
		printf("get image: %i %i \n", image->width, image->height);
		return (ImagePtr) image->imageData;
	}
	
	IplImage* OpenCVCamera::getIplImage() {
		return cvRetrieveFrame(capture);
	}
	
	Error OpenCVCamera::setFramerate(int framerate)
	{
		return SUCCESS;
	}
}