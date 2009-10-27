/*
 *  Slider.h
 *  vision2
 *
 *  Created by Marcus Wendt on 6/21/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#ifndef SLIDER_H
#define SLIDER_H

namespace Vision 
{
	class Slider
	{
	public:
		Slider (float min, float max) { init(min, max); };
		~Slider();
		
		void init (float min, float max) {
			this->min = min;
			this->scale = max - min;
			this->value = 0;
		};
		
		void setValue(float v) { 
			v = (v > 1) ? 1 : v;
			v = (v < 0) ? 0 : v;
			value = v;
		};
		
		float getValue() { return value;};
		
		float get() { return (value*scale) + min;};
		
	private:
		float value;
		float scale;
		float min;
	};
};
#endif