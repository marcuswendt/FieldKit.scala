/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._

/** a simple quadrilateral often used for billboards, shaders, etc */
object Quad extends Enumeration {
  val TOP_LEFT = Value
  val CENTER = Value
}

class Quad(name:String, mode:Quad.Value) extends TriMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.BufferUtil
  
  glGeometryMode = GL.GL_QUADS
  allocate(4)
  
  mode match {
  	case Quad.TOP_LEFT =>
  	  BufferUtil.put(vertices, 0.0f, 1.0f, 0.0f)
  	  BufferUtil.put(vertices, 1.0f, 1.0f, 0.0f)
  	  BufferUtil.put(vertices, 1.0f, 0.0f, 0.0f)
  	  BufferUtil.put(vertices, 0.0f, 0.0f, 0.0f)

  	case Quad.CENTER => 
  	  BufferUtil.put(vertices, -0.5f, 0.5f, 0.0f)
  	  BufferUtil.put(vertices, 0.5f, 0.5f, 0.0f)
  	  BufferUtil.put(vertices, 0.5f, -0.5f, 0.0f)
  	  BufferUtil.put(vertices, -0.5f, -0.5f, 0.0f)
  }
  vertices.rewind
  
  BufferUtil.put(texCoords, 0f, 0f)
  BufferUtil.put(texCoords, 1f, 0f)
  BufferUtil.put(texCoords, 1f, 1f)
  BufferUtil.put(texCoords, 0f, 1f)	
  texCoords.rewind
}
