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
	var scale = Vec3(1f)		
	def load(file:String)
}