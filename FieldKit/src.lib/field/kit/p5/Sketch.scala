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
import processing.core.PAppletProxy

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
			}

		} else {

			// set parameter
			arg match {
			case ARGS_PRESENT => present = true
			case ARGS_EXCLUSIVE => exclusive = true
			case ARGS_EXTERNAL => external = true		
			}
		}
		}

		val environment = GraphicsEnvironment.getLocalGraphicsEnvironment
		val displayDevice = environment.getDefaultScreenDevice

		val frame = new Frame(displayDevice.getDefaultConfiguration)
		frame.setTitle(applet.title)
		frame.setResizable(false)

		var fullScreenRect:Rectangle = null

		// presentation mode
		if(present) {
			frame.setUndecorated(true)
			frame.setBackground(Color.BLACK)

			if(exclusive) {
				displayDevice.setFullScreenWindow(frame)	
				fullScreenRect = frame.getBounds
			} else {
				val mode = displayDevice.getDisplayMode
				fullScreenRect = new Rectangle(0, 0, mode.getWidth(), mode.getHeight())
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
		//    applet.external = external;
		//applet.external = external

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
			
			val windowW = Math.max(applet.width, MIN_WINDOW_WIDTH) +
			insets.left + insets.right
			val windowH = Math.max(applet.height, MIN_WINDOW_HEIGHT) +
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
				frame.setLocation((applet.screen.width - applet.width) / 2,
						(applet.screen.height - applet.height) / 2)
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
abstract class Sketch extends PAppletProxy with Logger {

	def main(args:Array[String]):Unit = Sketch.launch(this, args)

	def title = logName

	// -- OpenGL Tweaks --------------------------------------------------------
	import processing.opengl.PGraphicsOpenGL
  
	def pgl = g.asInstanceOf[PGraphicsOpenGL]
	def gl = pgl.gl
	def beginGL = pgl.beginGL
	def endGL = pgl.endGL
	
	// Use pre / post methods for recorder
	// registerPre , registerPost
	
	//  
	//  // -- Menubar ---------------------------------------------------------------- 
	//  import java.awt.MenuBar
	//  import java.awt.Menu
	//  protected var menuBar:MenuBar = null
	//  
	//  /** Called from init to create a system menubar */
	//  protected def initMenuBar = {
	//    menuBar = new MenuBar
	//    
	//    // file menu
	//    val file = new Menu("File")
	//    initFileMenu(file)
	//    menuBar add file
	//    
	//    menuBar
	//  }
	//  
	//  protected def initFileMenu(file:Menu) {
	//    import field.kit.util.SwingUtil
	//    import java.awt.event.KeyEvent
	//    
	//    file add SwingUtil.menuItem("Record Screenshot", KeyEvent.VK_R, false, recordScreenshot)
	//    file add SwingUtil.menuItem("Record Sequence", KeyEvent.VK_R, true, recordSequence)
	//    file add SwingUtil.menuItem("Recording Settings", {
	//      import javax.swing.JOptionPane
	//      
	//      val options = new Array[Object](10)
	//      for(i <- 0 until options.length)
	//        options(i) = (i+1)*width +" x "+ (i+1)*height +" px"
	//      
	//      val input = JOptionPane.showInputDialog(frame, 
	//                                              "Select the output dimension:",
	//                                              "Recording Settings",
	//                                              JOptionPane.PLAIN_MESSAGE,
	//                                              null,
	//                                              options,
	//                                              options(0)).toString
	//      
	//      val outputWidth = input.substring(0, input.indexOf(' ')).toInt
	//      val numTiles = outputWidth / width
	//      rec.init(numTiles * width, numTiles * height)
	//    })
	//    file add SwingUtil.menuItem("Quit", KeyEvent.VK_Q, false, exit)
	//  }
	//  
	//  /*
	//  override def init(width:Int, height:Int, fullscreen:Boolean, aaSamples:Int, initializer: => Unit) {
	//    super.init(width, height, fullscreen, aaSamples, initializer)
	//    
	//    // set default recording dimensions
	//    rec.init(width, height)
	//    
	//    // dont show menubar in fullscreen mode, (deactivates keyboard shortcuts on linux)
	//    if(!fullscreen)
	//      frame.setMenuBar(initMenuBar)
	//  }
	//  */
	//    
	//  // -- Screen Recorder---------------------------------------------------------
	//  import field.kit.p5.Recorder
	//  var rec = new Recorder(this)
	//  rec.name = logName
	//  
	////  /** enables/disabled the vertical sync, prevents tearing */
	////  var vsyncEnabled = false
	////  
	////  override def draw {
	////    if(vsyncEnabled) gl.setSwapInterval(1)
	////    
	////    rec.pre
	////    render
	////    rec.post
	////  }
	//  
	//  protected def recordScreenshot = rec.screenshot
	//  
	//  protected def recordSequence = rec.sequence
}
