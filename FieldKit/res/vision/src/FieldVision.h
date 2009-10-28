/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef FIELD_VISION_H
#define FIELD_VISION_H

#include "Vision.h"
#include "CVFrameProcessor.h"
#include "Camera.h"

//
// Defines a C function interface to the C++ vision and camera components
//

#ifdef __cplusplus
extern "C" {
#endif
	
// global vision pointer
field::Vision* vision;
field::CVFrameProcessor* proc;
	
// functions
int fvCreate();
int fvDestroy();

int fvStart();
int fvStop();
int fvUpdate();
	
int fvSetCamera(int name);
int fvSetSize(int width, int height);
int fvSetFramerate(int fps);

// helpers
int fvError(int err);
	
#ifdef __cplusplus
}
#endif

#endif