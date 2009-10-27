/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef CANOPUS_ADVC_H
#define CANOPUS_ADVC_H

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
		
		Error init();
		Error update();
		Error close();
		
		ImagePtr getImage(int channel);
		IplImage* getIplImage();
		
	private:
		typedef Camera super;
		
		int cameraIndex;
		CvCapture* capture;
	};
};

#endif