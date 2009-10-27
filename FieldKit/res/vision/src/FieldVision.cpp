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
	return FK_SUCCESS;
}

// -- Setters ------------------------------------------------------------------
int fvSetCamera(int name) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	
	return FK_SUCCESS;
}

int fvSetSize(int width, int height) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	
	return FK_SUCCESS;
}

int fvSetFramerate(int fps) {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);	
	
	return FK_SUCCESS;
}

// -- Init ---------------------------------------------------------------------
int fvInit() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	
	return FK_SUCCESS;
}

// -- Update -------------------------------------------------------------------
int fvUpdate() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);
	
	return FK_SUCCESS;
}

// -- Destroy ------------------------------------------------------------------
int fvDestroy() {
	if(!vision) return fvError(FK_ERR_NOT_CREATED);	
	
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
