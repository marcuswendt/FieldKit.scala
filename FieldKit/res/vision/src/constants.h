/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef CONSTANTS_H
#define CONSTANTS_H

// error codes
#define FK_SUCCESS						1
#define FK_ERROR						0

#define FK_ERR_INVALID_ARGUMENT			-10

#define FK_ERR_NOT_CREATED				-200
#define FK_ERR_NOT_INITIALIZED			-201

// camera types
#define FK_CAMERA_OPENCV				0
#define FK_CAMERA_OPENCV_FIRST			1
#define FK_CAMERA_OPENCV_SECOND			2
#define FK_CAMERA_OPENCV_THIRD			3
#define FK_CAMERA_OPENCV_FOURTH			4
#define FK_CAMERA_PTGREY_BUMBLEBEE		10

// frame processor properties
#define FK_PROC_BACKGROUND				0
#define FK_PROC_THRESHOLD				1
#define FK_PROC_DILATE					2
#define FK_PROC_ERODE					3
#define FK_PROC_CONTOUR_MIN				4
#define FK_PROC_CONTOUR_MAX				5
#define FK_PROC_CONTOUR_REDUCE			6
#define FK_PROC_TRACK_RANGE				7

namespace field {
	//
	// TYPES
	//
	typedef unsigned char Image;
	typedef Image* ImagePtr;
	
	//
	// CONSTANTS
	//
	const static int BLOB_MAX_COUNT = 10;
	
	const static int VISION_DEFAULT_WIDTH = 320;
	const static int VISION_DEFAULT_HEIGHT = 240;
	const static int VISION_DEFAULT_FPS = 30;

	//
	// INLINE FUNCTIONS
	//
	inline int ptDistanceSquared(CvPoint a, CvPoint b)
	{
		int dx = a.x - b.x;
		int dy = a.y - b.y;	
		return dx*dx + dy*dy;
	}
	
	inline float pt64fDistanceSquared(CvPoint2D64f a, CvPoint2D64f b)
	{
		double dx = a.x - b.x;
		double dy = a.y - b.y;	
		return dx*dx + dy*dy;
	}
	
	inline int iDistanceSquared(int x1, int y1, int x2, int y2)
	{
		int dx = x1 - x2;
		int dy = y1 - y2;	
		return dx*dx + dy*dy;
	}
	
	inline float dDistanceSquared(double x1, double y1, double x2, double y2)
	{
		double dx = x1 - x2;
		double dy = y1 - y2;	
		return dx*dx + dy*dy;
	}
	
	inline double dSqr(double a)
	{
		return a * a;
	}
	
	// --
	inline int isLittleEndian()
	{
		short int word = 0x0001;
		char *byte = (char *) &word;
		return(byte[0] ? 1 : 0);
	}
	
	
	inline int little2bigEndianINT(int i)
	{
		return((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff);
	}
	
	
	//
	// MACROS
	//
	#define LOG(e) printf(e); printf("\n");
	
	#define LOG_ERR(msg) \
		fprintf(stderr, msg); \
		fprintf(stderr, "\n"); \
		exit(-1);
};
#endif
