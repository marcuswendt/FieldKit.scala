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

class Quad(name:String, var _width:Float, var _height:Float) extends QuadMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.Buffer
  
  var mode = Quad.CENTER
  
  // -- Initialisation ---------------------------------------------------------
  vertexCount = 4
  vertices = Buffer.vertex(4)
  normals = Buffer.normal(4)
  coords = Buffer.coord(4)  

  coords put 0f put 0f
  coords put 1f put 0f
  coords put 1f put 1f
  coords put 0f put 1f
  coords.rewind
  
  resize(_width, _height)
  
  /** auxilliary constructor */
  def this(name:String) {
    this(name,1f,1f)
  }
           
  def resize(width:Float, height:Float) {
    this._width = width
    this._height = height
    
    val hw = width * 0.5f
    val hh = height * 0.5f
    vertices.clear
    mode match {
      case Quad.TOP_LEFT =>
        vertices put 0 put height put 0
        vertices put width put height put 0
        vertices put width put height put 0
        vertices put 0 put height put 0
      
      case Quad.CENTER => 
        vertices put -hw put hh put 0
        vertices put hw put hh put 0
        vertices put hw put -hh put 0
        vertices put -hw put -hh put 0
	  }
    vertices.rewind
  }
  
  // -- Getters/ Setters -------------------------------------------------------
  def width = _width
  def height = _height
}
