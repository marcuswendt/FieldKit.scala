/*
 *  jniVision2.cpp
 *  vision2
 *
 *  Created by Marcus Wendt on 7/8/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#include "Vision2JNI.h"

// -------------------------------------------------------------------------------
#pragma mark -- Create --
// -------------------------------------------------------------------------------
JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniCreate
	(JNIEnv *env, jobject obj)
{
	dataReverseByteOrder = Vision::isLittleEndian();
	dataSize = DATA_SIZE;
	dataBuffer = new int[dataSize];
	dataIndex = 0;
	
//	proc = new Vision::CVStereoBlobDetector();
	proc = new Vision::CVBlobDetector();
	proc->setSize(640, 480);
	proc->setStageEnabled(true);

	camera = new Vision::PTGreyBumblebee2();
	
	vision = new Vision::Vision2();
	vision->setCamera(camera);
	vision->setProcessor(proc);
	vision->init();
	
	//vision->getCamera()->setFramerate(30);
	
	vision->start();
}

// -------------------------------------------------------------------------------
#pragma mark -- Update --
// -------------------------------------------------------------------------------
JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniUpdate
(JNIEnv *env, jobject obj)
{
	vision->update();
	dataUpdate();
}

// -------------------------------------------------------------------------------
#pragma mark -- Destroy --
// -------------------------------------------------------------------------------
JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniDestroy
(JNIEnv *env, jobject obj)
{
	vision->stop();
	delete vision;
}


// -------------------------------------------------------------------------------
#pragma mark -- Data --
// -------------------------------------------------------------------------------
inline void dataPush(int value)
{
	if(dataReverseByteOrder) value = Vision::little2bigEndianINT(value);
	dataBuffer[dataIndex++] = value;
}


void dataUpdate()
{
	// clean up
	dataIndex=0;
	for(int i=0; i<dataSize; i++)
		dataBuffer[i] = 0;
		
	// push new values
	dataPush(DATA_LIST_START);	
	for (int i=0; i < proc->getBlobNum(); i++) {
		Vision::Blob *blob = proc->getBlobs()[i];
		
		dataPush(DATA_LIST_ITEM_START);
		dataPush(blob->id);
		dataPush(blob->isActive);
		dataPush(blob->position.x);
		dataPush(blob->position.y);
		dataPush(DATA_LIST_ITEM_END);
	}
	dataPush(DATA_LIST_END);
}


// -------------------------------------------------------------------------------
#pragma mark -- Setters --
// -------------------------------------------------------------------------------
JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniSetSize
	(JNIEnv *env, jobject obj, jint width, jint height)
{
	proc->setSize(width, height);
}

JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniSetROI
	(JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
//	printf("setROI %i, %i %ix%i\n", x,y,width,height);
	proc->setROI(x,y,width,height);
}

JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniSetSlider
	(JNIEnv *env, jobject obj, jint slider, jfloat value)
{
	proc->set(slider, value);
}

JNIEXPORT void JNICALL Java_mw_lib_vision_Vision_jniSetWarp
	(JNIEnv *env, jobject obj, jint sx1, jint sy1, jint sx2, jint sy2, jint sx3, jint sy3, jint sx4, jint sy4)
{
	proc->setWarp(sx1, sy1, sx2, sy2, sx3, sy3, sx4, sy4);
}


// -------------------------------------------------------------------------------
#pragma mark -- Getters --
// -------------------------------------------------------------------------------
JNIEXPORT jint JNICALL Java_mw_lib_vision_Vision_jniGetWidth
	(JNIEnv *env, jobject obj)
{
	return proc->getROI().width;
//	return proc->getSize().width;
}

JNIEXPORT jint JNICALL Java_mw_lib_vision_Vision_jniGetHeight
(JNIEnv *env, jobject obj)
{
	return proc->getROI().height;
//	return proc->getSize().height;
}

JNIEXPORT jobject JNICALL Java_mw_lib_vision_Vision_jniGetBlobData
	(JNIEnv *env, jobject obj)
{
	return env->NewDirectByteBuffer(dataBuffer, dataSize);
}


// -------------------------------------------------------------------------------
#pragma mark -- Stages --
// -------------------------------------------------------------------------------
JNIEXPORT void JNICALL Java_mw_lib_vision_Stage_jniSetEnabled
(JNIEnv *env, jobject obj, jboolean enabled, jint stage)
{
	proc->setStageEnabled(enabled, stage);
}

JNIEXPORT jobject JNICALL Java_mw_lib_vision_Stage_jniGetImageBuffer
	(JNIEnv *env, jobject obj, jint stage)
{
	IplImage *image = proc->getImage(stage);
	return (image == NULL) ? NULL : env->NewDirectByteBuffer(image->imageData, image->imageSize);
}

JNIEXPORT jint JNICALL Java_mw_lib_vision_Stage_jniGetWidth
(JNIEnv *env, jobject obj, jint stage)
{
	IplImage *image = proc->getImage(stage);
	if(image == NULL) return 0;
	return image->width;
//	if(image->roi == NULL) return image->width;
//	return image->roi->width;
}

JNIEXPORT jint JNICALL Java_mw_lib_vision_Stage_jniGetHeight
(JNIEnv *env, jobject obj, jint stage)
{
	IplImage *image = proc->getImage(stage);
	if(image == NULL) return 0;
	return image->height;
//	if(image->roi == NULL) return image->height;
//	return image->roi->height;
}
