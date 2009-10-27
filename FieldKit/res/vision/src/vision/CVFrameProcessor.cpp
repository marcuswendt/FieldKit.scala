/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "CVFrameProcessor.h"

namespace field 
{	
	// ---------------------------------------------------------------------------------
	#pragma mark -- Constructor --
	// ---------------------------------------------------------------------------------
	CVFrameProcessor::CVFrameProcessor()
	{
		// colors
		cWhite		= cvScalarAll(255);
		cGrey		= cvScalarAll(128);
		cDarkGrey	= cvScalarAll(64);
		cLightGrey	= cvScalarAll(192);	
		cBlack		= cvScalarAll(0);
		
		cRed		= cvScalar(0, 0, 255);
		cGreen		= cvScalar(0, 255, 0);
		cBlue		= cvScalar(255, 0, 0);
		
		cache = new CVImageCache();

		sliders = std::map<int, Slider*>();
		stages = std::map<int, Stage*>();
		
		setSize(320, 240);
		setROI(0, 0, 320, 240);
	}

	
	// ---------------------------------------------------------------------------------
	#pragma mark -- Destructor --
	// ---------------------------------------------------------------------------------	
	CVFrameProcessor::~CVFrameProcessor()
	{	
		delete cache;
	}
	

	// ---------------------------------------------------------------------------------
	#pragma mark -- Stages --	
	// ---------------------------------------------------------------------------------
	void CVFrameProcessor::initStages(int num)
	{
		for(int i=0; i<num; i++) {
			getStage(i);
		}
	}
	
	Stage* CVFrameProcessor::getStage(int key)
	{
		std::map<int, Stage*>::iterator i = stages.find(key);
		
		// requested stage couldnt be found in the map
		// -> create and add it
		if(i == stages.end()) {
		   Stage *stage = new Stage(key);
		   stages.insert(std::make_pair(key, stage));
		   return stage;
		   
	   } else {		   
		   return (*i).second;	
	   }
	}
	
	void CVFrameProcessor::setStageEnabled(bool enabled, int key)
	{
		if(key == -1) {
			std::map<int, Stage*>::iterator i;
			for(i=stages.begin(); i != stages.end(); i++) {
				(*i).second->isEnabled = enabled;
			}
		} else {
			Stage *stage = getStage(key);
			stage->isEnabled = enabled;	
		}		
	}
	
	IplImage* CVFrameProcessor::getImage(int key)
	{
		return getStage(key)->image;
	}
	
	void CVFrameProcessor::copyStage(int key, IplImage *image)
	{
		Stage *stage = getStage(key);

		if(stage->isEnabled) {
			int width, height;
			if(image->roi == NULL) {
				width = image->width;
				height = image->height;
			} else {
				width = image->roi->width;
				height = image->roi->height;	
			}
			
			IplImage *stageImage = cache->getStage(key, cvSize(width, height), IPL_DEPTH_8U, image->nChannels);
			cvConvert(image, stageImage);
			stage->image = stageImage;
		}
	}
	
	int CVFrameProcessor::getStageNum()
	{
		return stages.size();
	}
	
	
	// ---------------------------------------------------------------------------------
	#pragma mark -- Sliders --
	// ---------------------------------------------------------------------------------
	void CVFrameProcessor::set(int key, float value) 
	{
		Slider* s = getSlider(key);
		if(s!=NULL) s->setValue(value);
	}
	
	float CVFrameProcessor::get(int key) 
	{
		Slider* s = getSlider(key);
		return (s==NULL) ? 0 : s->get();
	};
	
	void CVFrameProcessor::setSlider(int key, Slider* slider)
	{
		sliders.insert(std::make_pair(key, slider)); 
	}
	
	Slider* CVFrameProcessor::getSlider(int key) 
	{
		std::map<int, Slider*>::iterator i = sliders.find(key);
		return (*i).second;
	}	
	
	void CVFrameProcessor::setROI(int x, int y, int w, int h)
	{
		roi = cvRect(x, y, w, h);

		if(roi.x + roi.width >= size.width) roi.width = max(size.width,roi.x) - min(size.width, roi.x) - 1;
		if(roi.y + roi.height >= size.height) roi.height = max(size.height,roi.y) - min(size.height, roi.y) - 1;
		
		// printf("CVFrameProcessor::setROI x%i y%i w%i h%i \n", roi.x, roi.y, roi.width, roi.height);
		
		roiSize = cvSize(roi.width, roi.height);
	}
	
	void CVFrameProcessor::setSize(int width, int height)
	{
		size = cvSize(width, height);
	}
};