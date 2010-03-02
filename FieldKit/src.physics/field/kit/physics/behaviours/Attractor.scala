/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 01, 2010 */
package field.kit.physics.behaviours

import field.kit._
import field.kit.physics._
import field.kit.math.geometry._

/**
 * Drags the particle towards a certain point in space
 */
class AttractorPoint(val weight:Float) extends Vec3 with Behaviour {
	protected val tmp = new Vec3
	
	def this(v:Vec3, weight:Float) {
		this(weight)
		this := v
		this
	}
	
	def apply(p:Particle) {
		tmp := this
		tmp -= p
		tmp.normaliseTo(weight)
		p.force += tmp
	}
	
	type T = AttractorPoint
	def copy = new AttractorPoint(this, weight)
}
