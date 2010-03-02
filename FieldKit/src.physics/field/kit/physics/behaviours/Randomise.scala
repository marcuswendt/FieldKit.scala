/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 01, 2010 */
package field.kit.physics.behaviours

import field.kit._
import field.kit.physics._
import field.kit.math.geometry._

/**
 * Sets the particle to a random position within a sphere; Thought to be used as
 * Emitter behaviour and therefore clears the particles velocity 
 */
class RandomiseWithinRadius(val radius:Float) extends Behaviour {
	
	def apply(p:Particle) {
		p.x += random(-radius, radius)
		p.y += random(-radius, radius)
		p.z += random(-radius, radius)
		p.clearVelocity
	}
	
	type T = RandomiseWithinRadius
	def copy = new RandomiseWithinRadius(radius)
}

/**
 * Pushes the particle into a random direction
 */
class RandomSphericalForce(val weight:Float) extends Behaviour {
	
	protected val tmp = new Vec3
	
	def apply(p:Particle) {
		p.force += tmp.randomiseTo(weight)
	}
	
	type T = RandomSphericalForce
	def copy = new RandomSphericalForce(weight)
} 