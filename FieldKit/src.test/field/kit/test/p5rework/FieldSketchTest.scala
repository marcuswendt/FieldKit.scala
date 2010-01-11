package field.kit.test.p5rework

/*
 
	Preferred way of writing a Sketch - very close to P5

import field.kit.Sketch

object FieldSketchTest extends Sketch {

	// implicitly launch using def main(...)

	def setup {
		size(1024,768)
	}
	
	def draw {
		...
	}
}

*/

abstract class BaseSketch {
	def setup
	def draw
}

object HelloP5 extends BaseSketch {
	def setup {}
	def draw {}
}