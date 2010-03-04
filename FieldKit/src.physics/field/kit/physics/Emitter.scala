/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 24, 2010 */
package field.kit.physics

import field.kit._
import scala.reflect.Manifest

/**
 * An emitter is basically a particle that emits/ creates other particles at its 
 * current position.
 */
class Emitter[T <: Particle](val physics:Physics[T])(implicit m:Manifest[T]) 
extends Vec3 with Behavioural {

	var rate = 1
	var interval = 1000f
	var max = 100

	protected var time = 0f
	
	def update(dt:Float) {
		time += dt
    
		if(time < interval) return
		
		time = 0
		
		// emit particles
		var j = 0
		while(j < rate && physics.size < max) {
			emit
			j += 1
		}
	}
	
	/** emits a single particle and applies the emitter behaviours */
	protected def emit:T = {
		// create particle
		val p = create
		
		// set particle to start at the emitters position
		p.init(this) 
    
		// add particle to physics
		physics += p
    
		// apply behaviours
		applyBehaviours(p)
		applyConstraints(p)
		
		p
	}
	
	/** creates a new particle object from the parameterized type */
	protected def create:T = {
		val clazz = Class.forName(m.toString)
		val p = clazz.newInstance.asInstanceOf[T]
		p
	}
	
	protected def applyBehaviours(p:Particle) {
		if(behaviours == null) return
		
		var i = 0
		while(i < behaviours.size) {
			behaviours(i).apply(p)
			i += 1
		}	
	}
	
	protected def applyConstraints(p:Particle) {
		if(constraints == null) return
		
		var i = 0
		while(i < constraints.size) {
			constraints(i).apply(p)
			i += 1
		}
	}
}
