/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.p5

import field.kit._

/**
 * Companion object to class <code>Recorder</code>
 */
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
class Recorder(val sketch:Sketch) extends Logger {
  import java.io.File
  import java.nio.ByteBuffer
  import java.awt.image.BufferedImage
 
  import javax.media.opengl.GL
  import javax.media.opengl.GLException
  import com.sun.opengl.util.TGAWriter
  
  import field.kit.gl.util.Compressor
  import field.kit.math.Dim2
  
  // -- Configuration ----------------------------------------------------------
  var name = "screenshot"
  var baseDir = "./recordings"
  var alpha = false
  var fileFormat = Recorder.FileFormat.PNG
  
  /** the target image dimensions */
  protected var image = new Dim2[Int]
  
  // internal
  private var awtImage:BufferedImage = null
  private var buffer:ByteBuffer = null
  private var state = Recorder.State.OFF
  private var sequenceBasedir = "./"
  private var sequenceFrame = 0
  
  // -- tile renderer ----------------------------------------------------------
  private var useTiler = false
  var tiler:Tiler = null
  private var tga:TGAWriter = null
  private var tmpFile:File = null
  
  /**
   * Sets the target image dimensions
   */
  def init(width:Int, height:Int) {
    image := (width, height)    
    // check if we need to render the image as tiles
    useTiler = width > sketch.width || height > sketch.height
  }
  
  /**
   * Initializes the <code>BufferedImage</code> and its <code>ByteBuffer</code> 
   */
  protected def init {
    info("init", image.width, image.height, fileFormat)
    
    // check if we need to reinitialize the image and buffer
    if(fileFormat != Recorder.FileFormat.TGA) {
      val ib = Compressor.init(image.width, image.height, alpha)
      awtImage = ib._1
      buffer = ib._2
      buffer.clear
      //buffer.rewind
    }
    
    // init tiler
    if(useTiler) {
      var dataFormat = if(alpha) GL.GL_ABGR_EXT else GL.GL_BGR

      if(fileFormat == Recorder.FileFormat.TGA) {
        if(alpha) dataFormat = GL.GL_BGRA
        
        if(tmpFile == null)
          tmpFile = new File("tiler_tmp.tga")
        
        tga = new TGAWriter
        tga.open(tmpFile, image.width, image.height, alpha)
        buffer = tga.getImageData
      }
      
      if(tiler == null)
        tiler = new Tiler(this)
      
      tiler.init(image.width, image.height, buffer, dataFormat)
    }
  }
  
  /** 
   * Should be called before anything is drawn to the screen
   */
  def pre {
    if(!isRecording) return
    
    if(useTiler)
      tiler.pre
  }
  
  /** 
   * Saves the current frame if the recording is finished.
   */
  def post {
    if(!isRecording) return
      
    val isFrameFinished = 
      if(useTiler) tiler.post else true
    
    if(isFrameFinished)
      save
  }
  
  /**
   * Writes the finished frame to a file using the <code>Compressor</code> util.
   */
  protected def save {
    import java.io.IOException
    import com.sun.opengl.util.Screenshot 
    
    var width = sketch.width
    var height = sketch.height
    val suffix = "."+ fileFormat
    
    // prepare file & folders
    val file = state match {
      case Recorder.State.SCREENSHOT =>
        val f = new File(baseDir +"/"+ name + "_" + Timer() + suffix)
        info("file "+ f)
        f.getParentFile.mkdirs
        f
        
      case Recorder.State.SEQUENCE =>
        // create parent folder for the
        if(sequenceFrame == 0) {
          val tmp = new File(name)
          sequenceBasedir = baseDir + "/" + Timer()
          new File(sequenceBasedir).mkdirs
          name = tmp.getName
        }
        
        val f = new File(sequenceBasedir + "/" + name +"."+ sequenceFrame + suffix)
        sequenceFrame += 1
        f
      }
    
    // save the file
    try {
      fileFormat match {
        case Recorder.FileFormat.TGA => 
          Screenshot.writeToTargaFile(file, image.width, image.height, alpha)
          
        case _ =>
          // the tiler should already have filled the buffer
          if(!useTiler) {
        	  // capture image into buffer
        	  val readbackType = if(alpha) GL.GL_ABGR_EXT else GL.GL_BGR
        	  
        	  import javax.media.opengl.GLContext
        	  val gl = GLContext.getCurrent.getGL
        	  
        	  gl.glReadPixels(0, 0, awtImage.getWidth, awtImage.getHeight, readbackType, GL.GL_UNSIGNED_BYTE, buffer)
          }
          
          // compress buffer
          Compressor(awtImage, fileFormat.toString, file)
      }
    } catch {
      case e:GLException => warn(e)
      case e:IOException => warn(e)
    }
    
    // check if we're done and how to proceed
    state match {
      case Recorder.State.SCREENSHOT => stop
      case Recorder.State.SEQUENCE => init
      case _ => info("state", state)
    }
  }
  
  // -- State Control ----------------------------------------------------------
  def isRecording = state != Recorder.State.OFF
  
  def stop {
    state = Recorder.State.OFF
  }
  
  def screenshot = {
    init
    state = Recorder.State.SCREENSHOT
  }
  
  def sequence {
    if(isRecording) {
      info("sequence recording stopped.")
      stop
    } else {
      info("starting sequence recording...")
      init
      state = Recorder.State.SEQUENCE
      sequenceFrame = 0
    }
  }
}
