/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 16, 2010 */
package field.kit.physics.map

import field.kit._
import field.kit.math.geometry._
import field.kit.physics._

/**
 * Base class for all attribute maps 
 */
abstract class AttributeMap(physics:Physics[_]) extends AABB {
	
	protected var _scale = Vec3(1f)
	protected var invScale = Vec3(1f)
	
	def scale = _scale
	def scale_=(v:Vec3) = {
		_scale := v
		invScale := 1f /= v
		updateBounds
	}

	override def updateBounds = {
		val d = extent * scale
		min := this -= d
		max := this += d
		this
	}
	
	def load(file:String)
	
	/**
	 * @return the data-index at the given position or -1 when outside of the boundaries
	 */
	def indexOf(v:Vec3):Int = {
		if(v.z < min.z || v.z > max.z) return -1
			
		var ix = ((v.x - min.x) * invScale.x).toInt
		var iy = ((v.y - min.y) * invScale.y).toInt
		
		// check if we're within this fields boundaries
		if(ix < 0 || ix >= width)
			return -1
			
		if(iy < 0 || iy >= height)
			return -1
		
		// calculate index
		(iy * width + ix).toInt		
	}
}