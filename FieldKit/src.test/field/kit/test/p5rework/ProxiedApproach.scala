package field.kit.test.p5rework.proxy

/*
// library
import processing.core.PApplet

abstract class ProxiedApplet extends PApplet {
	val px:DrawProxy
}

abstract class DrawProxy(sketch:ProxiedApplet) {
	def setup
	def draw
}

// use case
object MySketchRunner extends ProxiedApplet {
	lazy val px = new DrawProxy(this) {
		def setup {
			size(1024, 768)
		}

		def draw {
			background(0)
		}
	}
}
*/

import processing.core.PApplet

abstract class Sketch extends PApplet {
//	override def setup {
//		
//	}
//	
//	override def draw {
//		
//	}
//	
//	def draw 
	
	def main(args: Array[String]):Unit = {
		runSketch(args)
	}
	
	def runSketch(args:Array[String]) {
		import java.awt._
		
		val frame = new Frame()
		val applet = this
		frame.add(applet)
		applet.init
		frame.pack
		frame.setVisible(true)
	}
}

object MySketch extends Sketch {
	override def setup {
		size(1024, 768)
	}
	
	override def draw {
		background(65)
		
		fill(255)
		rect(mouseX, mouseY, 10, 10)
	}
}