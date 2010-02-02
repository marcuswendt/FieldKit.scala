/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 23, 2009 */
package field.kit.gl.objects

import field.kit.gl._
import javax.media.opengl.GL

/**
 * Similar to the <code>FrameBuffer Object</code> this buffer stores the z-depth of the rendered objects
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Z-buffer">Wikipedia Z-Buffer</a>
 * @author Marcus Wendt
 */
class DepthBuffer extends GLObject {
  
  // automatically create a buffer when this class is instantiated
  create
  
  def this(width:Int, height:Int) {
    this()
    create
    bind
    size(width, height)
    unbind
  }
  
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