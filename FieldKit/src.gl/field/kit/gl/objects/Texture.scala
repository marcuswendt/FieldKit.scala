/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.objects

import field.kit._
import field.kit.gl._
import javax.media.opengl.GL

/** 
* Companion object to <code>Texture</code>
* @author Marcus Wendt
*/
object Texture extends Logger {
	import java.net.URL
	import com.sun.opengl.util.texture._
	import field.kit.util.Loader
	
	// Fast JOGL Textures
	/** Creates a new <code>JOGLTexture</code>  */
	def apply(path:String, mipmap:Boolean = true):JOGLTexture = 
		apply(Loader.resolveToURL(path), mipmap)

	/** Creates a new <code>JOGLTexture</code>> */
	def apply(file:URL, mipmap:Boolean):JOGLTexture = {
		import javax.imageio.ImageIO
		import com.sun.opengl.util.ImageUtil
		
		try {
			val image = ImageIO.read(file)
			ImageUtil.flipImageVertically(image)
			
			val suffix:String = null
			val data = TextureIO.newTextureData(image, mipmap)
			
			new JOGLTexture(data)
		} catch {
			case e:java.io.IOException => 
				warn(e)
				null
		}
	}
		
	/** Creates a new <code>ImageTexture</code> with a blank <code>Image</code> of the given dimensions */
	def apply(width:Int, height:Int, alpha:Boolean) =
		new ImageTexture(Image(width, height, alpha))

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
* Base class for different types of OpenGL textures
* @author Marcus Wendt
*/
abstract class Texture extends GLObject {
	
	id = Texture.UNDEFINED
	
	protected var _width = 0
	protected var _height = 0
	protected var _wrap = Texture.Wrap.CLAMP
	protected var _filter = Texture.Filter.NEAREST
	protected var needsUpdate = true
	
	def width = _width
	def height = _height
		
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

	/** checks if the associated texture is still valid */
	def isValid = gl.glIsTexture(this.id)
}
