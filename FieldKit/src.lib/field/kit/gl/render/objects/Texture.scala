/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.render.objects

import field.kit.gl.render._

/** 
 * Companion object to <code>Texture</code>
 * @author Marcus Wendt
 */
object Texture {
  import javax.media.opengl.GL
  import java.net.URL

  /** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given path */
  def apply(file:String) = load(file)

  /** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given <code>URL</code> */
  def apply(file:URL) = load(file)
  
  /** Creates a new <code>Texture</code> with a blank <code>Image</code> of the given dimensions */
  def apply(width:Int, height:Int, alpha:Boolean) = 
    create(width, height, alpha)
  
  def apply(image:Image) = new Texture(image)
  
  // -- Loading & Creating -----------------------------------------------------
  /** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given path */
  def load(file:String) = new Texture(Image(file))
  
  /** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given <code>URL</code> */
  def load(file:URL) = new Texture(Image(file))
  
  /** Creates a new <code>Texture</code> with a blank <code>Image</code> of the given dimensions */
  def create(width:Int, height:Int, alpha:Boolean) =
    new Texture(Image(width, height, alpha))
  
  // -- Constants --------------------------------------------------------------
  val UNDEFINED = -1
  
  object Wrap extends Enumeration {
    val CLAMP = Value(GL.GL_CLAMP)
    val MIRRORED_CLAMP = Value(GL.GL_MIRROR_CLAMP_EXT)
    val REPEAT = Value(GL.GL_REPEAT)
    val MIRRORED_REPEAT = Value(GL.GL_MIRRORED_REPEAT)
  }

  object Filter extends Enumeration {
    val NEAREST = Value(GL.GL_NEAREST)
    val LINEAR = Value(GL.GL_LINEAR)
  }
}

/**
 * Holds an OpenGL texture
 * @author Marcus Wendt
 */
class Texture extends GLObject {
  import javax.media.opengl.GL
  
  // -- Variables --------------------------------------------------------------
  protected var needsUpdate = false
  protected var _image:Image = null

  // -- OpenGL Data Descriptors ------------------------------------------------
  protected var _wrap = Texture.Wrap.CLAMP
  protected var _filter = Texture.Filter.NEAREST
    
  // init
  id = Texture.UNDEFINED
  
  // automatically create a texture when this class is instantiated
  // create

  // -- Methods ----------------------------------------------------------------
  def this(image:Image) = {
    this()
    this.image = image
  }
  
  def image = _image
  def image_=(image:Image) = {
    this._image = image
    needsUpdate = true
  }
  
  def wrap = _wrap
  def wrap_=(wrap:Texture.Wrap.Value) {
    this._wrap = wrap
    needsUpdate = true
  }
  
  def filter = _filter
  def filter_=(filter:Texture.Filter.Value) {
    this._filter = filter
    needsUpdate = true
  }
  
  protected def update {
    if(image != null) {
      if(id == Texture.UNDEFINED) create
      
      // upload image data to texture
      try {
        //info("updating texture", this.id, "width", image.width, "height", image.height, "data", image.data)
        
        gl.glEnable(GL.GL_TEXTURE_2D)
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.id)
        
        // make sure the data buffer is ready
        image.data.rewind
        
        // upload data
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, image.glFormat, image.texWidth, image.texHeight, 
                        0, image.glDataFormat, image.glDataType, image.data)
        
        // set filter parameters
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, filter.id)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filter.id)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrap.id)
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrap.id)
        
        // select modulate to mix texture with color for shading
        // glTexEnvf( GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE );
          
        unbind
      } catch {
        case e:java.lang.IndexOutOfBoundsException => { 
          warn("update: Couldnt upload image", e)
          id = Texture.UNDEFINED
          image = null
        }
      }
      needsUpdate = false
    }
  }
  
  /** checks if the associated texture is still valid */
  def isValid = gl.glIsTexture(this.id)
  
  def create {
    val ids = new Array[Int](1)
    gl.glGenTextures(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = 
    if(isValid)
      gl.glDeleteTextures(1, Array(id), 0)
  
  def bind {
    // check if texture is still valid
    if(!isValid && this.id != Texture.UNDEFINED) {
      warn("need to recreate texture "+ this.id)
      this.id = Texture.UNDEFINED
      needsUpdate = true
    }
    
    if(needsUpdate) update
    
    if(isValid) {
      gl.glEnable(GL.GL_TEXTURE_2D)
      gl.glBindTexture(GL.GL_TEXTURE_2D, this.id)
    }
  }
  
  def unbind {
    if(isValid) {
      gl.glDisable(GL.GL_TEXTURE_2D)
      gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
    }
  }
  
  import java.nio.Buffer
  
  def data(format:Int, width:Int, height:Int, data:Buffer) {
    if(!isValid) return
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 
                    format, width, height, 0, format, GL.GL_UNSIGNED_BYTE, data)
  }
}
