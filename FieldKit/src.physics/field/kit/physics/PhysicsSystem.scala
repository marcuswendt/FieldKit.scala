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
 * Contains several particles or flocks
 */
class PhysicsSystem extends Behavioural with Logger {
	import field.kit.math.geometry.AABB
	import scala.collection.mutable.ArrayBuffer
	
	/**
	 * Defines the 3D simulation space dimensions
	 */
	var space = new AABB((100f, 100f, 100f))
	
	// -- Elements -------------------------------------------------------------
	/**
	 * Keeps track of all physical elements in the system 
	 */
	val elements = new ArrayBuffer[Behavioural]
	
	/**
	 * Adds a new behavioural element to this system
	 */
	def +=(e:Behavioural) {
		elements += e
		// add behaviours to newly added element
		if(behaviours != null)
			behaviours foreach { e += _ }
	}
	
	def add(e:Behavioural) = this += e
	
	/**
	 * Removes an element from the system
	 */
	def -=(e:Behavioural) {
		elements -= e
		// remove behaviours from dropped element
		if(behaviours != null)
			behaviours foreach { e -= _ }
	}
	
	def remove(e:Behavioural) = this -= e 
	
	// -- Behaviours -----------------------------------------------------------
	override def +=(b:Behaviour) {
		super.+=(b)
		// add new behaviour to all particles
		elements foreach (_ += b)
	}
	
	override def -=(b:Behaviour) {
		super.-=(b)
		// remove behaviour from all particles
		elements foreach (_ -= b)
	}
	
	/**
	 * Updates all elements of the scene
	 */
	def update(dt:Float) {
		var i = 0
		while(i < elements.size) {
			elements(i).update(dt)
			i += 1
		}
	}
}

// TODO support emitters & world behaviours