/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 01, 2010 */
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
			p := (p -= circle).normalize *= circle.radius += circle	
		}
			
	}
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
			v.normalize 
			v *= (circle.radius + pcircle.radius)
			v += circle
			p := v
		}	
	}
}