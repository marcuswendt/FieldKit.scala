/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "Vision2.h"

namespace Vision
{
	Vision2::~Vision2()
	{
		err = camera->close();
		delete camera;	
		delete processor;
	}
	
	
	// ------------------------------------------------------------------------------------------------
	// INIT
	// ------------------------------------------------------------------------------------------------
	Error Vision2::init()
	{
		err = camera->init();
		if(err != SUCCESS) return err;
		
		err = processor->init();
		
		return SUCCESS;
	}

	// ------------------------------------------------------------------------------------------------
	// START
	// ------------------------------------------------------------------------------------------------
	Error Vision2::start()
	{
		err = camera->start();
		if(err != SUCCESS) return err;		
		
		return SUCCESS;
	}

	// ------------------------------------------------------------------------------------------------
	// STOP
	// ------------------------------------------------------------------------------------------------
	Error Vision2::stop()
	{
		err = camera->stop();
		if(err != SUCCESS) return err;

		return SUCCESS;		
	}
	
	// ------------------------------------------------------------------------------------------------
	// UPDATE
	// ------------------------------------------------------------------------------------------------
	Error Vision2::update()
	{
		//LOG("** camera->update **");		
		err = camera->update();
		if(err != SUCCESS) return err;

		//LOG("** processor->update **");
		err = processor->update(camera);
		if(err != SUCCESS) return err;
		
		return SUCCESS;
	};
};