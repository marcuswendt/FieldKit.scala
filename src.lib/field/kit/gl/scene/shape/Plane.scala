/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 11, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._
import field.kit._

/** implements a planeoid mesh with variable subdivisions along its two axes */
class Plane(name:String) extends TriMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.BufferUtil
  
  var width:Float = _
  var height:Float = _
  var rows:Int = _
  var cols:Int = _
  
  //glGeometryMode = GL.GL_TRIANGLES
  glGeometryMode = GL.GL_QUADS
 
  // init defaults
  init(2,2)
  resize(1f, 1f)
  
  /** auxilliary constructor */
  def this(name:String, _width:Float, _height:Float, _rows:Int, _cols:Int) = {
    this(name)
    init(_rows, _cols)
    resize(_width, _height)
  }
  
  /** auxilliary constructor */
  def this(name:String, _rows:Int, _cols:Int) = {
    this(name)
    init(_rows, _cols)
  }
  
  /** auxilliary constructor */
  def this(name:String, _width:Float, _height:Float) = {
    this(name)
    resize(_width, _height)
  }
  
  /** initializes the buffers for the given number of rows and cols */
  def init(_rows:Int, _cols:Int) {
	this.rows = _rows
	this.cols = _cols
	allocate(rows * cols)
  }
  
  /** sets the width and height and recalculates the vertex and texture coordinates
   * TODO could add a plane parameter to determine which axes are meant
   */
  def resize(_width:Float, _height:Float) {
    this.width = _width
    this.height = _height
    
    println("resize w"+ width +" h"+ height +" rows "+ rows +" cols "+ cols)
    
    vertices.rewind
    texCoords.rewind
    
    for(y <- 0 until cols) {
      for(x <- 0 until rows) {
        val u = (x / (rows.asInstanceOf[Float]-1)) * width
        val v = (y / (cols.asInstanceOf[Float]-1)) * height
        
        println("x "+ x +" y "+ y +" u "+ u +" v "+ v)
        
        BufferUtil.put(vertices, u, v, 0.0f)
        BufferUtil.put(texCoords, u, v)
      }
    }
    
    vertices.rewind
    texCoords.rewind
  }
  
  /** TODO could do a boundary check here */
  def index(x:Int, y:Int) = y * rows + x
}