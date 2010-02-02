/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.objects

import field.kit.gl._

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
  import java.nio.IntBuffer
  
  var needsUpdate = true
  
  protected var _image:Image = null
  protected var _wrap = Texture.Wrap.CLAMP
  protected var _filter = Texture.Filter.NEAREST
  
  var width = 0
  var height = 0
  
  protected var pixels:Array[Int] = null
  protected var buffer:IntBuffer = null
  
  id = Texture.UNDEFINED

  // -- Methods ----------------------------------------------------------------
  def this(image:Image) = {
    this()
    this.image = image
  }
  
  def image = _image
  def image_=(image:Image) = {
    this._image = image
    updateImage
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
    if(image == null) 
      return
        
    if(id == Texture.UNDEFINED) create
    
    // upload image data to texture
    try {
      //info("updating texture", this.id, "width", image.width, "height", image.height)
        
      // upload data
      gl.glEnable(GL.GL_TEXTURE_2D)
      gl.glBindTexture(GL.GL_TEXTURE_2D, this.id)
      gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, width, height, 
                        0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer)
        
      // set filter parameters
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, filter.id)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filter.id)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrap.id)
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrap.id)
        
      // select modulate to mix texture with color for shading
      // glTexEnvf( GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE );
      unbind
//    } catch {
//      case e:java.lang.IndexOutOfBoundsException => 
//        warn("update: Couldnt upload image", e)
//        id = Texture.UNDEFINED
    }
    
    needsUpdate = false
  }
  
  /** based on code ported from Processings PGraphicsOpenGL */
  protected def updateImage {
    import field.kit.util.Buffer
    
    // simply set texture dimensions to image dimensions, assuming the gpu supports
    // non power of two textures
    width = image.width
    height = image.height
    
    // make sure width & height are a power of 2
    /*
    width = 2
    height = 2
    
    while(width < image.width)
      width *= 2
    
    while(height < image.height)
      height *= 2
    */
    
    // create pixels storage
    if(pixels == null) {
      pixels = new Array[Int](width * height)
      buffer = Buffer.int(width * height)
    }
    
    // copy data into the texture
    var t = 0
    var p = 0
    image.format match {
      case Image.Format.GREY =>
        for(y <- 0 until image.height) {
          for(x <- 0 until image.width) {
            // flip the image upside down
            val pixel = image.pixels((image.height - y - 1) * image.width + x)
            
            pixels(t) = (pixel << 24) | 0x00FFFFFF
            t += 1
            p += 1
          }
          t += width - image.width
        }
        
      case Image.Format.RGB =>
        for(y <- 0 until image.height) {
          for(x <- 0 until image.width) {
            //val pixel = image.pixels(p)
            
            // flip the image upside down
            val pixel = image.pixels((image.height - y - 1) * image.width + x)
            
            // needs to be ABGR, stored in memory xRGB
            // so R and B must be swapped, and the x just made FF
            pixels(t) = 0xff000000 |  // force opacity for good measure
                		((pixel & 0xFF) << 16) |
                		((pixel & 0xFF0000) >> 16) |
                		(pixel & 0x0000FF00)
            t += 1
            p += 1
          }
          t += width - image.width
        }
        
      case Image.Format.ARGB =>
        for(y <- 0 until image.height) {
          for(x <- 0 until image.width) {
            //val pixel = image.pixels(p)
            
            // flip the image upside down
            val pixel = image.pixels((image.height - y - 1) * image.width + x)
            
            // needs to be ABGR stored in memory ARGB
            // so R and B must be swapped, A and G just brought back in
            pixels(t) = ((pixel & 0xFF) << 16) |
                		((pixel & 0xFF0000) >> 16) |
                		(pixel & 0xFF00FF00)
            
            t += 1
            p += 1
          }
          t += width - image.width
        }

    }
    
    // put pixels into buffer and rewind
    buffer.put(pixels)
    buffer.rewind
  }
  
  /** checks if the associated texture is still valid */
  def isValid = gl.glIsTexture(this.id)
  
  def create {
    val ids = new Array[Int](1)
    gl.glGenTextures(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy { 
    if(isValid)
      gl.glDeleteTextures(1, Array(id), 0)
  }
  
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
  
//  import java.nio.Buffer
//  
//  def data(format:Int, width:Int, height:Int, data:Buffer) {
//    if(!isValid) return
//    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 
//                    format, width, height, 0, format, GL.GL_UNSIGNED_BYTE, data)
//  }
}
