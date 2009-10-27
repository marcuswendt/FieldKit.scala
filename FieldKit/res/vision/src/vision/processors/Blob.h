/*
 *  Blob.h
 *  vision2
 *
 *  Created by Marcus Wendt on 7/6/08.
 *  Copyright 2008 MarcusWendt.com. All rights reserved.
 *
 */

#ifndef BLOB_H
#define BLOB_H

namespace Vision 
{
	class Blob
	{
		public:
			int id;
			bool isActive;
			bool isAssigned;

			CvPoint position;
			CvPoint2D64f position64f;
		
			CvRect bounds;
			CvSeq *contour;
		
			Blob (int id) { 
				this->id = id;
				init();
			};
			~Blob () {};	
			
			
			void init() 
			{
				isActive = false;
				isAssigned = false;
			}
			
			void set(Blob *b) 
			{
				this->position = b->position;
				this->position64f = b->position64f;
				this->bounds = b->bounds;
				this->contour = b->contour;
			}
		
		protected:
	};
};
#endif