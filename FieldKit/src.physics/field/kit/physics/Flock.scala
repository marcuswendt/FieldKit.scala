/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit.physics

import field.kit._

/**
 * Defines a group of particles influenced by the same behaviours
 */
trait Flock extends Updateable with Behavioural with Logger {
	import scala.collection.mutable.ArrayBuffer

	// -- Particles ------------------------------------------------------------
	protected var particles = new ArrayBuffer[Particle]
	
	def +=(p:Particle) = {
		particles += p
	}
	
	def -=(p:Particle) = {
		particles -= p
	}
	
	// -- Behaviours -----------------------------------------------------------
	override def +=(e:Behaviour) {
		super.+=(e)
		particles foreach (_ += e.copy)
	}
	
	override def -=(e:Behaviour) {
		super.-=(e)
	}
	
	/**
	 * Updates all particles
	 */
	def update(dt:Float) {
		var i = 0
		while(i < particles.length) {
			particles(i).update(dt)
			i += 1
		}
	}
}
