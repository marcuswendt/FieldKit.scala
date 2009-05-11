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
 * Provides usage constants for the <code>VertexBuffer</code> class
 */
object VertexBuffer extends Enumeration {
  val STATIC_DRAW = Value(GL.GL_STATIC_DRAW_ARB)
  val STATIC_READ = Value(GL.GL_STATIC_READ_ARB)
  val STATIC_COPY = Value(GL.GL_STATIC_COPY_ARB)
  val DYNAMIC_DRAW = Value(GL.GL_DYNAMIC_DRAW_ARB)
  val DYNAMIC_READ = Value(GL.GL_DYNAMIC_READ_ARB)
  val DYNAMIC_COPY = Value(GL.GL_DYNAMIC_COPY_ARB)
  val STREAM_DRAW = Value(GL.GL_STREAM_DRAW_ARB)
  val STREAM_READ = Value(GL.GL_STREAM_READ_ARB)
  val STREAM_COPY = Value(GL.GL_STREAM_COPY_ARB)
}

/**
 * A Vertex Buffer Object (VBO) is an OpenGL extension that provides methods for 
 * uploading data (vertex, normal vector, color, etc) to the video device for 
 * non-immediate-mode rendering. 
 * VBOs offer substantial performance gains over immediate mode rendering primarily 
 * because the data resides in the video device memory rather than the system memory 
 * and so it can be rendered directly by the video device.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Vertex_Buffer_Object">Wikipedia Vertex Buffer Object</a>
 * @see <a href="http://www.songho.ca/opengl/gl_vbo.html">Songho.ca VBO</a>
 * @author Marcus Wendt
 */
class VertexBuffer extends GLObject {
  import java.nio.FloatBuffer
  
  def create {
    val ids = new Array[Int](1)
    gl.glGenBuffers(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = gl.glDeleteBuffers(1, Array(id), 0)
  
  def bind {
     gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
     gl.glBindBuffer(GL.GL_ARRAY_BUFFER_ARB, id)
  }

  def unbind {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER_ARB, 0)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
  }
  
  // see also:
  // glBufferDataARB
  // glBufferSubDataARB
  def data(size:Int, data:FloatBuffer, usage:VertexBuffer.Value) = 
    gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, size, data, usage.id)
  
  def data(offset:Int, size:Int, data:FloatBuffer) =  
    gl.glBufferSubDataARB(GL.GL_ARRAY_BUFFER_ARB, offset, size, data)
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

/**
 * Similar to the <code>FrameBuffer Object</code> this buffer stores the z-depth of the rendered objects
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Z-buffer">Wikipedia Z-Buffer</a>
 * @author Marcus Wendt
 */
class DepthBuffer extends GLObject {
  def create {
    val ids = new Array[Int](1)
    gl.glGenRenderbuffersEXT(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = gl.glDeleteRenderbuffersEXT(1, Array(id), 0)
  
  def bind = gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, id)
  
  def unbind = gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, 0)
  
  def size(width:Int, height:Int) = 
    gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT, width, height)
}