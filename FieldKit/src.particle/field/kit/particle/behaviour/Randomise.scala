/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 24, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._
import field.kit.math.Vec3
import field.kit.math.Common._  

/**
 * randomly place the particle within a defined cube
 * note: min and max should be given as positive normalized vectors [0, 1]
 * @author Marcus Wendt
 */
class Randomise extends Behaviour {

	val min = Vec3(0f)
	val max = Vec3(1f)
	var weight = 1f
	
	protected val range = Vec3()
	
	override def prepare(dt:Float) {
		range := max -= min *= ps.space.dimension
	}
	
	def apply(p:Particle, dt:Float) {
		p.x += range.x * randomNormal * weight
		p.y += range.y * randomNormal * weight
		p.z += range.z * randomNormal * weight
	}
}
