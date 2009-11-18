/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.p5

import processing.core.PAppletProxy
import field.kit.Logger

/**
 * base class for all Scala-Processing based applets
 * @author Marcus Wendt
 */
abstract class BasicSketch extends PAppletProxy with Logger {
  import processing.core.PConstants
  import processing.core.PApplet
  import processing.opengl.PGraphicsOpenGL
  import field.kit.p5.FGraphicsOpenGL
  
  import java.awt._
  import java.awt.event._
    
  /** set to true when the sketch currently spans the full screen */
  var isFullscreen = false
  
  /** number of samples used for anti-aliasing the opengl graphics */
  var aaSamples = 0
  
  /** the function used when initializing/ reinitializing the sketch */
  protected var initializer:(() => Unit) = null
  
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   */
  def main(args:Array[String]):Unit = {}
  
//  /** alternate initializer */
//  def init(width:Int, height:Int):Unit = init(width, height, false, null)
//  
//  /** alternate initializer */
//  def init(width:Int, height:Int, initializer: => Unit):Unit = init(width, height, false, initializer)
//  
//  /** alternate initializer */  
//  def init(width:Int, height:Int, fullscreen:Boolean):Unit = init(width, height, fullscreen, null)
  
  /** 
   * initialize the frame and sets the sketch up, you can pass in a custom initializer
   * function that will get executed after everything is set and before the window is displayed 
   */
  def init(width:Int, height:Int, fullscreen:Boolean, aaSamples:Int, initializer: => Unit) {
    info("initialising "+ title +" (width: "+ width +" height: "+ height +" fullscreen:"+ fullscreen +")")

    // initialize
    this.width = width
    this.height = height
    this.hwidth = width / 2f
	this.hheight = height / 2f
	this.isFullscreen = fullscreen
	this.aaSamples = aaSamples
 
	if(this.initializer == null)
		this.initializer = () => initializer
    
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
      override def windowClosing(e:WindowEvent) = exit2
    })
    frame.setBackground(Color.BLACK) 
    frame.setLayout(null)
    frame.add(this)
    frame.setResizable(false)
    
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
                     sketchDim.height )
      
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
    super.init
        
    frame.setVisible(true)
    requestFocus
  }
  
  // -- Reinitialisation -------------------------------------------------------
  private var newWidth = 0
  private var newHeight = 0
  private var newFullscreen = false
  
  def reinit(width:Int, height:Int, fullscreen:Boolean) {
    info("reinit", width, height, fullscreen)
    this.newWidth = width
    this.newHeight = height
    this.newFullscreen = fullscreen
    exit
  }
  
  override def exit2() {
    if(newWidth != 0) {
      // clean up
      setThread(null)
      frame.dispose
      
      // initialize again
      init(newWidth, newHeight, newFullscreen, aaSamples, initializer)
      newWidth = 0
      
    } else {
      super.exit2
    }
  }
  
  def toggleFullscreen = reinit(width, height, !isFullscreen)
  
  // -- Renderer ---------------------------------------------------------------  
  /** always use the OpenGL renderer */
  override def getSketchRenderer = classOf[FGraphicsOpenGL].getCanonicalName
  
  override def handleDraw {
     if (g != null && (looping || redraw)) {
      if (!g.canDraw()) {
        // Don't draw if the renderer is not yet ready.
        // (e.g. OpenGL has to wait for a peer to be on screen)
        return
      }

      g.beginDraw()

      val now = System.nanoTime()

      if (frameCount == 0) {
        import processing.core.PApplet._
        try {
          //println("Calling setup()")
          //setup()
          this.initializer()
          //println("Done with setup()")

        } catch {
          case e:RendererChangeException =>
            // Give up, instead set the new renderer and re-attempt setup()
            return
        }
        this.defaultSize = false

      } else {  // frameCount > 0, meaning an actual draw()
        // update the current frameRate
        val rate = 1000000.0 / ((now - frameRateLastNanos) / 1000000.0)
        val instantaneousRate = (rate / 1000.0f).toFloat
        frameRate = (frameRate * 0.9f) + (instantaneousRate * 0.1f)

        preMethods.handle()

        // use dmouseX/Y as previous mouse pos, since this is the
        // last position the mouse was in during the previous draw.
        pmouseX = dmouseX
        pmouseY = dmouseY

        draw()

        // dmouseX/Y is updated only once per frame (unlike emouseX/Y)
        dmouseX = mouseX
        dmouseY = mouseY

        // these are called *after* loop so that valid
        // drawing commands can be run inside them. it can't
        // be before, since a call to background() would wipe
        // out anything that had been drawn so far.
        dequeueMouseEvents()
        dequeueKeyEvents()

        drawMethods.handle()

        redraw = false  // unset 'redraw' flag in case it was set
        // (only do this once draw() has run, not just setup())

      }

      g.endDraw()

      frameRateLastNanos = now
      frameCount += 1

      // Actively render the screen
      paint()

      postMethods.handle()
    }
  }
  
//  override def run {
//    var waitingForInitializer = true
//    while(waitingForInitializer) {
//      if(g.canDraw) {
//        this.initializer()
//        waitingForInitializer = false
//      }
//      Thread.sleep(10)
//    }
//    
//    super.run
//  }
  
  override def draw = render
  
  def render

  final override def size(w:Int, h:Int) = fatal("Dont use size in FieldKit/p5. Use init(...) instead")
  
  // --------------------------------------------------------------------
  /* extras */
  def title = logName
  final def pgl = g.asInstanceOf[FGraphicsOpenGL]
  final def gl = pgl.gl
  
  final def beginGL = pgl.beginGL
  final def endGL = pgl.endGL
  final def activeCamera = pgl.activeCamera
  
  var hwidth = 0f
  var hheight = 0f
}