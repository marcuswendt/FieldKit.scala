/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 23, 2009 */
package field.kit.gl.render.objects

import javax.media.opengl.GL

/**
 * The frame buffer object architecture (FBO) is an extension to OpenGL for doing 
 * flexible off-screen rendering, including rendering to a texture. 
 * By capturing images that would normally be drawn to the screen, it can be used 
 * to implement a large variety of image filters, and post-processing effects.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Framebuffer_Object">Wikipedia Framebuffer Object</a>
 * @author Marcus Wendt
 */
class FrameBuffer extends GLObject {
  def create {
    val ids = new Array[Int](1)
    gl.glGenFramebuffersEXT(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = gl.glDeleteFramebuffersEXT(1, Array(id), 0)
  
  def bind = gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, id)
  
  def unbind = gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0)
  
  def status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT)
  
  // TODO should use a real texture object, and respect its texture unit -> GL_COLOR_ATTACHMENT0_EXT
  def attach(texture:Int) = 
    gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_2D, texture, 0)
}