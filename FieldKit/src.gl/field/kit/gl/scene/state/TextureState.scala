/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.scene.state

import field.kit.gl._
import field.kit.gl.scene.RenderState
import field.kit.gl.objects.Texture

/** Companion object to <code>TextureState</code> */
object TextureState {
	import java.net.URL

	def apply(texture:Texture) = new TextureState(texture)

	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give path */
	def apply(file:String, mipmap:Boolean) =
		new TextureState(Texture(file, mipmap))

	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give URL */
	def apply(file:URL, mipmap:Boolean) = 
		new TextureState(Texture(file, mipmap))

	/** Creates a new <code>TextureState</code> with a single Texture and a blank Image with the given properties */
	def apply(width:Int, height:Int, alpha:Boolean = true, mipmap:Boolean = true) =
		new TextureState(Texture(width, height, alpha))
}

/** Applies a texture to an object */
class TextureState extends RenderState {
	import javax.media.opengl.GL
	import scala.collection.mutable.ArrayBuffer

	var textures = new ArrayBuffer[Texture]

	def this(texture:Texture) = {
		this()
		this() = texture
	}

	/** Enables all registered Textures */
	def enable {
		var i = 0
		while(i < textures.length) {
			val t = textures(i)
			if(t != null) {
				activateUnit(i)
				t.bind
			}
			i += 1
		}
	}

	/** Disables all registered Textures */
	def disable {
		var i = 0
		while(i < textures.length) {
			val t = textures(i)
			if(t != null) {
				activateUnit(i)
				t.unbind
			}
			i += 1
		}
		
		gl.glActiveTexture(GL.GL_TEXTURE0)
	}

	/** Destroys all registered Textures */
	def destroy {
		var i = 0
		while(i < textures.length) {
			val t = textures(i)
			if(t != null) {
				activateUnit(i)
				t.destroy	
			}
			i += 1
		}
	}

	/** Activates the texture unit for the given texture */
	protected def activateUnit(texture:Texture):Unit = activateUnit(textures.indexOf(texture))
	
	protected def activateUnit(unit:Int) = gl.glActiveTexture(GL.GL_TEXTURE0 + unit)

	def apply(i:Int) = textures(i)

	/** Sets the given texture for the first texture unit */
	def update(texture:Texture):Unit = update(0, texture)

	/** Sets the given texture for the given texture unit */
	def update(unit:Int, texture:Texture) {
		if(unit >= 0) {
			while (unit >= textures.size) 
				textures += null

			textures(unit) = texture
		}
	}

	// -- Helpers ----------------------------------------------------------------
	/** @return The Texture Unit as int for the given texture */
	def unit(texture:Texture) = textures indexOf texture
}
