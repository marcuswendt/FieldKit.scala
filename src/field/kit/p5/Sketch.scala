/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 12, 2009 */
package field.kit.p5

import processing.core.PApplet
import field.kit.Logger

/**
 * contains global constants 
 */
object Sketch extends PApplet {
  val ONE = 1
  val TWO = "the two"
}

/**
 * base class for all Processing based Scala sketches
 */
abstract class Sketch extends PApplet with Logger {
  import processing.core.PConstants
  
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   */
  def main(args:Array[String]) = init
  
  /** override init to allow specialized initialisation orders */
  override def init {
    import java.awt._
    import java.awt.event._
    
    val titleHeight = 23

    info("initialising "+ title +" (width: "+ width +" height: "+ height +")")
    
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
    
    frame.setVisible(true)
    requestFocus
  } 
  
  override def start {
    info("starting...")
    super.start
  }

  override def size(width:Int, height:Int) {
    this.width = width
    this.height = height
    defaultSize = false
  }
  
  /** always use the OpenGL renderer */
  override def getSketchRenderer = PConstants.OPENGL
  
  override def draw = render
  
  def render
  
  // --------------------------------------------------------------------
  /* helpers
   * in PApplet these methods are defined as static final meaning in Scala we have to use PApplet.min(...)
   * so we redefine them here again to keep the code concise
   */
   /*
  seems to fail validation at runtime =/

  def abs(n:Int) = if(n < 0) -n else n
  def abs(n:Float) = if(n < 0) -n else n

  def sq(n:Float) = n*n
  def sqrt(a:Float) = Math.sqrt(a).asInstanceOf[Float]
  def log(a:Float) = Math.log(a).asInstanceOf[Float]
  def exp(a:Float) = Math.exp(a).asInstanceOf[Float]
  def pow(a:Float, b:Float) = Math.pow(a,b).asInstanceOf[Float]
  
  def max(a:Int, b:Int) = if(a > b) a else b
  def max(a:Float, b:Float) = if(a > b) a else b
  def min(a:Int, b:Int) = if(a > b) b else a
  def min(a:Float, b:Float) = if(a > b) b else a
  */
    
  // --------------------------------------------------------------------
  /* extras */
  def title = logName
}
