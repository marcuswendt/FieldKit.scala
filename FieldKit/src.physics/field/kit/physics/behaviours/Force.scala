/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 07, 2010 */
package field.kit.physics.behaviours

import field.kit._
import field.kit.physics._
import field.kit.math.geometry._

/**
 * Applies a constant force to each particle
 */
class Force extends Vec3 with Behaviour {
	
	var weight = 1f
	protected val tmp = new Vec3
	
	def this(x:Float, y:Float, z:Float) {
		this()
		set(x,y,z)
	}
	
	def this(v:Vec3) {
		this()
		set(v)
	}
	
	def apply(p:Particle) {
		tmp := this
		tmp *= weight
		p.force += tmp 
	}
}