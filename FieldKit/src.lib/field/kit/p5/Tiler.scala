/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 27, 2009 */
package field.kit.p5

import field.kit.Logger

/**
 * Utility used by <code>Recorder</code> to render a high-res image from a smaller OpenGL view.
 * 
 * This class is still experimental and behaves unexpectedly when used with an 
 * imageWidth and imageHeight that are not the exact same multiples of the renderer window dimensions.
 * 
 */
class Tiler(rec:Recorder) extends Logger {
  import java.nio.ByteBuffer
  import javax.media.opengl.GL
  
  import field.kit.gl.render._
  import field.kit.math.Common._
  import field.kit.math._
  
    
  /** the current tile */
  var index = new Dim2[Int]
  
  /** the total number of tiles (columns & rows) */
  var tiles = new Dim2[Int]

  /** height & width of a single tile */
  var tile = new Dim2[Float]
  
  /** height & width of a the final image */
  var image = new Dim2[Float]
  
  /** the GL data format; interpreted as in glReadPixels */
  var dataFormat = GL.GL_BGR
  
  /** interpreted as in glReadPixels */
  val dataType = GL.GL_UNSIGNED_BYTE
  
  // -- Internal ---------------------------------------------------------------
  protected val sketch = rec.sketch

  // the buffer itself, must be large enough to hold the final image
  protected var buffer:ByteBuffer = null 
  
  /** stores the original */
  protected var originalCamera = sketch.activeCamera.clone

  /** shortcut to the currently active render camera */
  protected def camera = sketch.activeCamera
  
  protected var isFinished = false
  
  
  /**
   * @param width the hi-res image width
   * @param height the hi-res image height
   */
  def init(imageWidth:Int, imageHeight:Int, buffer:ByteBuffer, dataFormat:Int) {
    this.buffer = buffer
    this.dataFormat = dataFormat
    
    image.width = imageWidth
    image.height = imageHeight
    
    info("init", image)
        
    index := (0,0)
    
    // calculate number of tiles in x, y
    tiles.columns = ceil(imageWidth/ sketch.width.toFloat).toInt
    tiles.rows = ceil(imageHeight/ sketch.height.toFloat).toInt
    
    // calculate dimensions of a single tile for the camera
    tile.width = sketch.width / tiles.columns.toFloat
    tile.height = sketch.height / tiles.rows.toFloat
    
    originalCamera := sketch.activeCamera
    
    isFinished = false
  }
  
  /**
   * Sets the camera perspective for the current tile
   */
  def pre {
    if(isFinished) return

    val offsetX = -sketch.hwidth + tile.width /2f
    val offsetY = -sketch.hheight + tile.height /2f 
    
    val x = offsetX + (tile.width * index.x) 
    val y = offsetY + (tile.height * index.y)
	val z = (originalCamera.location.z - (originalCamera.location.z / tiles.height)) * -1 
    
    camera := originalCamera
    camera.track(x, y)
    camera.dolly(z)
    camera.feed(sketch)
  }

  
  /**
   * @return true, when the current render is finished
   */
  def post:Boolean = {
    if(isFinished) return true
      
    val gl = sketch.gl
      
    // be sure OpenGL rendering is finished
    gl.glFlush
    
    // save current glPixelStore values
    val prevRowLength = new Array[Int](1)
    val prevSkipRows = new Array[Int](1)
    val prevSkipPixels = new Array[Int](1)
    val prevAlignment = new Array[Int](1)
    gl.glGetIntegerv(GL.GL_PACK_ROW_LENGTH, prevRowLength, 0)
    gl.glGetIntegerv(GL.GL_PACK_SKIP_PIXELS, prevSkipPixels, 0)
    gl.glGetIntegerv(GL.GL_PACK_SKIP_ROWS, prevSkipRows, 0)
    gl.glGetIntegerv(GL.GL_PACK_ALIGNMENT, prevAlignment, 0)
    
    val destX = sketch.width * index.x
    val destY = sketch.height * (tiles.y - index.y - 1)
    
    //info("tile", index, "width", image.width.toInt, "dest", destX, destY)
    
    // setup pixel store for glReadPixels
    gl.glPixelStorei(GL.GL_PACK_ROW_LENGTH, image.width.toInt)
    gl.glPixelStorei(GL.GL_PACK_SKIP_ROWS, destY)
    gl.glPixelStorei(GL.GL_PACK_SKIP_PIXELS, destX)
    gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1)
    
    // read the tile into the final image
    gl.glReadPixels(0, 0, camera.width, camera.height, dataFormat, dataType, buffer)
    
    // restore previous glPixelStore values
    gl.glPixelStorei(GL.GL_PACK_ROW_LENGTH, prevRowLength(0))
    gl.glPixelStorei(GL.GL_PACK_SKIP_ROWS, prevSkipRows(0))
    gl.glPixelStorei(GL.GL_PACK_SKIP_PIXELS, prevSkipPixels(0))
    gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, prevAlignment(0))

    //import com.sun.opengl.util._
    //import java.io.File
    //Screenshot.writeToTargaFile(new File("recordings/tile"+ index.x +"_"+ index.y +".png"), 0, 0, sketch.width, sketch.height,false)
    
    next
  }
  
  /** increment counter and check if we're done */
  protected def next:Boolean = {      
    index.x += 1
    if(index.x == tiles.columns) {
      index.x = 0
      index.y += 1
      if(index.y == tiles.rows) {
        index.y = 0
        
        // redraw view using original camera settings
        camera := originalCamera
        isFinished = true
      }
    }
    
    isFinished
  }
}