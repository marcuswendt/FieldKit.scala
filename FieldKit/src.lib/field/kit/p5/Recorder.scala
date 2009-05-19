/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.p5

object Recorder {
  object FileFormat extends Enumeration {
    val TGA = Value("tga")
    val PNG = Value("png")
    val JPG = Value("jpg")
  }
    
  object State extends Enumeration {
    val SCREENSHOT = Value
    val SEQUENCE = Value
    val OFF = Value
  }
}

/**
 * provides screenshot, sequence and tile recording for a renderer
 * 
 * @see <a href="https://dev.field.io/hg/opensource/libAGL/raw-file/9d7bd472280f/src/field/lib/agl/util/recorder/Recorder.scala">libAGL Recorder</a>
 * @author Marcus Wendt
 */
class Recorder(s:BasicSketch) extends field.kit.Logger {
  import java.io.File
  import java.nio.ByteBuffer
  import java.awt.image.BufferedImage
 
  import javax.media.opengl.GL
  import javax.media.opengl.GLException

  import field.kit.util.Compressor
  
  // configuration
  var name = "screenshot"
  var baseDir = "./recordings"
  var alpha = false
  var format = Recorder.FileFormat.PNG
  
  var image:BufferedImage = null
  var buffer:ByteBuffer = null
  
  // internal 
  private var state = Recorder.State.OFF
  private var sequenceBasedir = "./"
  private var sequenceFrame = 0

  // ---------------------------------------------------------------------------
  
  // used with tiler, later
  /** should be called before anything is drawn to the screen */
  def pre {
    // check if we need to reinitialize the image and buffer
    if(format != Recorder.FileFormat.TGA) {
      val initRequired = if(buffer == null)
        true
      else
        image.getWidth != s.width || image.getHeight != s.height
      
      if(initRequired) {
        val ib = Compressor.prepare(s.width, s.height, alpha)
        image = ib._1
        buffer = ib._2
      }
    }
      // var image = Screenshot.readToBufferedImage(width, height, alpha)
  }
  
  /** should be called after the drawing is finished */
  def post {
    import java.io.IOException
    import com.sun.opengl.util.Screenshot
    import com.sun.opengl.util.TGAWriter    
    import field.kit.util.Timestamp
    
    if(!isRecording) return
    
    var width = s.width
    var height = s.height
    val suffix = "."+ format
    
    // prepare file & folders
    val file = state match {
      case Recorder.State.SCREENSHOT =>
        val f = new File(baseDir +"/"+ name + "_" + Timestamp() + suffix)
        info("file "+ f)
        f.getParentFile.mkdirs
        f
        
      case Recorder.State.SEQUENCE =>
        // create parent folder for the
        if(sequenceFrame == 0) {
          val tmp = new File(name)
          sequenceBasedir = baseDir + "/" + Timestamp()
          new File(sequenceBasedir).mkdirs
          name = tmp.getName
        }
        
        val f = new File(sequenceBasedir + "/" + name +"."+ sequenceFrame + suffix)
        sequenceFrame += 1
        f
      }
    
    // save the file
    try {
      format match {
        case Recorder.FileFormat.TGA => Screenshot.writeToTargaFile(file, width, height, alpha)
        case _ =>
          // capture image into buffer
          val readbackType = if(alpha) GL.GL_ABGR_EXT else GL.GL_BGR
          val gl = s.pgl.gl
          gl.glReadPixels(0, 0, image.getWidth, image.getHeight, readbackType, GL.GL_UNSIGNED_BYTE, buffer)

          // compress buffer
          Compressor(image, format.toString, file)
      }
    } catch {
      case e:GLException => warn(e)
      case e:IOException => warn(e)
    }
    
    // check if we're done
    if(state == Recorder.State.SCREENSHOT) stop
  }
  
  // -- State Control ----------------------------------------------------------
  def isRecording = state != Recorder.State.OFF
  
  def stop = state = Recorder.State.OFF
  
  def screenshot = state = Recorder.State.SCREENSHOT
  
  def sequence {
    if(isRecording) {
      info("sequence recording stopped.")
      stop
    } else {
      info("starting sequence recording...")
      state = Recorder.State.SEQUENCE
      sequenceFrame = 0
    }
  }
}
