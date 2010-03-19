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
}


/** 
 * A constraint is a special type of Behaviour that may not be invalidated by other behaviours
 * such as forces like gravity etc. 
 */
trait Constraint extends Behaviour


/**
 * Base trait for all classes using physics behaviours (Physics, Particle)
 * It is up to the implementing class to define how the behaviours are actually used.
 */
trait Behavioural {
	import scala.collection.mutable.ArrayBuffer
	
	
	// -- Behaviours -----------------------------------------------------------
	
	protected var behaviours:ArrayBuffer[Behaviour] = _
	
	/**
	 * Adds the given Behaviour to this Behavioural
	 */
	def +=(b:Behaviour) {
		if(behaviours == null) behaviours = new ArrayBuffer[Behaviour](6)
		behaviours += b
	}
	
	/**
	 * Adds the given Behaviour to this Behavioural
	 */
	def add(b:Behaviour) = this += b
	
	/**
	 * Removes the given Behaviour to this Behavioural
	 */
	def -=(b:Behaviour) {
		if(behaviours == null) return
		behaviours -= b
	}
	
	/**
	 * Removes the given Behaviour to this Behavioural
	 */
	def remove(b:Behaviour) = this -= b

	
	// -- Constraints ----------------------------------------------------------
	
	protected var constraints:ArrayBuffer[Constraint] = _
	
	/**
	 * Adds the given Constraint to this Behavioural
	 */
	def +=(c:Constraint) {
		if(constraints == null) constraints = new ArrayBuffer[Constraint](6)
		constraints += c
	}

	/**
	 * Adds the given Constraint to this Behavioural
	 */
	def add(c:Constraint) = this += c
	
	/**
	 * Removes the given Constraint to this Behavioural
	 */
	def -=(c:Constraint) {
		if(constraints == null) return
		constraints -= c
	}
	
	/**
	 * Removes the given Constraint to this Behavioural
	 */
	def remove(c:Constraint) = this -= c
	
	// -- Sets -----------------------------------------------------------------
	
	/**
	 * Adds the given BehaviourSet to this Behavioural
	 */
	def +=(s:BehaviourSet) {
		if(s.constraints == null) return
		if(constraints == null) constraints = new ArrayBuffer[Constraint](6)
		s.constraints foreach { constraints += _ }
	}
	
	def add(s:BehaviourSet) = this += s
	
	def -=(s:BehaviourSet) {
		if(s.constraints == null) return
		if(constraints == null) return
		s.constraints foreach { constraints -= _ }
	}
	
	def remove(s:BehaviourSet) = this -= s
}

/**
 * Groups a set of behaviours and constraints to allow switching between
 * complex physics states
 */
class BehaviourSet extends Behavioural {
	
	/**
	 * Adds all behaviours & constraints of this set to the given target
	 */ 
	def apply(target:Behavioural) {
		if(behaviours != null)
			behaviours foreach { target += _ }
		
		if(constraints != null)
			constraints foreach { target += _ }
	}
	
	/**
	 * Removes all behaviours & constraints of this set from the given target
	 */
	def unapply(target:Behavioural) {
		if(behaviours != null)
			behaviours foreach { target -= _ }
		
		if(constraints != null)
			constraints foreach { target -= _ }
	}
	
	def clear {
		if(behaviours != null)
			behaviours.clear
			
		if(constraints != null)
			constraints.clear
	}
}