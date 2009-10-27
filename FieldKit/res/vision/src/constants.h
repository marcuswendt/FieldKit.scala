/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef CONSTANTS_H
#define CONSTANTS_H

namespace Vision {
	//
	// TYPES
	//
	typedef unsigned char Image;
	typedef Image* ImagePtr;
	
	//
	// CONSTANTS
	//
	const static int BLOB_MAX_COUNT = 10;
	
	typedef enum {
		SUCCESS =  0,
		FAILURE,
		ERR_CAMERA_INIT,
		ERR_CAMERA_START,
		ERR_CAMERA_UPDATE,
		ERR_CAMERA_STOP,
		ERR_CAMERA_CLOSE,
		ERR_NO_CAMERA_FOUND,
		ERR_CAMERA_CANT_SET_FRAMERATE,
		ERR_CAMERA_INVALID_CHANNEL,
		ERR_CAMERA_NOT_INITIALIZED,
		ERR_CAMERA_ALREADY_STARTED,
		ERR_CAMERA_NOT_STARTED,
	} Error;

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
