/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 08, 2010 */
package field.kit.physics.behaviours

import field.kit.physics._
import field.kit.math.geometry._

/**
 * Constrains the particle to the inside or outside of a circle
 * @author marcus
 */
class BoxConstraint extends AABB with Behaviour {
	
	var isBounding = false
	
	def apply(p:Particle) {
//		val isInside = contains(p)
//		if((isBounding && !isInside) || 
//		  (!isBounding && isInside)) {
//			p := (p -= circle).normalise *= circle.radius += circle	
//		}
//			
	}
}