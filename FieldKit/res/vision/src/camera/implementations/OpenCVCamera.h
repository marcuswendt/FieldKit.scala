/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef OPENCV_CAMERA_H
#define OPENCV_CAMERA_H

#include <OpenCV/OpenCV.h>
#include "Camera.h"

namespace field 
{
	//
	// integrates the capture components from opencv
	// opencv.sf.net
	//
	class OpenCVCamera : public Camera
	{
	public:
		OpenCVCamera(int cameraIndex);
		~OpenCVCamera() {};
		
		int init();
		int update();
		int close();
		IplImage* getImage(int channel=0);
		
	private:
		typedef Camera super;
		
		int cameraIndex;
		CvCapture* capture;
	};
};

#endif