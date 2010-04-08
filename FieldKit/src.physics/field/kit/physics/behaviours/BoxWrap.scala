/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 08, 2010 */
package field.kit.physics.behaviours

import field.kit.physics._
import field.kit.math.geometry._

/**
 * Keeps a particle inside the given AABB volume by wrapping it around its edges 
 * without loosing velocity.
 */
class BoxWrap extends AABB with Behaviour {
	
	var isBounding = false
	
	def this(box:AABB) {
		this()
		this := box
	}

	def apply(p:Particle) {		
		if(p.x < min.x) {
			p.prev.x += max.x - p.x
			p.x = max.x
		} else if(p.x > max.x) {
			p.prev.x += min.x - p.x
			p.x = min.x
		}
	
	    if(p.y < min.y) {
	    	p.prev.y += max.y - p.y
	    	p.y = max.y
	    } else if(p.y > max.y) {
	    	p.prev.y += min.y - p.y
	    	p.y = min.y
	    }
	
	    if(p.z < min.z) {
	    	p.prev.z += max.z - p.z
	    	p.z = max.z
	    } else if(p.z > max.z) {
	    	p.prev.z += min.z - p.z
	    	p.z = min.z
	    }
	}
}