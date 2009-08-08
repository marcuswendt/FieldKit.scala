/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 15, 2009 */
package field.kit.gl.render.objects

import javax.media.opengl.GL

/**
 * Provides usage constants for the <code>VertexBuffer</code> class
 */
object VertexBuffer extends Renderable {
  /** lists different usage types for a vertex buffer */
  object Usage extends Enumeration {
    val STATIC_DRAW = Value(GL.GL_STATIC_DRAW)
    val STATIC_READ = Value(GL.GL_STATIC_READ)
    val STATIC_COPY = Value(GL.GL_STATIC_COPY)
    val DYNAMIC_DRAW = Value(GL.GL_DYNAMIC_DRAW)
    val DYNAMIC_READ = Value(GL.GL_DYNAMIC_READ)
    val DYNAMIC_COPY = Value(GL.GL_DYNAMIC_COPY)
    val STREAM_DRAW = Value(GL.GL_STREAM_DRAW)
    val STREAM_READ = Value(GL.GL_STREAM_READ)
    val STREAM_COPY = Value(GL.GL_STREAM_COPY)
  }
  /** lists different states for a vertex buffer */
  object State extends Enumeration {
    val COLOR = Value(GL.GL_COLOR_ARRAY)
    val EDGE = Value(GL.GL_EDGE_FLAG_ARRAY)
    val INDEX = Value(GL.GL_INDEX_ARRAY)
    val NORMAL = Value(GL.GL_NORMAL_ARRAY)
    val COORD = Value(GL.GL_TEXTURE_COORD_ARRAY)
    val VERTEX = Value(GL.GL_VERTEX_ARRAY)
  }
  
  def isSupported = gl.isFunctionAvailable("glGenBuffers") &&
  	gl.isFunctionAvailable("glBindBuffer") &&
  	gl.isFunctionAvailable("glBufferData") &&
  	gl.isFunctionAvailable("glDeleteBuffers")
  
  def render {}
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
class VertexBuffer(var state:VertexBuffer.State.Value) extends GLObject {
  import java.nio.FloatBuffer
  
  def this() = this(VertexBuffer.State.VERTEX)
  
  def this(buffer:java.nio.FloatBuffer) {
    this(VertexBuffer.State.VERTEX)
    create
    bind
    buffer.rewind
    data(buffer.capacity, buffer, VertexBuffer.Usage.STATIC_DRAW)
    unbind
  }
  
  def create {
    val ids = new Array[Int](1)
    gl.glGenBuffers(ids.length, ids, 0)
    this.id = ids(0)
  }
  
  def destroy = gl.glDeleteBuffers(1, Array(id), 0)
  
  def bind = {
    gl.glEnableClientState(state.id)
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id)
  }

  def unbind = {
    gl.glDisableClientState(state.id)
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
  }
  
  def data(size:Int, data:FloatBuffer, usage:VertexBuffer.Usage.Value) =
    gl.glBufferData(GL.GL_ARRAY_BUFFER, size * java.lang.Float.SIZE, data, usage.id)
  
  /** uploads a chunk of the given FloatBuffer into the VBO */
  def data(offset:Int, size:Int, data:FloatBuffer) =  
    gl.glBufferSubData(GL.GL_ARRAY_BUFFER, offset * java.lang.Float.SIZE, size * java.lang.Float.SIZE, data)
  
  /** uploads the entire FloatBuffer into the VBO */
  def data(data:FloatBuffer) {
    data.rewind
    gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, data.capacity * java.lang.Float.SIZE, data)
  }
}