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

class Quad(name:String, width:Float, height:Float) extends QuadMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.BufferUtil
  
  var mode = Quad.CENTER
  allocate(4)
  vertexCount = 4
  
  BufferUtil.put(coords, 0f, 0f)
  BufferUtil.put(coords, 1f, 0f)
  BufferUtil.put(coords, 1f, 1f)
  BufferUtil.put(coords, 0f, 1f)	
  coords.rewind
  
  resize(width, height)
  
  def resize(width:Float, height:Float) {
    val hw = width * 0.5f
    val hh = height * 0.5f
    mode match {
      case Quad.TOP_LEFT =>
        BufferUtil.put(vertices, 0, height, 0)
        BufferUtil.put(vertices, width, height, 0)
        BufferUtil.put(vertices, width, 0, 0)
        BufferUtil.put(vertices, 0, 0, 0)
      
      case Quad.CENTER => 
        BufferUtil.put(vertices, -hw, hh, 0)
        BufferUtil.put(vertices, hw, hh, 0)
        BufferUtil.put(vertices, hw, -hh, 0)
        BufferUtil.put(vertices, -hw, -hh, 0)
	  }
	  vertices.rewind
  }
}
