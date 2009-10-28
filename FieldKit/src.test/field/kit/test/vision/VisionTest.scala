/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 28, 2009 */
package field.kit.test.vision

/**
 * VM Args: -Djna.library.path=lib/vision
 * Until OpenCV.framework can be compiled as 64bit also use -d32
 */
object VisionTest extends test.Sketch {
  import kit.vision.Vision
  import processing.core._
  
  Logger.level = Logger.FINE
  
  /*
  //
  // This is NOT thread safe, should use actors!
  //
  val v = new Thread("visionthread") {
    override def run {
      Vision.setCamera(Vision.CAMERA_OPENCV)
      //Vision.setSize(640, 480)
      Vision.start
  
      while(true) {
        Vision.update
      	Thread.sleep((1000/ 30.0).toLong)
      }
    }
  } 
  v.start
  */
  
  Vision.setCamera(Vision.CAMERA_OPENCV)
  //Vision.setSize(640, 480)
  Vision.start
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    // update
    Vision.update
    
    // render
    background(0)
    
    rectMode(PConstants.CENTER)
    
    pushMatrix
    scale(width / 320f, height / 240f, 1f)
    
    Vision.blobs filter (_.active == true) foreach { b =>
      val c = 128 + 128 * (b.id / Vision.blobs.size.toFloat)
      stroke(c)
      noFill
      rect(b.bounds.x1, b.bounds.y1, b.bounds.x2, b.bounds.y2)
      
      fill(c)
      rect(b.x, b.y, 10, 10)
    }
    popMatrix
  }
}