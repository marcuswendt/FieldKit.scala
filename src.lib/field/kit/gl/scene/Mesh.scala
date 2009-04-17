/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Renderable

/** Base class for all sorts of polygon mesh geometry */
abstract class Mesh(name:String) extends Geometry(name) {
  import java.nio.IntBuffer
  import field.kit.util.BufferUtil
  
  var indices:IntBuffer = _
  var indexCount = 0
  
  override def allocate(capacity:Int) {
    super.allocate(capacity)
    indices = allocateIndices
  }
  
  protected def allocateIndices = BufferUtil.int(capacity)
  
  override def clear {
    super.clear
    indexCount = 0
    indices.clear
  }
}

/** Base class for all triangle based polygon meshes */
class TriMesh(name:String) extends Mesh(name) {
  import javax.media.opengl.GL
  
  def draw {
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    
    if(indexCount > 0) {
      gl.glDrawElements(GL.GL_TRIANGLES, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount)
    }
    
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    
    disableStates
  }
}

/** Base class for all quad based polygon meshes */
class QuadMesh(name:String) extends Mesh(name) {
  import javax.media.opengl.GL
  
  def draw {
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    
    if(indexCount > 0) {
      gl.glDrawElements(GL.GL_QUADS, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(GL.GL_QUADS, 0, vertexCount)
    }
    
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    
    disableStates
  }
}