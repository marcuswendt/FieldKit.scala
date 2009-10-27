/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "OpenCVCamera.h"

namespace field 
{
	OpenCVCamera::OpenCVCamera(int cameraIndex) {
		this->cameraIndex = cameraIndex;
	}
	
	// -------------------------------------------------------------------------
	// INIT
	// -------------------------------------------------------------------------
	Error OpenCVCamera::init()
	{
		if(isStarted) {
			LOG_ERR("OpenCVCamera: Cannot initialize, since camera is already started.");
			return FAILURE;
		}
				
		capture = cvCreateCameraCapture(cameraIndex);
		
		if(!capture) {
			LOG_ERR("OpenCVCamera: Couldnt create camera capture.");
			return ERR_CAMERA_INIT;
		}
		
		// only seems to be implemented in the opencv linux (ffmpeg or gstreamer based) versions
		cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_WIDTH, width);
		cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_HEIGHT, height);
		cvSetCaptureProperty(capture, CV_CAP_PROP_FPS, fps);

		return super::init();
	}
	
	// -------------------------------------------------------------------------
	// CLOSE
	// -------------------------------------------------------------------------
	Error OpenCVCamera::close()
	{
		cvReleaseCapture(&capture);
		return super::close();
	}
	
	// -------------------------------------------------------------------------
	// UPDATE
	// -------------------------------------------------------------------------
	Error OpenCVCamera::update()
	{
		if(cvGrabFrame(capture)) return SUCCESS;
		return ERR_CAMERA_UPDATE;
	}
	
	// -------------------------------------------------------------------------
	// HELPERS
	// -------------------------------------------------------------------------
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
}