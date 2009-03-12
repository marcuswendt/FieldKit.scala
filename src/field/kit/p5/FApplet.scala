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

abstract class FApplet extends PApplet with Logger {
  import processing.core.PConstants
  
  /**
   * custom initialisation, preventing papplets bad double init behaviour
   */
  val OPENGL = PConstants.OPENGL
  private var initWidth = 0
  private var initHeight = 0
  private var initRenderer = ""

  override def size(width:Int, height:Int, renderer:String) {
    this.initWidth = width
    this.initHeight = height
    this.initRenderer = renderer
  }

  def main(args:Array[String]) = {
    import java.awt._
    
    info("main")
    //PApplet.main(Array(this.getClass.getCanonicalName))
    
    val title = logName
    val titleHeight = 23
    
    frame = new Frame(title)
    frame.setBackground(Color.BLACK)
    frame.setResizable(false)
    frame.setSize(initWidth, initHeight + titleHeight)
    frame.setLayout(null)
    frame.add(this)
    this.init()
    this.setSize(initWidth, initHeight + titleHeight)
    this.setupFrameResizeListener
    frame.setVisible(true)
    this.requestFocus
  }
  
  // --------------------------------------------------------------------
}
