/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 23, 2009 */
package field.kit.gl.render.objects


object FrameBuffer {
  import javax.media.opengl.GL

  object Format extends Enumeration {
    val RGB = Value(GL.GL_RGB)
    val RGBA = Value(GL.GL_RGBA)
    val DEPTH = Value(GL.GL_DEPTH_COMPONENT)
    val STENCIL = Value(GL.GL_STENCIL_INDEX)
  }
}

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
  import javax.media.opengl.GL

  // automatically create a framebuffer when this class is instantiated
  create
  
  def create {
    val ids = new Array[Int](1)
    gl.glGenFramebuffersEXT(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = gl.glDeleteFramebuffersEXT(1, Array(id), 0)
  
  def bind = gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, id)
  
  def unbind = gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0)
  
  def status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT)
  
  /** initialize storage capacity for this fbo */
  def init(format:FrameBuffer.Format.Value, width:Int, height:Int) {
    gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, format.id, width, height)
  }
  
  // TODO should use a real texture object, and respect its texture unit -> GL_COLOR_ATTACHMENT0_EXT
  def +=(buffer:GLObject) {
    buffer match {
      case texture:Texture =>
        gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, 
                                     GL.GL_COLOR_ATTACHMENT0_EXT, 
                                     GL.GL_TEXTURE_2D, texture.id, 0)
        
      case depth:DepthBuffer =>
        gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT,
                                        GL.GL_RENDERBUFFER_EXT, depth.id)
    }
  }
  
  def isComplete = {
    status match {
      case GL.GL_FRAMEBUFFER_COMPLETE_EXT => 
        info("fbo complete")
        true
        
      case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT =>
        warn("GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT")
        false
      case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT =>
        warn("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT")
        false
      case _ =>
        warn("Unknown GL Error")
        false
    }
  }
}