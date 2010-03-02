package field.kit.physics.behaviours

import field.kit.physics._
import field.kit.math.geometry._

/**
 * Constrains the particle to the inside or outside of a circle
 */
class CircleConstraint(circle:Circle, isBoundingCircle:Boolean) extends Behaviour {
	
	def apply(p:Particle) {
		val isInside = circle.contains(p)
		if((isBoundingCircle && !isInside) || 
		  (!isBoundingCircle && isInside)) {
			p := (p -= circle).normalise *= circle.radius += circle	
		}
			
	}
	
	type T = CircleConstraint
	def copy = new CircleConstraint(circle, isBoundingCircle)
}

/**
 * Constrains a particle to the outside of a circle 
 * (the particle will be offset by its radius)
 */
class CircleParticleSizeConstraint(circle:Circle) extends Behaviour {
	
	protected val pcircle = new Circle
	
	def apply(p:Particle) {
		pcircle := p
		pcircle.radius = p.size
		
		if(circle.intersects(pcircle)) {
			val v = (p -= circle)
			v.normalise 
			v *= (circle.radius + pcircle.radius)
			v += circle
			p := v
		}	
	}
	
	type T = CircleParticleSizeConstraint
	def copy = new CircleParticleSizeConstraint(circle)
}