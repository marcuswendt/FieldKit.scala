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
	
	var values:Array[Float] = _
	
	def load(file:String) = load(Image(file))
	
	def load(image:Image) {
		extent = new Vec3(image.width/2f, image.height/2f, 1f)
		values = new Array[Float](image.width * image.height)
		
		for(y <- 0 until image.height) {
			for(x <- 0 until image.width) {
				val index = (y * width + x).toInt
				values(index) = image.red(x, y)
			}
		}
	}
	
	/** @return the value of this map at the specified position */
	def apply(v:Vec3):Float = {
		val index = indexOf(v)
		if(index == -1) 0f else values(index)
	}
}