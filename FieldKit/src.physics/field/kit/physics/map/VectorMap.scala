/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 20, 2010 */
package field.kit.physics.map

import field.kit._
import field.kit.physics._

import field.kit.gl._

/**
 * Represents a 2D field of vectors mapped into the physics space
 */
class VectorMap(physics:Physics[_]) extends AttributeMap[Vec3](physics) {
	
	defaultValue = Vec3(0f)
	
	def load(file:String) = load(Image(file))
	
	def load(image:Image) {
		extent = new Vec3(image.width/2f, image.height/2f, 1f)
		values = new Array[Vec3](image.width * image.height)
		
		for(y <- 0 until image.height) {
			for(x <- 0 until image.width) {
				val index = (y * width + x).toInt
				val v = new Vec3
				v.x = image.red(x, y)
				v.y = image.green(x, y)
				v.z = image.blue(x, y)
				values(index) = v
			}
		}
	}
}