package field.kit.test

import field.kit.Sketch

object SketchTest extends Sketch {
	import processing.core.PConstants._
	
	override def setup {
		size(1280, 720, OPENGL)
	}
	
	override def draw {
		background(0)
	}
}
