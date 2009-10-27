/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef VISION_H
#define VISION_H

#include "Camera.h"
#include "CVFrameProcessor.h"

namespace field
{
	class Vision
	{
	public:
		Vision();
		~Vision();
		
		Error init();
		Error start();
		virtual Error update();
		Error stop();
		Error shutdown();
		
		// setters
		void setSize(int width, int height);
		void setFramerate(int fps);
		void setCamera(Camera *camera);
		void setProcessor(CVFrameProcessor *processor);
		
		// getters
		int getWidth() { return width; };
		int getHeight() { return height; };
		int getFPS() { return fps; };
		
		Camera* getCamera() { return camera; };
		CVFrameProcessor* getProcessor() { return processor; };
		
	protected:
		bool isInitialized;
		bool isStarted;
		int width, height, fps;
		Camera *camera;
		CVFrameProcessor *processor;
	};
};
#endif