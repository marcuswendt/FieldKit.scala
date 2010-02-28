/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.scene.state

import field.kit.gl._
import field.kit.gl.scene.RenderState
import field.kit.gl.objects.Texture

/** Companion object to <code>TextureState</code> */
object TextureState {
	import java.net.URL

	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give path */
	def apply(file:String) = load(file)

	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give URL */
	def apply(file:URL) = load(file)

	/** Creates a new <code>TextureState</code> with a single Texture and a blank Image with the given properties */
	def apply(width:Int, height:Int, alpha:Boolean) = create(width, height, alpha)

	def apply(texture:Texture) = new TextureState(texture)

	// -- Loading & Creating -----------------------------------------------------
	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give path */
	def load(file:String) = new TextureState(Texture(file))

	/** Creates a new <code>TextureState</code> with a single Texture loaded from the give URL */
	def load(file:URL) = new TextureState(Texture(file))

	/** Creates a new <code>TextureState</code> with a single Texture and a blank Image with the given properties */
	def create(width:Int, height:Int, alpha:Boolean) =
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
