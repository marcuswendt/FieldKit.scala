/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "FieldVision.h"

#include "constants.h"
#include "OpenCVCamera.h"
#include "CVBlobDetector.h"

#ifdef __cplusplus
extern "C" {
#endif
	
// -- Create -------------------------------------------------------------------
int fvCreate() {
	if(vision != NULL) fvDestroy();
	vision = new field::Vision();
	
	proc = new field::CVBlobDetector();
	vision->setProcessor(proc);
	return FK_SUCCESS;
}

	
// -- Destroy ------------------------------------------------------------------
int fvDestroy() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	delete vision;
	return FK_SUCCESS;
}

// -- Start ---------------------------------------------------------------------
int fvStart() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	return vision->start();
}
	
// -- Stop ---------------------------------------------------------------------
int fvStop() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	return vision->stop();
}

// -- Update -------------------------------------------------------------------
int fvUpdate() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	return vision->update();
}

	
// -- Setters ------------------------------------------------------------------
int fvSetCamera(int name) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	
	// TODO!!!
	
	field::Camera* camera = new field::OpenCVCamera(0);
	vision->setCamera(camera);
	return FK_SUCCESS;
}

int fvSetSize(int width, int height) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	vision->setSize(width, height);
	return FK_SUCCESS;
}

int fvSetFramerate(int fps) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);	
	vision->setFramerate(fps);
	return FK_SUCCESS;
}
	
// -- Helpers ------------------------------------------------------------------
int fvError(int err) {
	const char* msg;
	if(err == FK_ERR_NOT_CREATED) {
		msg = "No vision object created yet";
	} else {
		msg = "Unknown error";
	}
	fprintf(stderr, "FieldVision: %s!\n", msg);
	return FK_ERROR;
}

	
#ifdef __cplusplus
}
#endif
