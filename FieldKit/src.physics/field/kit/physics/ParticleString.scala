/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 02, 2010 */
package field.kit.physics

import field.kit._

/**
 * Utility class to create & manage a series of particles connected via springs 
 * into one continous string. 
 */
class ParticleString[T <: Particle](var physics:Physics[T]) {
	import scala.collection.mutable.ArrayBuffer
	
	// particles
	var particles = new ArrayBuffer[T]
	
	def head = particles(0)
	def tail = particles(particles.size - 1)
	def size = particles.size
	
	// springs
	var springs = new ArrayBuffer[Spring]
	
	protected var _strength = 1f
	def strength = _strength
	def strength_=(s:Float) = {
		_strength = s
		springs foreach { _.strength = s }
	}
	
	/**
	 * Creates a number of springs from a certain Vec3 to another Vec3 position
	 */
	def create(start:Vec3, end:Vec3, steps:Int, mass:Float, strength:Float) {
		val step = (end - start) / (steps -1)
		create(steps, start, step, mass, strength)
	}
	
	
	def create(num:Int, start:Vec3, step:Vec3, mass:Float, strength:Float) {
		
		val pos = Vec3(start)
		val len = step.length
		
		var prev:T = null.asInstanceOf[T]
		
		for(i <- 0 until num) {
			// create particle and add it to string/ physics
			val p = physics.emitter.emit(pos)
			
			particles += p
			physics += p
			
			// create spring
			if(prev != null) { 
				val s = createSpring(prev, p, len, strength)
				springs += s
				physics += s
			}
			
			prev = p
			pos += step
		}
	}
	
	/** Creates a new spring connecting two particles a, b */
	protected def createSpring(a:T, b:T, len:Float, strength:Float) = 
		new Spring(a, b, len, strength)
	
	def clear {
		particles foreach { physics -= _ }
		springs foreach { physics -= _ }
		
		particles.clear
		springs.clear
	}
}