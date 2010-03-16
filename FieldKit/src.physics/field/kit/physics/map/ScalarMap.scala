/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 16, 2010 */
package field.kit.physics.map

import field.kit._
import field.kit.physics._

import field.kit.gl._

/**
 * Represents a 2D field of scalar values mapped into the 3d physics space
 */
class ScalarMap(physics:Physics[_]) extends AttributeMap(physics) {
		
	private var width = 0
	private var height = 0
	
	private var halfWidth = 0
	private var halfHeight = 0
	
	var values:Array[Float] = _
	
	def load(file:String) = load(Image(file))
	
	def load(image:Image) {
		width = image.width
		height = image.height
		
		halfWidth = width / 2
		halfHeight = height / 2
		
		val depth = 1f

		extent = new Vec3(width/2f, height/2f, depth/2f)
		values = new Array[Float](image.width * image.height)
		
		for(y <- 0 until image.height) {
			for(x <- 0 until image.width) {
				val index = y * width + x
				values(index) = image.red(x, y)
			}
		}
	}
	
	/** @return the value of this map at the specified position */
	def apply(v:Vec3):Float = {
		if(!contains(v))
			return 0f
		
		// get position in image
		var ix = v.x - min.x
		var iy = v.y - min.y
		
		// apply scale
		ix = (ix * invScale.x + (halfWidth * (1f - invScale.x))).toInt
		iy = (iy * invScale.y + (halfHeight * (1f - invScale.y))).toInt
		
		// check if we're still in the image's boundaries
		if(ix < 0 || ix >= width)
			return 0f
			
		if(iy < 0 || iy >= height)
			return 0f
		
		// sample image
		val i = (iy * width + ix).toInt			
		values(i)
	}
}