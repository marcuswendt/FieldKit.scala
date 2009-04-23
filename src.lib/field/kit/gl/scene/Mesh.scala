/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

object Mesh extends Enumeration {
  import javax.media.opengl.GL
  
  /** individual points */
  val POINTS = Value(GL.GL_POINTS)
  
  /** pairs of vertices interpreted as individual line segments */
  val LINES = Value(GL.GL_LINES)
  
  /** series of connected line segments */
  val LINE_STRIP = Value(GL.GL_LINE_STRIP)
  
  /** same as above, with a segment added between last and first vertices */
  val LINE_LOOP = Value(GL.GL_LINE_LOOP)
  
  /** triples of vertices interpreted as triangles */
  val TRIANGLES = Value(GL.GL_TRIANGLES)
  
  /** linked strip of triangles */
  val TRIANGLE_STRIP = Value(GL.GL_TRIANGLE_STRIP)
  
  /** linked fan of triangles */
  val TRIANGLE_FAN = Value(GL.GL_TRIANGLE_FAN)
  
  /** quadruples of vertices interpreted as four-sided polygons */
  val QUADS = Value(GL.GL_QUADS)
  
  /** linked strip of quadrilaterals */
  val QUAD_STRIP = Value(GL.GL_QUAD_STRIP)
  
  /** boundary of a simple, convex polygon */
  val POLYGON = Value(GL.GL_POLYGON)
}


/** Base class for all sorts of polygon mesh geometry */
abstract class Mesh(name:String) extends Geometry(name) {
  import java.nio.IntBuffer
  import field.kit.util.BufferUtil
  
  var indices:IntBuffer = _
  var indexCount = 0
  
  protected var geometryType = Mesh.TRIANGLES
    
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

import field.kit.gl.scene.transform._

/** Base class for all triangle based polygon meshes */
class TriMesh(name:String) extends Mesh(name) with Triangulator {
  import javax.media.opengl.GL
  
  geometryType = Mesh.TRIANGLES
  
  def triangulate:Unit = triangulate(vertexCount, vertices, indices)
  
  def draw {
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
    
    if(indexCount > 0) {
      gl.glDrawElements(geometryType.id, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(geometryType.id, 0, vertexCount)
    }
    
    disableStates
    
    gl.glDisableClientState(GL.GL_NORMAL_ARRAY)
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
  }
}


/** Base class for all quad based polygon meshes */
class QuadMesh(name:String) extends Mesh(name) {
  
  geometryType = Mesh.QUADS
  
  def draw {
    import javax.media.opengl.GL
    
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
    
    if(indexCount > 0) {
      gl.glDrawElements(geometryType.id, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(geometryType.id, 0, vertexCount)
    }
    
    disableStates
    
    gl.glDisableClientState(GL.GL_NORMAL_ARRAY)
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
  }
}


class QuadStripMesh(name:String) extends QuadMesh(name) {
   geometryType = Mesh.QUAD_STRIP
}