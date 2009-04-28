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
  
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   */
  def main(args:Array[String]):Unit = { 
    info("main")
  }
  
  /** override init to allow specialized initialisation orders */
  def init(width:Int, height:Int):Unit = init(width, height, null)
  
  def init(width:Int, height:Int, initializer: => Unit) {
    import java.awt._
    import java.awt.event._
    
    info("initialising "+ title +" (width: "+ width +" height: "+ height +")")
    
    this.width = width
    this.height = height
    this.hwidth = width / 2f
	this.hheight = height / 2f
    
    defaultSize = false
    val titleHeight = 23
    
    frame = new Frame(title)
    frame.addWindowListener(new WindowAdapter {
    	override def windowClosing(e:WindowEvent) = System.exit(0)
    })
    frame.setBackground(Color.BLACK)
    frame.setResizable(false)
    frame.setSize(width, height + titleHeight)
    frame.setLayout(null)
    frame.add(this)
    
    setSize(width, height + titleHeight)
    setPreferredSize(new Dimension(width, height + titleHeight))
    setupFrameResizeListener
    this.sketchPath = "."
    this.args = args
    
    super.init // go!
    
    initializer
    
    frame.setVisible(true)
    requestFocus
  }
  
  /** always use the OpenGL renderer */
  override def getSketchRenderer = classOf[FGraphicsOpenGL].getCanonicalName
  
  override def draw = render
  
  def render

  final override def size(w:Int, h:Int) = fatal("Dont use size in FieldKit/p5. Use init(...) instead")
  
  // --------------------------------------------------------------------
  /* helpers
   * in PApplet these methods are defined as static final meaning in Scala we have to use PApplet.min(...)
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