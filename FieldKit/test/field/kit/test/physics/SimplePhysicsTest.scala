/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit.test.physics

import field.kit._
import field.kit.p5._
import field.kit.physics._

/**
 * Shows how to create a simple animation using just a single physics Particle
 * and a few Behaviours
 */
object SimplePhysicsTest extends Sketch {
	import processing.core.PConstants._
	
	val startHeight = 100f
	def floorHeight = height - 100f
	
	val p = new Particle {
		def reset {
			this.reset(width/2f, startHeight, 0f)
			this.extent = 25f
		}
		def size = this.bounds.extent.x * 2
	}
	
	// Gravity 
	p += behaviour { p:Particle =>
		p.steer.y += 0.1f
	}
	
	// Floor Bounce
	p += behaviour { p:Particle =>
		if(p.bounds.max.y > floorHeight) {
			p.y = floorHeight - p.bounds.extent.y
			p.steer *= -0.5
		}
	}
	
	val timer = new Timer
	
	override def setup {
		size(1024, 768, OPENGL)
		p.reset
		timer.reset
	}
	
	override def draw {
		
		p.update(timer.update)
		
		background(0)
		
		// draw floor
		stroke(128)
		line(0, startHeight, width, startHeight)
		line(0, floorHeight, width, floorHeight)
		
		// draw ball
		noStroke
		fill(255)
		ellipse(p.x, p.y, p.size, p.size)
	}
	
	override def keyPressed {
		key match {
			case ' ' => p.reset
			case _ =>
		}
	}
}
