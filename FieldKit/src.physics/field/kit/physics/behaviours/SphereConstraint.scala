package field.kit.physics.behaviours

import field.kit.physics._
import field.kit.math.geometry._

/**
 * Constrains the particle to the inside or outside of a sphere
 */
class SphereConstraint(var sphere:Sphere, var isBoundingSphere:Boolean) extends Behaviour {
	
	def apply(p:Particle) {
		val isInside = sphere.contains(p)
		if((isBoundingSphere && !isInside) || 
		  (!isBoundingSphere && isInside)) {
			p := (p -= sphere).normalise *= sphere.radius += sphere	
		}
	}
	
	type T = SphereConstraint
	def copy = new SphereConstraint(sphere, isBoundingSphere)
}
