/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.render

/**
 * Companion object for the <code>Image</code> class.
 * Has various helper methods to load and create Images.
 * @author Marcus Wendt
 */
object Image extends field.kit.Logger {
  import java.awt.image.BufferedImage
  import java.net.URL
  import javax.imageio.ImageIO
  import java.nio.ByteBuffer
  
  import scala.collection.mutable.HashMap
  
  import field.kit.util.BufferUtil
  
  private val DEFAULT_USE_CACHE = true
  
  val cache = new HashMap[URL, Image]
  
  /** Creates a new Image with the given dimensions and alpha */
  def apply(width:Int, height:Int, alpha:Boolean) = create(width, height, alpha)

  /** Resolves the given string as URL and returns an Image */
  def apply(file:String) = load(file, DEFAULT_USE_CACHE)
  
  /** Loads the image from the given URL */
  def apply(file:URL) = load(file, DEFAULT_USE_CACHE)
  
  /** Resolves the given string as URL and returns an Image */
  def apply(file:String, useCache:Boolean) = load(file, useCache)
  
  /** Loads the image from the given URL */
  def apply(file:URL, useCache:Boolean) = load(file, useCache)
    
  // -- Loading & Creating -----------------------------------------------------
  /** Resolves the given string as URL and returns an Image */
  def load(file:String, useCache:Boolean):Image = {
    import field.kit.util.Loader
    Loader.resolveToURL(file) match {
      case null => warn("load: Couldnt find file '"+ file +"'"); null
      case url:URL => load(url, useCache)
    }
  }
  
  /** 
   * Loads the image from the given URL and stores it in the cache 
   */
  def load(url:URL, useCache:Boolean):Image = {
    var image:Image = null
    
    if(url == null) { 
      warn("load: No URL given")
      null
      
    } else if(cache.contains(url) && useCache) {
      info("reusing", url)
      image = cache(url)
      
    } else {
      info("loading", url)
    
      if(url.getFile.toLowerCase.endsWith(".tga"))
        image = loadTGA(url)
      else {
        try {
          image = loadImage(ImageIO.read(url))
        } catch {
          case e:Exception => warn("load", url, e)
        } 
      }
      
      // put image into cache
      if(image != null && useCache) cache(url) = image
    }
    
    image
  }
  
  protected def loadTGA(url:URL) = {
    import javax.media.opengl.GL
    import com.sun.opengl.util.texture.spi.TGAImage
    val tga = TGAImage.read(url.openStream)
    
    val format = if(tga.getGLFormat == GL.GL_BGR) Format.BGR else Format.BGRA
    create(tga.getWidth, tga.getHeight, format, tga.getData)
  }
  
  /** Creates a FieldKit Image from a Java AWT Image */
  protected def loadImage(sourceImage:BufferedImage) = {
    import com.sun.opengl.util.ImageUtil
    
    var width = sourceImage.getWidth
    var height = sourceImage.getHeight
    val alpha = hasAlpha(sourceImage)
    
    // flip image so it sits correctly in the OpenGL view 
    ImageUtil.flipImageVertically(sourceImage)

    // create image
    val image = create(width, height, alpha)

    // eventually resize the image to match the texture's dimensions
    val textureImage = 
      if(image.texWidth != image.width || image.texHeight != image.height) {
        import java.awt.Color
        
        // Transformations seem to cause random errors in some JRE versions
        // better resize the old school way
        // However g.drawImage doesnt seem to respect the Alpha channel
        import java.util.Map
        import java.util.HashMap

        import java.awt.RenderingHints
        import java.awt.image.RescaleOp
        import java.awt.image.AffineTransformOp
        import java.awt.geom.AffineTransform
        import java.awt.Color
         
        val af = AffineTransform.getScaleInstance(
          image.texWidth/ image.width.asInstanceOf[Float],
          image.texHeight/ image.height.asInstanceOf[Float] )
        
        val rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val transform = new AffineTransformOp(af,rh)
             
        val resizedImage = transform.createCompatibleDestImage(sourceImage, sourceImage.getColorModel)
        transform.filter(sourceImage, resizedImage)
        
        /*
        val imageType = if(alpha) BufferedImage.TYPE_4BYTE_ABGR else BufferedImage.TYPE_3BYTE_BGR
        val resizedImage = new BufferedImage(image.texWidth, image.texHeight, sourceImage.getType)
        val g = resizedImage.getGraphics
        g.drawImage(sourceImage, 0, 0, image.texWidth, image.texHeight, new Color(0f,0f,0f,0f), null)
        */
        
        resizedImage
      } else {
        sourceImage
      }
    
    var rasterData = textureImage.getRaster.getDataElements(0, 0, textureImage.getWidth, textureImage.getHeight, null)

    // check if data is really a byte array, otherwise we need to convert it
    // this only seems to happen with 1.5 jre's
    val data:Array[Byte] = rasterData match {
      case byte:Array[Byte] => byte
      case _ =>
        val tmp = new BufferedImage(textureImage.getWidth, textureImage.getHeight, 
                                    if(alpha) BufferedImage.TYPE_4BYTE_ABGR 
                                    else BufferedImage.TYPE_3BYTE_BGR)
        val g = tmp.getGraphics
        g.drawImage(textureImage, 0, 0, null)
        tmp.getRaster.getDataElements(0, 0, tmp.getWidth, tmp.getHeight, null).asInstanceOf[Array[Byte]]
    }
    
    image.data.put(data)
    image.data.flip
    
    image
  }
  
  /** Creates a new Image with the given dimensions and alpha */
  def create(width:Int, height:Int, alpha:Boolean):Image = 
    create(width, height, if(alpha) Format.RGBA else Format.RGB, null)
  
  /** Creates a new Image with the given dimensions and format */
  def create(width:Int, height:Int, format:Format.Value, data:ByteBuffer) = {
    // calculate texture size    
    var texWidth = 2
    while(texWidth < width)
      texWidth *= 2
    
    var texHeight = 2
    while(texHeight < height)
      texHeight *= 2
    
    val image = new Image
    image.format = format
    image.width = width
    image.height = height
    image.texWidth = texWidth
    image.texHeight = texHeight
    
    // creates an empty data buffer
    if(data == null) {
      val scratch = BufferUtil.byte(texWidth * texHeight * bitdepth(format))
      scratch.limit(scratch.capacity)
      image.data = scratch
      
    // otherwise assigns the given buffer
    } else {
      image.data = data
    }
    
    image
  }
  
  // -- Helpers ----------------------------------------------------------------
  def bitdepth(format:Format.Value) = 
    format match {
      case Image.Format.RGBA => 4
      case Image.Format.RGB => 3
      case Image.Format.BGRA => 4
      case Image.Format.BGR => 3
      case Image.Format.GREY => 1 
    }
  
  def hasAlpha(format:Image.Format.Value) = 
    format == Image.Format.RGBA || format == Image.Format.BGRA 
  
  /** Tests wether the given image has an alpha channel */
  def hasAlpha(image:java.awt.Image) = 
    image match {
      case null => false
        
      case bufImg:BufferedImage =>
        bufImg.getColorModel.hasAlpha
        
      case _ =>
        import java.awt.image.PixelGrabber
        val pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false)
        pixelGrabber.grabPixels
        val colorModel = pixelGrabber.getColorModel
        if(colorModel != null)
          colorModel.hasAlpha
        else
          false
    }
  
  // -- Constants --------------------------------------------------------------
  /** A list of constants that can be used to set the format of an <code>Image</code> */
  object Format extends Enumeration {
    val RGBA = Value("RGBA")
    val RGB = Value("RGB")
    val BGR = Value("BGR")
    val BGRA = Value("BGRA")
    val GREY = Value("GREYSCALE")
  }
}

/**
 * Lightweight store for raw image data and format description
 * @author Marcus Wendt
 */
class Image extends field.kit.Logger {
  import java.nio.ByteBuffer
  import javax.media.opengl.GL
  
  /** the original image width, 
   * we need to remember this so we set the image to its proper dimensions later */
  var width = 0
  
  /** the original image height, 
   * we need to remember this so we set the image to its proper dimensions later */
  var height = 0
  
  /** the width of the opengl texture 
   * (not necessarily the same as width) */
  var texWidth = 0
  
  /** the height of the opengl texture 
   * (not necessarily the same as height) */
  var texHeight = 0
  
  /** the image's texture data */
  var data:ByteBuffer = null
  
  protected var _format:Image.Format.Value = null
  def format = _format
  
  // init defaults
  format = Image.Format.RGBA
  
  // -- OpenGL Data Descriptors ------------------------------------------------
  /**
   * Specifies the number of color components in
   * the texture. Must be 1, 2, 3, or 4, or one
   * of the following symbolic constants:
   * GL_ALPHA, GL_ALPHA4, GL_ALPHA8, GL_ALPHA12, GL_ALPHA16, GL_LUMINANCE, GL_LUMINANCE4,
   * GL_LUMINANCE8, GL_LUMINANCE12, GL_LUMINANCE16, GL_LUMINANCE_ALPHA, GL_LUMINANCE4_ALPHA4, 
   * GL_LUMINANCE6_ALPHA2, GL_LUMINANCE8_ALPHA8, GL_LUMINANCE12_ALPHA4, GL_LUMINANCE12_ALPHA12,
   * GL_LUMINANCE16_ALPHA16, GL_INTENSITY, GL_INTENSITY4, GL_INTENSITY8, GL_INTENSITY12, 
   * GL_INTENSITY16, GL_R3_G3_B2, GL_RGB, GL_RGB4, GL_RGB5, GL_RGB8, GL_RGB10,
   * GL_RGB12, GL_RGB16, GL_RGBA, GL_RGBA2, GL_RGBA4, GL_RGB5_A1, GL_RGBA8, GL_RGB10_A2,
   * GL_RGBA12, or GL_RGBA16. 
   */
  var glFormat = 0
  
  /**
   * Specifies the   format of the pixel data.
   * The following symbolic values are accepted:
   * GL_COLOR_INDEX, GL_RED, GL_GREEN, GL_BLUE, GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, and GL_LUMINANCE_ALPHA. 
   */
  var glDataFormat = 0
  
  /**
   * Specifies the  data type of the pixel data. The following symbolic values are accepted:
   * GL_UNSIGNED_BYTE, GL_BYTE, GL_BITMAP, GL_UNSIGNED_SHORT, GL_SHORT, GL_UNSIGNED_INT, GL_INT, and GL_FLOAT.
   */
  var glDataType = 0
  
  // -- Methods ----------------------------------------------------------------
  def format_=(format:Image.Format.Value) {
    this._format = format 
    format match {
      case Image.Format.RGBA =>
         glFormat = GL.GL_RGBA8
         glDataFormat = GL.GL_RGBA
         
      case Image.Format.RGB =>
        glFormat = GL.GL_RGB8
        glDataFormat = GL.GL_RGB
        
      case Image.Format.BGRA =>
        glFormat = GL.GL_RGBA8
        glDataFormat = GL.GL_BGRA
        
      case Image.Format.BGR =>
        glFormat = GL.GL_RGB8
        glDataFormat = GL.GL_BGR
        
      case Image.Format.GREY =>
        glFormat = GL.GL_LUMINANCE
        glDataFormat = GL.GL_LUMINANCE
    }
    
    glDataType = GL.GL_UNSIGNED_BYTE
  }
  
  def destroy {
    if(data != null) {
      data.clear
      data = null
    }
  }
  
  override def toString = "Image("+ format +" "+ width +"x"+ height +")"
}
