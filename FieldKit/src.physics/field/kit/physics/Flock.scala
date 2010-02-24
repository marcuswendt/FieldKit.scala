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
trait Flock extends Behavioural with Logger {
	import scala.collection.mutable.ArrayBuffer

	// -- Particles ------------------------------------------------------------
	protected var particles = new ArrayBuffer[Particle]
	
	/**
	 * Adds a new particel to this flock
	 */
	def +=(p:Particle) = {
		particles += p
		// add behaviours to particle
		if(behaviours != null)
			behaviours foreach { p += _ }
	}
	
	def add(e:Particle) = this += e
	
	/**
	 * Removes a particle from this flock
	 */
	def -=(p:Particle) = {
		particles -= p
		// remove behaviours from particle
		if(behaviours != null)
			behaviours foreach { p -= _ }
	}
	
	def remove(e:Particle) = this -= e 
	
	// -- Behaviours -----------------------------------------------------------
	override def +=(b:Behaviour) {
		super.+=(b)
		// add new behaviour to all particles
		particles foreach (_ += b)
	}
	
	override def -=(b:Behaviour) {
		super.-=(b)
		// remove behaviour from all particles
		particles foreach (_ -= b)
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
