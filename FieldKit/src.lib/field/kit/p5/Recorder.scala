/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.p5

/**
 * provides screenshot, sequence and tile recording for a renderer
 * 
 * @see <a href="https://dev.field.io/hg/opensource/libAGL/raw-file/9d7bd472280f/src/field/lib/agl/util/recorder/Recorder.scala">libAGL Recorder</a>
 * @author Marcus Wendt
 */
class Recorder extends field.kit.Logger {
  import java.io.File
  import java.awt.image.BufferedImage
 
  import javax.media.opengl.GL
  import javax.media.opengl.GLException
  
  import com.sun.opengl.util.TGAWriter

  object FileFormat extends Enumeration {
    val TGA = Value("tga")
    val PNG = Value("png")
    val JPG = Value("jpg")
  }
    
  object State extends Enumeration {
    val Screenshot = Value
    val Sequence = Value
    val Off = Value
  }
  
  // Variables
  var name = "screenshot"
  var alpha = false
  var format = FileFormat.TGA

  // used with tiler, later
  /** should be called before anything is drawn to the screen */
  def pre {}
  /** should be called after the drawing is finished */
  def post {}
  
  def screenshot {
    // TODO finish this
  }
  
  def screenshot(file:File) {
    
  }
}
