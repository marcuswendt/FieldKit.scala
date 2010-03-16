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
		
	// -- Update ---------------------------------------------------------------
	
	/**
	 * Updates all particles
	 */
	def update(dt:Float) {
		if(emitter != null)
			emitter.update(dt)
			
		updateParticles(dt)
		updateSprings
		updateNeighbours
	}
	
	// -- Particles ------------------------------------------------------------
	var particles = new ArrayBuffer[T]
	
	protected def updateParticles(dt:Float) {
		var i = 0
		while(i < particles.length) {
			particles(i).update(dt)
			i += 1
		}
	}
	
	/**
	 * Adds a new particle to this flock
	 */
	def +=(p:T) = {
		particles += p
		
		// add behaviours to particle
		if(behaviours != null)
			behaviours foreach { p += _ }
		
		if(constraints != null)
			constraints foreach { p += _ }
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
		
		if(constraints != null)
			constraints foreach { p -= _ }
	}
	
	def remove(e:T) = this -= e 
	
	def apply(i:Int) = particles.apply(i)
	 
	def size = particles.size
	
	def clear {
		particles.clear
		
		if(behaviours != null)
			behaviours.clear
			
		if(constraints != null)
			constraints.clear
			
		if(springs != null)
			springs.clear
	}
	
	// -- Behaviours ---------------------------------------------------------------
	override def +=(b:Behaviour) {
		
		warn("added behaviour", b.getClass)
		
		super.+=(b)
		// add new behaviour to all particles
		particles foreach (_ += b)
	}
	
	override def -=(b:Behaviour) {
		super.-=(b)
		// remove behaviour from all particles
		particles foreach (_ -= b)
	}
	
	// -- Constraints ----------------------------------------------------------
	override def +=(c:Constraint) {
		
		warn("added constraint", c.getClass)
		super.+=(c)
		// add new behaviour to all particles
		particles foreach (_ += c)
	}
	
	override def -=(c:Constraint) {
		super.-=(c)
		// remove behaviour from all particles
		particles foreach (_ -= c)
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
	var springs:ArrayBuffer[Spring] = _
	
	def +=(s:Spring) = {
		if(springs == null)
			springs = new ArrayBuffer[Spring]
		springs += s
	}
	
	def add(s:Spring) = this += s
	
	def -=(s:Spring) = {
		if(springs != null)
			springs -= s
	}
	
	def remove(s:Spring) = this -= s
	
	/**
     * Updates all spring connections based on new particle positions
     */
    protected def updateSprings {
    	if(springs == null) return
    	
//		val numIterations = 20
		
		var i = 0
		while(i < springs.length) {
			springs(i).update(true)
			i += 1
		}
    }
}
