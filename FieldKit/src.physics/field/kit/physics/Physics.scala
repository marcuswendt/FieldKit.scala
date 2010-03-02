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
class Physics[T <: Particle](implicit m:Manifest[T]) extends Behavioural with Logger {
	import scala.collection.mutable.ArrayBuffer

	/**
	 * Defines the 3D simulation space dimensions
	 */
	var space = new OctreeSpace(1000f, 1000f, 1000f)
	
	/**
	 * Emits new particles into the space
	 */
	var emitter = new Emitter[T](this)
	
	// -- Particles ------------------------------------------------------------
	var particles = new ArrayBuffer[T]
	
	/**
	 * Adds a new particle to this flock
	 */
	def +=(p:T) = {
		particles += p
		// add behaviours to particle
		if(behaviours != null)
			behaviours foreach { p += _ }
	}
	
	def add(e:T) = this += e
	
	/**
	 * Removes a particle from this flock
	 */
	def -=(p:T) = {
		particles -= p
		// remove behaviours from particle
		if(behaviours != null)
			behaviours foreach { p -= _ }
	}
	
	def remove(e:T) = this -= e 
	
	def apply(i:Int) = particles.apply(i)
	 
	def size = particles.size
	
	def clear {
		particles.clear
		if(behaviours != null)
			behaviours.clear
	}
	
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
		// update emitter
		emitter.update(dt)
		
		// update all particles
		var i = 0
		while(i < particles.length) {
			particles(i).update(dt)
			i += 1
		}
	}
	
	// -- Neighbours -----------------------------------------------------------
	import field.kit.math.geometry.Sphere
	var neighbourSearchVolume = new Sphere(50f)
	
	/**
	 * Update neighbour lists for all particles in system
	 */
	protected def updateNeighbours {

		// clear space
		space.clear
		
		// add all known particles into space
		var i = 0
		while(i < particles.size) {
			space insert particles(i)
			i += 1
		}
		
		i = 0
		while(i < particles.size) {
			val p = particles(i)
			p.clearNeighbours
			neighbourSearchVolume := p
			space(neighbourSearchVolume, p.neighbours)
			i += 1
		}
	}
	
	// -- Springs --------------------------------------------------------------
	var springs = new ArrayBuffer[Spring]
	
	def +=(s:Spring) = springs += s
	def -=(s:Spring) = springs -= s
	
	/**
     * Updates all spring connections based on new particle positions
     */
    protected def updateSprings {
		val numIterations = 20
		var i = 0
		while(i < springs.length) {
			springs(i).update
			i += 1
		}
    }
}
