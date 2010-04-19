/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._
import field.kit.math._

/** 
 * Companion object to class <code>Quad</code>
 */
object Quad extends Enumeration {
  val TOP_LEFT = Value
  
  val CENTER = Value
  
  /** true when new Quadliterals should be constructed from quads instead of triangles */
  val DEFAULT_USE_QUADS = true
  
  /** Creates a new default <code>Quad</code> */
  def apply() = 
    new Quad("Quad", CENTER, 1f, 1f)
  
  def apply(width:Float, height:Float) = 
    new Quad("Quad", CENTER, width, height)
  
  def apply(mode:Quad.Value, width:Float, height:Float) = 
    new Quad("Quad", mode, width, height)
  
  def apply(name:String, width:Float, height:Float) = 
    new Quad(name, CENTER, width, height)
}


/** 
 * A quadliteral mesh, often used for billboards, shaders, etc
 */
class Quad(name:String, mode:Quad.Value,  protected var _width:Float, protected var _height:Float)
extends Mesh(name) {
  import field.kit.util.Buffer
  
  // TODO could make a more convenient method for that
  var useQuads = Quad.DEFAULT_USE_QUADS
  
  init(mode, width, height)
  
  def this(name:String) {
	  this(name, Quad.CENTER, 1f, 1f)
  }
  
  /**
   * initializes the geometry data of this Quad
   */
  def init(_mode:Quad.Value, width:Float, height:Float) {
    this._width = width
    this._height = height
    
    // -- Vertices -------------------------------------------------------------
    val vertices = data.allocVertices(4)
    vertices.clear
    
    mode match {
      case Quad.TOP_LEFT =>
        vertices put 0 put height put 0
        vertices put width put height put 0
        vertices put width put 0 put 0
        vertices put 0 put 0 put 0
      
      case Quad.CENTER =>
        val hw = width * 0.5f
        val hh = height * 0.5f
        
        vertices put -hw put hh put 0
        vertices put hw put hh put 0
        vertices put hw put -hh put 0
        vertices put -hw put -hh put 0
	  }
    
    // -- Texture Coordinates --------------------------------------------------
    val textureCoords = data.allocTextureCoords(4)
    textureCoords.clear
    
    textureCoords put 0f put 0f
    textureCoords put 1f put 0f
    textureCoords put 1f put 1f
    textureCoords put 0f put 1f

    // -- Normals --------------------------------------------------------------
    val normals = data.allocNormals(4)
    normals.clear
    
    for(i <- 0 until 4)
      normals put 0 put 0 put 1
    
    // -- Indices --------------------------------------------------------------
    if(useQuads) {
      data.indexModes(0) = IndexMode.QUADS
    } else {
      val indices = data.allocIndices(6)
      indices.clear
      
      indices put Array(0, 1, 2, 0, 2, 3) 
    }
  }
  
  // -- Getters ----------------------------------------------------------------
  def width = _width
  def height = _height
}
