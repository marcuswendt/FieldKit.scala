/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

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