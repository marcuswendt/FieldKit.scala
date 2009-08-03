/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 11, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene.TriMesh
import field.kit._

/** 
 * implements a planeoid mesh with variable subdivisions along its two axes
 * @author Marcus Wendt
 */
class Plane(name:String) extends TriMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.BufferUtil
  import field.kit.math.FMath
  
  var width:Float = _
  var height:Float = _
  var rows:Int = _
  var cols:Int = _
 
  // init defaults
  init(2,2)
  resize(1f, 1f)
  
  /** auxilliary constructor */
  def this(name:String, _width:Float, _height:Float, _cols:Int, _rows:Int) = {
    this(name)
    init(_cols, _rows)
    resize(_width, _height)
  }
  
  /** auxilliary constructor */
  def this(name:String, _cols:Int, _rows:Int) = {
    this(name)
    init(_cols, _rows)
  }
  
  /** auxilliary constructor */
  def this(name:String, _width:Float, _height:Float) = {
    this(name)
    resize(_width, _height)
  }
  
  /** initializes the buffers for the given number of rows and cols */
  def init(_cols:Int, _rows:Int) {
    def index(x:Int, y:Int) = y * rows + x
    
	this.cols = FMath.max(1, _cols)
    this.rows = FMath.max(1, _rows)
    this.indexCount = (cols-1) * (rows-1) * 2 * 3
    this.vertexCount = rows * cols

	allocate(cols * rows)
 
	// statically initialize shared vertices
	// adds two triangles per cell 
    indices.clear
	for(y <- 0 until (cols-1)) {
	  for(x <- 0 until (rows-1)) {
	    // triangle a
	    BufferUtil.put(indices, index(x, y))
	    BufferUtil.put(indices, index(x + 1, y + 1))
        BufferUtil.put(indices, index(x, y + 1))
        
        // triangle b
	    BufferUtil.put(indices, index(x, y))
	    BufferUtil.put(indices, index(x + 1, y + 1))
        BufferUtil.put(indices, index(x + 1, y))
	  }
	}
	indices.rewind
 
	// reinit vertices
	if(width != 0 && height != 0) resize(width, height)
  }
  
  override def allocateIndices = BufferUtil.int(indexCount)
  
  /** sets the width and height and recalculates the vertex and texture coordinates
   * TODO could add a plane parameter to determine which axes are meant
   */
  def resize(_width:Float, _height:Float) {
    this.width = _width
    this.height = _height
    
    vertices.clear
    coords.clear
    
    for(y <- 0 until cols) {
      for(x <- 0 until rows) {
        val u = (x / (rows.asInstanceOf[Float]-1)) * width
        val v = (y / (cols.asInstanceOf[Float]-1)) * height
        
        BufferUtil.put(vertices, u, v, 0.0f)
        BufferUtil.put(coords, u, v)
      }
    }
    
    vertices.rewind
    coords.rewind
  }
  
  def update {
    import field.kit.math.Vec3
    val cur = new Vec3
    val left = new Vec3
    val up = new Vec3
    
    val v1 = new Vec3
    val v2 = new Vec3
    
    normals.clear
    def index(x:Int, y:Int) = y * rows + x
      
    for(y <- 0 until cols) {
      for(x <- 0 until rows) {
        cur := (vertices, index(x,y))
        left := (vertices, index(if(x+1 == rows) x-1 else x+1, y))
        up := (vertices, index(x, if(y+1 == cols) y-1 else y+1))
        
        v1 := up -= cur
        v2 := left -= cur
        v1 cross v2
        
        normals put v1.x
        normals put v1.y
        normals put v1.z
      }
    }
    normals.rewind
  }
}