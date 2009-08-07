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
abstract class Mesh(name:String, val geometryType:Mesh.Value) extends Geometry(name) {
  import javax.media.opengl.GL
  import java.nio.IntBuffer
  
  var indices:IntBuffer = _
  var indexCount = 0
  
  override def clear {
    super.clear
    indexCount = 0
    indices.clear
  }
  
  def draw {
    val coloursEnabled = colours != null
    val normalsEnabled = normals != null
    
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    if(coloursEnabled) gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    if(normalsEnabled) gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    
    // set colour array or single solid colour
    if(coloursEnabled) 
      gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    else
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
    
    if(normalsEnabled) gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
    
    // draw the mesh
    if(indexCount > 0) {
      gl.glDrawElements(geometryType.id, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(geometryType.id, 0, vertexCount)
    }
    
    disableStates
    
    if(normalsEnabled) gl.glDisableClientState(GL.GL_NORMAL_ARRAY)
    if(coloursEnabled) gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
  }
}

import field.kit.gl.scene.transform._

/** Base class for all triangle based polygon meshes */
abstract class TriMesh(name:String) extends Mesh(name, Mesh.TRIANGLES) with Triangulator {
  def triangulate:Unit = triangulate(vertexCount, vertices, indices)
}

/** Base class for all quad based polygon meshes */
abstract class QuadMesh(name:String) extends Mesh(name, Mesh.QUADS) {}

abstract class QuadStripMesh(name:String) extends Mesh(name, Mesh.QUAD_STRIP) {}