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
	}
	
	def load(file:String)
}