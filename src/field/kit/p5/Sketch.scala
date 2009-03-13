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
 * base class for all Processing based Scala sketches
 */
abstract class Sketch extends PApplet with Logger {
  import processing.core.PConstants
  
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   * use -Djava.library.path=lib/jogl/ to use opengl
   */
  override def size(width:Int, height:Int) {
    this.width = width
    this.height = height
    defaultSize = false
  }

  def main(args:Array[String]) = {
    import java.awt._

    val titleHeight = 23

    info("initialising "+ title +" (width: "+ width +" height: "+ height +")")
    
    frame = new Frame(title)
    frame.setBackground(Color.BLACK)
    frame.setResizable(false)
    frame.setSize(width, height + titleHeight)
    frame.setLayout(null)
    frame.add(this)
    
    setSize(width, height + titleHeight)
    setPreferredSize(new Dimension(width, height + titleHeight))
    setupFrameResizeListener
    init // go!
    
    frame.setVisible(true)
    requestFocus
  }
  
  override def start = {
    info("starting...")
    super.start
  }
  
  /** always use the OpenGL renderer */
  override def getSketchRenderer = PConstants.OPENGL
  
  def title = logName
  // --------------------------------------------------------------------
}
