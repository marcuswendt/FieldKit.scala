/*                                                                            *\
 **           _____  __  _____  __     ____                                    **
 **          / ___/ / / /____/ / /    /    \    FieldKit                       **
 **         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
 **        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 12, 2009 */
package field.kit.p5

import field.kit._

import processing.core.PApplet
import processing.core.PConstants._

/**
 * Companion to <code>class Sketch</code> provides helpers to manage (e.g. launch) Sketches
 */
object Sketch {

	/**
	 * Launches a Sketch by setting up a window and starting the Applet lifecycle 
	 */
	def launch(applet:Sketch, args:Array[String]) = {
		import java.awt._
		import java.awt.event._
		import processing.core.PConstants._
		import processing.core.PApplet._

		var present = false
		var exclusive = false
		var external = false

		var displayId = -1
		var location:Array[Int] = null

		args foreach { arg =>

		// parse argument
		val equals = arg.indexOf('=')
			if(equals != -1) {
				val param = arg.substring(0, equals)
				val value = arg.substring(equals + 1)
				
				param match {
					case ARGS_LOCATION => 
						location = parseInt(split(value, ','))
						
					case ARGS_DISPLAY =>
						displayId = parseInt(value)
						
					case _ => System.err.println("Invalid argument", param, "value", value)
				}
	
			} else {
	
				// set parameter
				arg match {
					case ARGS_PRESENT => present = true
					case ARGS_EXCLUSIVE => exclusive = true
					case ARGS_EXTERNAL => external = true
					case _ => System.err.println("Invalid argument:", arg)
				}
			}
		}

		val environment = GraphicsEnvironment.getLocalGraphicsEnvironment
		val devices = environment.getScreenDevices
		
		if(displayId >= devices.length) displayId = -1
			
		val display = if(displayId == -1) environment.getDefaultScreenDevice else devices(displayId)  
		
		// figure out display position on the virtual desktop
		var offsetX = 0
		var offsetY = 0
		
		for(i <- 0 until displayId)
			offsetX += devices(i).getDisplayMode.getWidth

					
		val frame = new Frame(display.getDefaultConfiguration)
		frame.setTitle(applet.title)
		frame.setResizable(false)

		var fullScreenRect:Rectangle = null

		// presentation mode
		if(present) {
			frame.setUndecorated(true)
			frame.setBackground(Color.BLACK)

			if(exclusive) {
				display.setFullScreenWindow(frame)	
				fullScreenRect = frame.getBounds
				
			} else {				
				val mode = display.getDisplayMode
				fullScreenRect = new Rectangle(offsetX, offsetY, mode.getWidth(), mode.getHeight())
				frame.setBounds(fullScreenRect)
				frame.setVisible(true)
			}
		}

		frame.setLayout(null)
		frame.add(applet)

		if(present) frame.invalidate else frame.pack

		// these are needed before init/start
		applet.frame = frame;
		//    applet.sketchPath = folder;
		applet.args = args
		//applet.external = external

		applet.screenWidth = display.getDisplayMode.getWidth
		applet.screenHeight = display.getDisplayMode.getHeight
		
		// Initialize sketch
		applet.init

		// Wait until the applet has figured out its width
		while (applet.defaultSize && !applet.finished) {
			Thread.sleep(5)
		}

		if (present) {
			frame.setBounds(fullScreenRect)
			applet.setBounds((fullScreenRect.width - applet.width) / 2,
					(fullScreenRect.height - applet.height) / 2,
					applet.width, applet.height)
		} else {
			
			// Set frame size
			val insets = frame.getInsets()
			
			val windowW = max(applet.width, MIN_WINDOW_WIDTH) +
			insets.left + insets.right
			val windowH = max(applet.height, MIN_WINDOW_HEIGHT) +
			insets.top + insets.bottom

			frame.setSize(windowW, windowH)

			// Set applet size
			val usableWindowH = windowH - insets.top - insets.bottom;
			applet.setBounds((windowW - applet.width)/2,
					insets.top + (usableWindowH - applet.height)/2,
					applet.width, applet.height);

			if (location != null) {
				frame.setLocation(location(0), location(1))

			} else {
				frame.setLocation(
						offsetX + (applet.screenWidth - applet.width) / 2,
						offsetY + (applet.screenHeight - applet.height) / 2)
			}
		}

		frame.addWindowListener(new WindowAdapter {
			override def windowClosing(e:WindowEvent) = System.exit(0)
		}) 

		// handle frame resizing events
		applet.setupFrameResizeListener();

		// all set for rockin
		if (applet.displayable()) {
			frame.setVisible(true);
		}
	}
}


/**
* The FieldKit version of a Processing.org PApplet sketch
* @author Marcus Wendt
*/
abstract class Sketch extends PApplet with Logger {
	import processing.core.PConstants._
	
	def main(args:Array[String]):Unit = Sketch.launch(this, args)

	def title = logName

	// -- OpenGL Tweaks --------------------------------------------------------
	import processing.opengl.PGraphicsOpenGL

	def pgl = g.asInstanceOf[PGraphicsOpenGL]
	def gl = pgl.gl
	def beginGL = pgl.beginGL
	def endGL = pgl.endGL

	// -- Screen Recorder ------------------------------------------------------
	import field.kit.p5.Recorder
	protected var rec:Recorder = _
	
	def beginRecord {
		if(rec != null) rec.pre
	}
	
	override def endRecord {
		super.endRecord
		if(rec != null) rec.post
	}
	
	protected def initRecorder {
		rec = new Recorder(this)
	}
	
	override def keyPressed {
		key match {
			case 'r' =>
				if(rec == null) initRecorder
				rec.screenshot
				
			case 'R' =>
				if(rec == null) initRecorder
				rec.sequence
				
			case _ =>
		}
	}
}
