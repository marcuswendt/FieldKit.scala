package field.kit.p5

object launch {
	def apply(sketch:Sketch, args:Array[String]) = {
		
		println("sketch launcher")
		
		import java.awt._
		
		val frame = new Frame()
		val applet = sketch
		frame.add(applet)
		applet.init
		frame.pack
		frame.setVisible(true)
		
		sketch
	}
}
