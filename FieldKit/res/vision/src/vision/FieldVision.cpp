/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "FieldVision.h"

namespace field
{
	FieldVision::FieldVision() {
		isInitialized = false;
		isStarted = false;
		width = VISION_DEFAULT_WIDTH;
		height = VISION_DEFAULT_HEIGHT;
		fps = VISION_DEFAULT_FPS;
	}
	
	FieldVision::~FieldVision()
	{
		camera->close();
		delete camera;	
		delete processor;
	}

	// -------------------------------------------------------------------------
	// SETTERS
	// -------------------------------------------------------------------------
	void FieldVision::setSize(int width, int height) {
		if(isStarted) {
			LOG_ERR("FieldVision: Cannot set size, since vision is already started.");
			return;
		}
		this->width = width;
		this->height = height;
	}
	
	void FieldVision::setFramerate(int fps) {
		if(isStarted) {
			LOG_ERR("FieldVision: Cannot set framerate, since camera is already started.");
			return;
		}
		this->fps = fps;
	}
	
	void FieldVision::setCamera(Camera *camera) { 
		if(isStarted) {
			LOG_ERR("FieldVision: Cannot set camera, since camera is already started.");
			return;
		}		
		this->camera = camera; 
	}
	
	void FieldVision::setProcessor(CVFrameProcessor *processor) {
		if(isStarted) {
			LOG_ERR("FieldVision: Cannot set frame processor, since camera is already started.");
			return;
		}		
		this->processor = processor; 
	}
	
	
	// -------------------------------------------------------------------------
	// INIT
	// -------------------------------------------------------------------------
	Error FieldVision::init()
	{
		// initialize camera
		if(camera == NULL) {
			LOG_ERR("FieldVision: Cannot initialize, since there is no camera set yet.");
			return FAILURE;
		}
		camera->setSize(width, height);
		camera->setFramerate(fps);

		Error err;
		err = camera->init();
		if(err != SUCCESS) return err;

		// initialize frame processor
		if(processor == NULL) {
			LOG_ERR("FieldVision: Cannot initialize, since there is no frame processor set yet.");
			return FAILURE;
		}

		processor->setSize(width, height);
		err = processor->init();
		
		if(err == SUCCESS) isInitialized = true;
		
		return err;
	}

	// -------------------------------------------------------------------------
	// START
	// -------------------------------------------------------------------------
	Error FieldVision::start()
	{
		Error err;
		
		// check if we need to initialize first
		if(!isInitialized) {
			err = this->init();
			if(err != SUCCESS) return err;
		}
		
		// all good, begin grabbing frames
		err = camera->start();
		
		if(err == SUCCESS) isStarted = true;
		return err;
	}

	// -------------------------------------------------------------------------
	// STOP
	// -------------------------------------------------------------------------
	Error FieldVision::stop()
	{
		if(!isStarted) return FAILURE;

		Error err;
		err = camera->stop();
		
		if(err == SUCCESS) isStarted = false;
		return err;
	}
	
	// -------------------------------------------------------------------------
	// UPDATE
	// -------------------------------------------------------------------------
	Error FieldVision::update()
	{
		if(!isStarted) {
			LOG_ERR("FieldVision: Cannot update, since vision is not started yet.");
			return FAILURE;
		}
		
		Error err;
		err = camera->update();
		if(err != SUCCESS) return err;

		err = processor->update(camera);
		if(err != SUCCESS) return err;
		
		return SUCCESS;
	};
};