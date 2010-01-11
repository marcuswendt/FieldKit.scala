package field.kit.test.p5rework

import processing.core.PApplet
import processing.core.PConstants._

object OldSketchTestRunner {
	def main(args: Array[String]) {
		PApplet.main(Array(classOf[OldSketchTest].getName)) 
	}
}

class OldSketchTest extends PApplet {
	
	override def setup() {
		size(1024, 768, OPENGL)
	}

	override def draw() {
		background(255,0,0)
	}
}
