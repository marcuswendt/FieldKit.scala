package field.kit.test.physics

import field.kit._
import field.kit.p5._
import field.kit.physics._

object SimplePhysicsTest extends Sketch {
	import processing.core.PConstants._
	
	val startHeight = 100f
	def floorHeight = height - 100f
	
	val p = new Particle {
		def reset {
			this.reset(width/2f, startHeight, 0f)
			this.extent = 50f
		}
		def size = this.extent.x * 2
	}
	
	// Gravity 
	p += new Behaviour {
		def apply(p:Particle) {
			p.steer.y += 0.1f
		}
	}
	
	// Floor Bounce
	p += new Behaviour {
		def apply(p:Particle) {
			if(p.max.y > floorHeight) {
				p.y = floorHeight - p.extent.y
				p.steer *= -0.5
			}
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
