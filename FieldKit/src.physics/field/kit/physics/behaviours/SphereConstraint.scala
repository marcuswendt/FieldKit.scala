/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 01, 2010 */
package field.kit.physics.behaviours

import field.kit._
import field.kit.physics._
import field.kit.math.geometry._

/**
 * Constrains the particle to the inside or outside of a sphere
 * @author marcus
 */
class SphereConstraint(var isBoundingSphere:Boolean) 
extends Sphere with Constraint {
	
	def this(sphere:Sphere, isBoundingSphere:Boolean) {
		this(isBoundingSphere)
		this := sphere
	}
	
	def this(center:Vec3, radius:Float, isBoundingSphere:Boolean) {
		this(isBoundingSphere)
		this := center
		this.radius = radius
		//println("sphere", center, "radius", radius)
	}
	
	def this(radius:Float, isBoundingSphere:Boolean) {
		this(isBoundingSphere)
		this.radius = radius
	}
	
	def apply(p:Particle) {
		val isInside = contains(p)
		if((isBoundingSphere && !isInside) || (!isBoundingSphere && isInside)) {
			val s = this.asInstanceOf[Sphere]
			p := (p -= s).normalize *= radius += s	
		}
	}
}