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
 * Base trait for all physical effectors that move or push an individual 
 * particle around the simulation space.
 * 
 * Possible behaviours can be roughly categorised in:
 *  
 * - Steering behaviours, which update the particle's steer vector 
 *   e.g. Gravity, Attraction, Brownian Align etc.
 *   
 * - Constraint behaviours, which set the particle's position to meet a certain requirement 
 *   e.g. on collision with geometry, emitter placement
 *   
 * - Custom behaviours: that steer or set other properties of the particle 
 *   e.g. Colour steering
 */
trait Behaviour extends Logger {
	
	/**
	 * Applies this behaviour to the given particle
	 */
	def apply(p:Particle)
	
	// support for creating identical copies
	type T <: Behaviour
	
	/**
	 * @return returns an identical copy of this behaviour
	 */
	def copy:T
}

/**
 * Base trait for all classes using physics behaviours (PhysicsSystem, Flock, Particle)
 * It is up to the implementing class to define how the behaviours are actually used.
 */
trait Behavioural {
	import scala.collection.mutable.ArrayBuffer
	
	protected var behaviours:ArrayBuffer[Behaviour] = _
	
	/**
	 * Adds the given Behaviour
	 */
	def +=(b:Behaviour) {
		if(behaviours == null)
			behaviours = new ArrayBuffer[Behaviour]
		behaviours += b
	}
	
	def add(b:Behaviour) = this += b
	
	def -=(b:Behaviour) {
		if(behaviours == null) return
		// TODO this should probably match against the behaviour's class or type
		// not the isntance variable
		behaviours -= b
	}
	
	def remove(b:Behaviour) = this -= b

	/**
	 * Each Behavioural class needs some form of updating its state
	 */
	def update(dt:Float)
}
