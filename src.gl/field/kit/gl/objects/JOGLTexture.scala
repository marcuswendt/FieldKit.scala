/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 17, 2010 */
package field.kit.gl.objects

import javax.media.opengl.GL
import com.sun.opengl.util.texture._

/**
 * A lightweight and fast Texture whose pixel contents cannot be accessed directly
 * @author marcus
 */
class JOGLTexture(data:TextureData) extends Texture {
	
	protected var texture = TextureIO.newTexture(data)
	needsUpdate = true
	
	_width = texture.getWidth
	_height = texture.getHeight
	
	def bind {
		texture.enable()
		texture.bind()
		if(needsUpdate) update
	}
	
	protected def update {
		texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, filter.id)
		texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, filter.id)
		texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, wrap.id)
		texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, wrap.id)
		needsUpdate = false
	}
	
	def unbind = texture.disable()
	
	def create = this.id = texture.getTextureObject
	
	def destroy = {}
}