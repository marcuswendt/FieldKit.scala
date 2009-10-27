/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef VISION2_JNI_H
#define VISION2_JNI_H

// vision classes
#include "Vision2.h"
#include "PTGreyBumblebee2.h"
#include "CVBlobDetector.h"
#include "CVStereoBlobDetector.h"

// jni generated headers
#include "mw_lib_vision_Vision.h"

//
// CONSTANTS
//
// data buffer codes
const static int DATA_SIZE = 1000;
const static int DATA_LIST_START = -2000;
const static int DATA_LIST_END = -2001;
const static int DATA_LIST_ITEM_START = -3000;	
const static int DATA_LIST_ITEM_END = -3001;	

//
// FIELDS
//
int *dataBuffer;
int dataSize;
int dataIndex;
bool dataReverseByteOrder;

// objects
Vision::Vision2 *vision;
Vision::CVBlobDetector *proc;
Vision::PTGreyBumblebee2 *camera;

//
// FUNCTIONS
//
void dataUpdate();

#endif
