/*
 *  ImageCache.h
 *  vision2
 *
 *  Created by Marcus Wendt on 7/8/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#ifndef CV_IMAGE_CACHE_H
#define CV_IMAGE_CACHE_H

#include <map>

namespace Vision 
{
	class CVImageCache
	{
	public:
		const static int IMAGE_TMP = 100;
		const static int IMAGE_STAGE = 200;

		CVImageCache();
		~CVImageCache();

		// image accessors
		IplImage* get(int key, CvSize size, int depth=IPL_DEPTH_8U, int channels=1);
		
		// utilities
		IplImage* getTmp(int key, CvSize size, int depth=IPL_DEPTH_8U, int channels=1);
		IplImage* getTmp(int key, IplImage* target);
		IplImage* getStage(int key, CvSize size, int depth=IPL_DEPTH_8U, int channels=1);
		
		std::map<int, IplImage*>::iterator begin() { return map.begin(); };
		std::map<int, IplImage*>::iterator end() { return map.end(); };
		
	private:
		std::map<int, IplImage*> map;
	};
};
#endif