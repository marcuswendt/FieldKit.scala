/*
 *  OpenCVCamera.h
 *  FieldVision
 *
 *  Created by Marcus Wendt on 26/10/2009.
 *  Copyright 2009 FIELD. All rights reserved.
 *
 */

#ifndef CANOPUS_ADVC_H
#define CANOPUS_ADVC_H

#include <OpenCV/OpenCV.h>
#include "Camera.h"

namespace Vision 
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
		Error start();
		Error update();
		Error stop();
		Error close();
		
		Error setFramerate(int framerate);
		
		ImagePtr getImage(int channel);
		IplImage* getIplImage();
		
	private:
		int cameraIndex;
		CvCapture* capture;
	};
};

#endif