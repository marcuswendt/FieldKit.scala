/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 08, 2010 */
package field.kit.physics.behaviours

import field.kit.physics._
import field.kit.math.geometry._

/**
 * Keeps a particle inside the given AABB volume by wrapping it around its edges 
 * without loosing velocity.
 * @author marcus
 */
class BoxWrap extends AABB with Behaviour {
	
	var isBounding = false
	
	/** whether the particles velocity should be cleared when it was wrapped */
	var clearVelocity = true
	
	def this(box:AABB) {
		this()
		this := box
	}

	def apply(p:Particle) {
		var wrapped = false
		
		if(p.x < min.x) {
			p.prev.x += max.x - p.x
			p.x = max.x
			wrapped = true
			
		} else if(p.x > max.x) {
			p.prev.x += min.x - p.x
			p.x = min.x
			wrapped = true
		}
	
	    if(p.y < min.y) {
	    	p.prev.y += max.y - p.y
	    	p.y = max.y
	    	wrapped = true
	    	
	    } else if(p.y > max.y) {
	    	p.prev.y += min.y - p.y
	    	p.y = min.y
	    	wrapped = true
	    }
	
	    if(p.z < min.z) {
	    	p.prev.z += max.z - p.z
	    	p.z = max.z
	    	wrapped = true
	    	
	    } else if(p.z > max.z) {
	    	p.prev.z += min.z - p.z
	    	p.z = min.z
	    	wrapped = true
	    }
	    
	    if(wrapped) {
	    	if(clearVelocity)
				p.clearVelocity
	    }
	}
}