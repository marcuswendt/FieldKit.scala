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
  import kit.vision._
  import processing.core._
  import processing.core.PConstants._
  import javax.media.opengl._
  
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
  
  var index = 0
  //Vision.setCamera(Vision.CAMERA_OPENCV)
  //Vision.setSize(640, 480)
  Vision.start
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    // update
    Vision.update
    
    // render
    background(0)
    
    // draw stage
    {
      val s = Vision.stage(index)
      val format = if(s.depth == 8) GL.GL_LUMINANCE else GL.GL_BGR
      gl.glPixelZoom(width / s.width.toFloat, height / s.height.toFloat)
      gl.glDrawPixels(s.width, s.height, format, GL.GL_UNSIGNED_BYTE, s.image)
    }
    
    // draw blobs
    {
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
  
  override def keyPressed {
    keyCode match {
      case LEFT =>
        index -= 1
        if(index < 0) index = Vision.Stages.size - 1
      case RIGHT => 
        index += 1
        if(index == Vision.Stages.size) index = 0
      case UP => index = 0
      case _ =>
    }
  }
}