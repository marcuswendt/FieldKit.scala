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
 * extends the BasicSketch with useful features
 * @author Marcus Wendt
 */
abstract class Sketch extends PAppletProxy with Logger {
	import field.kit.p5._
	
	def main(args:Array[String]):Unit = launch(this, args)
	
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
