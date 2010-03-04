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

object MeshData {
  // disable vbo by default on linux
  var DEFAULT_USE_VBO = !System.getProperty("os.name").equals("Linux") 
}

/**
 * Stores all commonly used buffers of a Mesh
 */
class MeshData {
  import java.nio.IntBuffer
  import java.nio.FloatBuffer
  import scala.collection.mutable.ArrayBuffer
  
  import field.kit.util.Buffer
  import field.kit.gl.objects.VertexBuffer

  /** Number of vertices represented by this data. */
  var vertexCount = 0
  
  ///** Number of primitives represented by this data. */
  //var primitiveCount = Array(1)
  
  /** Buffer data */
  var vertices:FloatBuffer = _
  var normals:FloatBuffer = _
  var colours:FloatBuffer = _
  var textureCoords:ArrayBuffer[FloatBuffer] = _ 

  /** Index data */
  var indices:IntBuffer = _
  var indexLengths:Array[Int] = _
  var indexModes = Array(IndexMode.TRIANGLES)

  /** set true when any of the buffers data has changed */
  var needsRefresh = true
  
  // -- VBO support ------------------------------------------------------------  
  var vbo:VertexBuffer = _
  
  /** when true attempts to upload its data on every update into a vbo */
  var useVBO = MeshData.DEFAULT_USE_VBO

  /** interleaved data for use with VBO */
  var interleaved:FloatBuffer = _
  
  var vboUsage = VertexBuffer.Usage.STATIC_DRAW
  
  // -- Updating ---------------------------------------------------------------
  /**
   * Should be called whenever the vertex buffer data has changed 
   */
  def updateVertexCount {
	  vertexCount = if(vertices == null) 0 else vertices.limit / 3
  }
  
  /**
   * Should be called whenever the buffer data has changed
   */
  def refresh = {
	  updateVertexCount
	  needsRefresh = true
  }
  
  // -- Allocation -------------------------------------------------------------
  /**
   * Allocates a new vertex buffer when necessary
   * @return the current vertex buffer
   */
  def allocVertices(capacity:Int) = {
    vertices = Buffer.float(vertices, capacity * 3)
    
    // assumes the buffer is going to be filled with a static mesh
    updateVertexCount
    vertices
  }
  
  /** 
   * Allocates a new normal buffer when necessary
   * @return the current normal buffer
   */
  def allocNormals(capacity:Int) = {
    normals = Buffer.float(normals, capacity * 3)
    normals
  }
  
  /** 
   * Allocates a new colour buffer when necessary
   * @return the updated colour buffer
   */
  def allocColours(capacity:Int) = {
    colours = Buffer.float(colours, capacity * 4)
    colours
  }
  
  def allocColours:FloatBuffer =
    allocColours( if(vertices == null) 0 else vertices.capacity/3 )
  
  /** 
   * Allocates a new index buffer when necessary
   * @return the current index buffer
   */
  def allocIndices(capacity:Int) = {
    indices = Buffer.int(indices, capacity)
    indices
  }
  
  /** 
   * Allocates a new texture coordinate buffer when necessary
   * @return the texture coordinate buffer for this texture unit
   */
  def allocTextureCoords(textureUnit:Int, capacity:Int) = {
    if(textureCoords == null)
      textureCoords = new ArrayBuffer[FloatBuffer]
    
    val tcBuffer = if(textureUnit < textureCoords.size) textureCoords(textureUnit) else null 
    val buffer = Buffer.float(tcBuffer, capacity * 2)
    if(textureUnit >= textureCoords.size)
      textureCoords += buffer
    else
      textureCoords(textureUnit) = buffer
    buffer
  }
  
  def allocTextureCoords(capacity:Int):FloatBuffer = allocTextureCoords(0, capacity) 
  
  /** 
   * Allocates a new interleaved data buffer when necessary
   * @return the current interleaved data buffer
   */
  def allocInterleaved(capacity:Int) = {
    interleaved = Buffer.float(interleaved, capacity)
    interleaved
  }
}
