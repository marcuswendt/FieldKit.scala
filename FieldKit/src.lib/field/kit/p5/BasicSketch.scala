/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.p5

import processing.core.PApplet

/**
 * base class for all Scala-Processing based applets
 * @author Marcus Wendt
 */
abstract class BasicSketch extends PApplet with Logger {
  import processing.core.PConstants
  import processing.opengl.PGraphicsOpenGL
  import field.kit.p5.FGraphicsOpenGL
  
  /** set to true when the sketch currently spans the full screen */
  var isFullscreen = false
  
  var isInitialized = false
  
  /** the function used when initializing/ reinitializing the sketch */
  private var initializer:Unit = null
    
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   */
  def main(args:Array[String]):Unit = { 
    info("main")
  }
  
  /** alternate initializer */
  def init(width:Int, height:Int):Unit = init(width, height, false, null)
  
  /** alternate initializer */
  def init(width:Int, height:Int, initializer: => Unit):Unit = init(width, height, false, initializer)
  
  /** alternate initializer */  
  def init(width:Int, height:Int, fullscreen:Boolean):Unit = init(width, height, fullscreen, null)
  
  /** 
   * initialize the frame and sets the sketch up, you can pass in a custom initializer
   * function that will get executed after everything is set and before the window is displayed 
   */
  def init(width:Int, height:Int, fullscreen:Boolean, initializer: => Unit) {
    info("initialising "+ title +" (width: "+ width +" height: "+ height +" fullscreen:"+ fullscreen +")")
    
    // check if we need to de-initialize first
    if(isInitialized) deinit

    // initialize
    import java.awt._
    import java.awt.event._

    this.width = width
    this.height = height
    this.hwidth = width / 2f
	this.hheight = height / 2f
	this.isFullscreen = fullscreen
    this.initializer = initializer
    
    defaultSize = false
    val titleHeight = 23
    
    // initialize frame
    val sketchDim = new Rectangle(0, 0, width, height)
    
    val environment = GraphicsEnvironment.getLocalGraphicsEnvironment
	val displayDevice = environment.getDefaultScreenDevice
	val mode = displayDevice.getDisplayMode
	val screenDim = new Rectangle(0, 0, mode.getWidth, mode.getHeight)
 
    frame = new Frame(displayDevice.getDefaultConfiguration)
    frame.setTitle(title)
    frame.addWindowListener(new WindowAdapter {
    	override def windowClosing(e:WindowEvent) = System.exit(0)
    })
    frame.setBackground(Color.BLACK)
    //frame.setResizable(false)

    frame.setLayout(null)
    frame.add(this)
    
    if(fullscreen) {
      frame.setUndecorated(true)
      displayDevice.setFullScreenWindow(frame)
      frame.setBounds( (screenDim.width - sketchDim.width) / 2,
    		  		   (screenDim.height - sketchDim.height) / 2,
    		  		    sketchDim.width, 
    		  		    sketchDim.height)
      
      this.setSize(sketchDim.width, sketchDim.height)
      
    } else {
      frame.pack
      val insets = frame.getInsets
      this.setBounds(insets.left + insets.right,
                     insets.top + insets.bottom,
                     sketchDim.width,
                     sketchDim.height)
      
      frame.setBounds( (screenDim.width - sketchDim.width) / 2,
    		  		   (screenDim.height - sketchDim.height) / 2,
    		  		    sketchDim.width + insets.left + insets.right, 
    		  		    sketchDim.height + insets.top + insets.bottom)
    }
    
    // setup sketch
    setupFrameResizeListener
    this.sketchPath = "."
    this.args = args
    
    // go!
    isInitialized = true
    super.init 
    initializer
    
    frame.setVisible(true)
    requestFocus
  }

  /** 
   * called when the sketch is getting initialized but was already displayed before
   * derived sketches should override this to make sure they restart cleanly 
   */
  protected def deinit {
    fine("de-initializing")
    isInitialized = false
    stop
    frame.dispose
  }
  
  def toggleFullscreen = init(width, height, !isFullscreen, initializer)
  
  // -- Renderer ---------------------------------------------------------------  
  /** always use the OpenGL renderer */
  override def getSketchRenderer = classOf[FGraphicsOpenGL].getCanonicalName
  
  override def draw = render
  
  def render

  final override def size(w:Int, h:Int) = fatal("Dont use size in FieldKit/p5. Use init(...) instead")
  
  // -- Helpers ----------------------------------------------------------------
  /* in PApplet these methods are defined as static final meaning in Scala we have to use PApplet.min(...)
   * so we redefine them here again to keep the code concise
   */
  val SCREEN = PConstants.SCREEN
  val CENTER = PConstants.CENTER
  
  // --------------------------------------------------------------------
  /* extras */
  def title = logName
  def pgl = g.asInstanceOf[PGraphicsOpenGL]
  
  def beginGL = pgl.beginGL
  def endGL = pgl.endGL
  
  var hwidth = 0f
  var hheight = 0f
}