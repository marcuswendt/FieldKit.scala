/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 30, 2010 */
package field.kit.physics.behaviours

import field.kit._
import field.kit.physics._
import field.kit.math.geometry._

/**
 * Base class for all Flocking Behaviours
 * @author marcus
 */
abstract class FlockingBehaviour extends Behaviour {
	var weight = 1f
	protected val tmp = new Vec3
}

/**
 * Flocking behaviour: steers particles away from their neighbours
 * @author marcus
 */
class FlockRepel extends FlockingBehaviour {
	
	override def apply(p:Particle) {
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
					
					tmp *= (dist - radius)/ radius * 0.5f
					tmp.normalizeTo(weight)
					
					p.force += tmp
					//n.force -= tmp
				}
			}
			
			i += 1
		}
	}
}