package field.kit.test.p5rework


// library
import processing.core.PApplet
class ProxiedApplet extends PApplet {}

class DrawProxy(applet:ProxiedApplet) {}


// per sketch
object SPDERunner {
  def main(args: Array[String]) { PApplet.main(Array(classOf[SPDESketch].getName)) }
}

class SPDESketch extends ProxiedApplet {
	lazy val px = new DrawProxy(this) {
		// shares same namespace as papplet
	}
}