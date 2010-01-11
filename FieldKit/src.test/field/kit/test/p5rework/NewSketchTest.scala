package field.kit.test.p5rework

import processing.core.PApplet
import processing.core.PConstants._

//object NewSketchTestRunner {
//	def main(args: Array[String]) {
//		PApplet.main(Array(classOf[NewSketchTest].getName)) 
//	}
//}

//object NewSketchRunner extends PAppletRunner(classOf[NewSketchTest]) 
//
//class PAppletRunner(sketchClass:Class[_ <: PApplet]) {
//	def main(args:Array[String]) {
//		PApplet.main(Array(sketchClass.getName))
//	}
//}

object NewSketchRunner 
extends PAppletRunner(classOf[NewSketchTest].getName) {
	println("NewSketchRunner ctor")	
}

class PAppletRunner(sketchClass:String) {
	println("PAppletRunner ctor")
	
	def main(args:Array[String]) {
		println("PAppletRunner main")
		PApplet.main(Array(sketchClass))
	}
}

class NewSketchTest extends PApplet {
	println("NewSketchTest ctor")
	
	override def setup() {
		size(1024, 768, OPENGL)
	}

	override def draw() {

	}
}

object NewSketchRunner2 
extends PAppletRunner(classOf[NewSketchTest].getName)