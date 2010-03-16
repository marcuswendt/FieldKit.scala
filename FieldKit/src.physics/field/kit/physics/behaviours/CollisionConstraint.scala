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
 * Prevents the particles from colliding with each other
 */
class CollisionConstraint extends Constraint {
	
	protected val tmp = new Vec3
	
	def apply(p:Particle) {
		if(p.neighbours == null) return
		
		var i = 0
		while(i < p.neighbours.size) {
			val n = p.neighbours(i)
						
			if(n != p) {
				tmp := n -= p
				val distSq = tmp.lengthSquared
				
				val radius = (p.size + n.size) * 0.51f
				val radiusSq = radius * radius
				
				if(distSq < radiusSq) {
					val dist = sqrt(distSq)
					
					// exponential repel
					//tmp *= (dist - radius)/ dist * 0.5f
					
					// linear repel
					tmp *= (dist - radius)/ radius * 0.5f
					p += tmp
					n -= tmp
				}
			}
			
			i += 1
		}
	}
}