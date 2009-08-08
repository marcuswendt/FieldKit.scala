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

import transform._

/** 
 * Base class for all sorts of polygon mesh geometry
 * 
 * To encourage encapsulation and for better readability additional functionality 
 * is added via Traits
 */
abstract class Mesh(name:String) extends Spatial(name) 
with RenderStateable with Triangulator {
  import javax.media.opengl.GL
  import java.nio.IntBuffer
  import java.nio.FloatBuffer
  
  import math.FMath._
  
  var geometryType = Mesh.TRIANGLES
  
  var colour = new Colour(Colour.WHITE)
  
  var vertices:FloatBuffer = _
  var normals:FloatBuffer = _
  var coords:FloatBuffer = _
  var colours:FloatBuffer = _
  var indices:IntBuffer = _
  
  /** the number of actual indices used */
  var indexCount = 0
  
  /** the number of actual vertices in the buffer */
  var vertexCount = 0
  
  def clear {
    vertexCount = 0   
    if(vertices != null) vertices.clear
    if(coords != null) coords.clear
    if(colours != null) colours.clear
    
    indexCount = 0
    indices.clear
  }
  
  def draw {
    if(vertices == null) {
      throw new Exception("Cannot draw object '"+ name +"' due to undefined vertices buffer")
      return
    }
    
    val normalsEnabled = normals != null
    val coordsEnabled = coords != null
    val coloursEnabled = colours != null
    val indicesEnabled = indices != null && indexCount > 0
    
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    if(coordsEnabled) gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    if(coloursEnabled) gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    if(normalsEnabled) gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
    
    enableStates

    if(coordsEnabled) gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    
    // set colour array or single solid colour
    if(coloursEnabled) 
      gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    else
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
    
    if(normalsEnabled) gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
    
    // draw the mesh
    if(indicesEnabled)
      gl.glDrawElements(geometryType.id, indexCount, GL.GL_UNSIGNED_INT, indices)
    else
      gl.glDrawArrays(geometryType.id, 0, vertexCount)
    
    disableStates
    
    if(normalsEnabled) gl.glDisableClientState(GL.GL_NORMAL_ARRAY)
    if(coloursEnabled) gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    if(coordsEnabled) gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
  }
  
  // -- Colours ----------------------------------------------------------------
  def solidColour(c:Colour) {
    colour := c
    if(colours!=null) {
      colours.clear
      for(i <- 0 until colours.capacity/4) {
        colours.put(c.r)
        colours.put(c.g)
        colours.put(c.b)
        colours.put(c.a)
      }
      colours.rewind
    }
  }
  
  def randomizeColours {
    colours.clear
    for(i <- 0 until colours.capacity/4) {
      colours.put(random)
      colours.put(random)
      colours.put(random)
      colours.put(1f)
    }
    colours.rewind
  }
  
  // -- Traits -----------------------------------------------------------------
  def triangulate:Unit = triangulate(vertexCount, vertices, indices)
}
