/*
 *  Vision2.h
 *  vision2
 *
 *  Created by Marcus Wendt on 6/19/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#ifndef VISION2_H
#define VISION2_H

#include "Camera.h"
#include "CVFrameProcessor.h"

namespace Vision 
{
	class Vision2
	{
	public:
		Vision2() {};
		~Vision2();
		
		Error init();
		Error start();
		virtual Error update();
		Error stop();
		
		// setters
		void setCamera(Camera *camera) { this->camera = camera; };
		void setProcessor(CVFrameProcessor *processor) { this->processor = processor; };
		
		// getters
		Camera* getCamera() { return camera; };
		CVFrameProcessor* getProcessor() { return processor; };
		
	protected:
		Camera *camera;
		CVFrameProcessor *processor;
		
	private:
		Error err;
	};
};
#endif