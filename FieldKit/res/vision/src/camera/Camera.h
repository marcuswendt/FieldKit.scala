/*
 *  Camera.h
 *  vision2
 *
 *  Created by Marcus Wendt on 6/19/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#ifndef CAMERA_H
#define CAMERA_H

namespace Vision {
	//
	// base class for all camera types
	//
	class Camera
	{
	public:
		Camera() {
			isInitialized = false;
			isStarted = false;
		};
		~Camera() {};
		
		virtual Error init() = 0;
		virtual Error start() = 0;
		virtual Error update() = 0;
		virtual Error stop()  = 0;
		virtual Error close()  = 0;
		
		virtual ImagePtr getImage(int channel) = 0;
		virtual Error setFramerate(int framerate) = 0;
		
		int getWidth() { return width; };
		int getHeight() { return height; };
		
	protected:
		int width, height;
		
		bool isInitialized;
		bool isStarted;
	};
};
#endif