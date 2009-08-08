/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 08, 2009 */
package field.kit.gl.scene

object IndexMode extends Enumeration {
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


/**
 * Stores all commonly used buffers of a Mesh
 */
class MeshData {
  import java.nio.IntBuffer
  import java.nio.FloatBuffer

  import util.datatype.collection.ArrayBuffer
  import util.Buffer
  
  ///** Number of vertices represented by this data. */
  var vertexCount = 0

  ///** Number of primitives represented by this data. */
  //var primitiveCount = Array(1)
  
  /** Buffer data */
  var vertices:FloatBuffer = _
  var normals:FloatBuffer = _
  var colours:FloatBuffer = _
  var textureCoords = new ArrayBuffer[FloatBuffer]()

  /** Index data */
  var indices:IntBuffer = _
  var indexLengths:Array[Int] = _
  var indexModes = Array(IndexMode.LINES)
  
  
  // -- Updating ---------------------------------------------------------------
  
  /**
   * Should be called whenever the vertex buffer data has changed 
   */
  def updateVertices:Unit = {
    if(vertices == null) return
    vertexCount = vertices.position / 3
    vertices.rewind
  }
  
  /**
   * Should be called whenever the normal buffer data has changed 
   */
  def updateNormals:Unit = {
    if(normals == null) return
    normals.rewind
  }
  
  /**
   * Should be called whenever the texture coord buffer data has changed 
   */
  def updateTextureCoords:Unit = {
    if(textureCoords == null) return
    textureCoords(0).rewind
  }
  
  /**
   * Should be called whenever the index buffer data has changed 
   */
  def updateIndices:Unit = {
    if(indices == null) return
    indices.rewind
  }
  
  // -- Allocation -------------------------------------------------------------
  /**
   * Allocates a new vertex buffer when necessary
   * @return the current vertex buffer
   */
  def allocVertices(capacity:Int) = {
    vertices = allocateFloatBuffer(vertices, capacity * 3)
    vertices
  }
  
  /** 
   * Allocates a new normal buffer when necessary
   * @return the current normal buffer
   */
  def allocNormals(capacity:Int) = {
    normals = allocateFloatBuffer(normals, capacity * 3)
    normals
  }
  
  /** 
   * Allocates a new colour buffer when necessary
   * @return the updated colour buffer
   */
  def allocColours(capacity:Int) = {
    colours = allocateFloatBuffer(colours, capacity * 4)
    colours
  }
  
  /** 
   * Allocates a new index buffer when necessary
   * @return the current index buffer
   */
  def allocIndices(capacity:Int) = {
    indices = allocateIntBuffer(indices, capacity)
    indices
  }
  
  /** 
   * Allocates a new texture coordinate buffer when necessary
   * @return the texture coordinate buffer for this texture unit
   */
  def allocTextureCoords(textureUnit:Int, capacity:Int) = {
    val buffer = allocateFloatBuffer(textureCoords(textureUnit), capacity * 2)
    if(textureUnit >= textureCoords.size)
      textureCoords += buffer
    else
      textureCoords(textureUnit) = buffer
    buffer
  }
  
  def allocTextureCoords(capacity:Int):FloatBuffer = allocTextureCoords(0, capacity) 
  
  // ---------------------------------------------------------------------------
//  def clear {
//    vertexCount = 0
//  }
  
  // -- Utilities --------------------------------------------------------------
  /**
   * @return a new FloatBuffer if the given ones capacity wasn sufficient
   */
  private def allocateFloatBuffer(buffer:FloatBuffer, capacity:Int):FloatBuffer = {
    if(buffer != null) {
      if(buffer.capacity <= capacity) {
        buffer.rewind
        return buffer
      }
    }
    Buffer.float(capacity)
  }
  
  /**
   * @return a new FloatBuffer if the given ones capacity wasn sufficient
   */
  private def allocateIntBuffer(buffer:IntBuffer, capacity:Int):IntBuffer = {
    if(buffer != null) {
      if(buffer.capacity <= capacity) {
        buffer.rewind
        return buffer
      }
    }
    Buffer.int(capacity)
  }
}
