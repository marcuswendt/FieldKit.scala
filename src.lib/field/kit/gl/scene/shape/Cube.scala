/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._

/** 
 * implements a cubic mesh with variable subdivisions along its three axes
 * 
 * @author Marcus Wendt
 */
/*
class Cube(name:String) extends TriMesh(name) {
  import javax.media.opengl.GL
  import field.kit.util.BufferUtil
  import field.kit.math.FMath
  
  var width:Float = _
  var height:Float = _
  var depth:Float = _
  
  var rows:Int = _
  var cols:Int = _
  var levels:Int = _
  
  /** auxilliary constructor */
  def this(name:String, _cols:Int, _rows:Int, _levels:Int) = {
    this(name)
    init(_cols, _rows, _levels)
    resize(1f,1f,1f)
  }
  
  /** auxilliary constructor */
  def this(name:String, _cols:Int, _rows:Int, _levels:Int, _width:Float, _height:Float, _depth:Float) = {
    this(name)
    init(_cols, _rows, _levels)
    resize(_width,_height,_depth)
  }
  
  /** initializes the buffers for the given number of rows and cols */
  def init(_cols:Int, _rows:Int, _levels:Int) {
    this.cols = FMath.max(1, _cols)
    this.rows = FMath.max(1, _rows)
    this.levels = FMath.max(1, _levels)

    info("cols", cols, "rows", rows, "levels", levels)
    
    val colsi = cols-1
    val rowsi = rows-1
    val levelsi = levels-1
    
    val topV = levels * cols
    val topI = levelsi * rowsi * 6
    
    val bottomV = levels * cols
    val bottomI = levelsi * rowsi * 6
    
    val frontV = cols * rows
    val frontI = colsi * rowsi * 6
    
    val backV = 0
    val leftV = 0
    val rightV = 0
    
    this.indexCount = topI + frontI
    
    this.vertexCount = topV + frontV + bottomV 
    allocate(vertexCount)
    
    info("vertexCount", vertexCount, "indexCount", indexCount)
    
    // helpers
    def triangle(x:Int, y:Int, z:Int) {
      indices put x
      indices put y
      indices put z
      
      info("tri", x, y, z)
    }
    
    // top
    def indexTop(x:Int, z:Int) = z * rows + x  
    for(x <- 0 until rowsi) {
      for(z <- 0 until levelsi) {
        triangle( indexTop(x, z), 
                  indexTop(x+1, z+1), 
                  indexTop(x, z+1) )
        
        triangle( indexTop(x, z), 
                  indexTop(x+1, z+1), 
                  indexTop(x+1, z) )
      }
    }
    
    info("indices top ", indices.position)
    var offset = rows * levelsi

    // front
    def indexFront(x:Int, y:Int) = {
      var offset = rowsi * levelsi
      offset + y * cols + x
    }

    for(x <- 0 until rowsi) {
      var y = 0
      var offset1 = rows * levelsi
      var offset2 = rows * levels
      
      triangle( offset1 + y * cols + x,
    		  	offset2 + (y+1) * cols + (x+1), 
    		  	offset2 + (y+1) * cols + x )
        
//      triangle( offset1 + y * cols + x,
//    		  	offset1 + (y+1) * cols + (x+1), 
//    		  	offset1 + y * cols + (x+1) )
    }
    
//    for(y <- 1 until colsi-1) {
//      for(x <- 0 until rowsi) {
//        triangle( indexFront(x, y), 
//                  indexFront(x+1, y+1), 
//                  indexFront(x, y+1) )
//        
//        triangle( indexFront(x, y), 
//                  indexFront(x+1, y+1), 
//                  indexFront(x+1, y) )
//      }
//    }
    
    info("indices front ", indices.position)
    
     /*
    // bottom
    
    
    def indexBottom(x:Int, z:Int) = topBottomCount + (z * rows + x)
    for(z <- 0 until levelsi) {
      for(x <- 0 until rowsi) {
        triangle( indexBottom(x, z), 
                  indexBottom(x+1, z+1), 
                  indexBottom(x, z+1) )
        
        triangle( indexBottom(x, z), 
                  indexBottom(x+1, z+1), 
                  indexBottom(x+1, z) )
      }
    }
  
    
   
    def index(x:Int, y:Int) = y * rows + x
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
    */
    
    indices.rewind
  }
  
  override def allocateIndices = BufferUtil.int(indexCount)
  
  def resize(_width:Float, _height:Float, _depth:Float) {
    this.width = _width
    this.height = _height
    this.depth = _depth
    
    val hwidth = width * .5f
    val hheight = height * .5f
    val hhdepth = depth * .5f
    
    val colsf = cols.asInstanceOf[Float] - 1
    val rowsf = rows.asInstanceOf[Float] - 1
    val levelsf = levels.asInstanceOf[Float] - 1
    
    vertices.clear
    coords.clear
    
    // helpers
    def vertex(x:Float, y:Float, z:Float) {
      vertices put x
      vertices put y
      vertices put z
    }
    def tc(u:Float, v:Float) {
      coords put u
      coords put v
    }
    
    // bottom
    for(z <- 0 until levels) {
      for(x <- 0 until rows) {
        val u = (x / rowsf) * width - hwidth
        val v = -hheight
        val w = (z / levelsf) * depth - hhdepth
        vertex(u, v, w)
        tc(u, w)
      }
    }
    
    /*
    // top
    for(z <- 0 until levels) {
      for(x <- 0 until rows) {
        val u = (x / rowsf) * width - hwidth
        val v = hheight
        val w = (z / levelsf) * depth - hhdepth
        vertex(u, v, w)
        tc(u, w)
      }
    }
    info("top vtx", vertices.position)
    */
    
    // front
    for(y <- 1 until cols-1) {
      for(x <- 0 until rows) {
        val u = (x / rowsf) * width - hwidth
        val v = (y / colsf) * height - hheight
        val w = hhdepth
        vertex(u, v, w)
        tc(u, v)
      }
    }
    info("front vtx", vertices.position)
    
   
    info("bottom vtx", vertices.position)
    
    /*
    
    
    // back
    for(y <- 0 until cols) {
      for(x <- 0 until rows) {
        val u = (x / rowsf) * width - hwidth
        val v = (y / colsf) * height - hheight
        val w = -hhdepth
        vertex(u, v, w)
        tc(u, v)
      }
    }
    
    // left
    for(y <- 0 until cols) {
      for(z <- 0 until levels) {
        val u = -hwidth
        val v = (y / colsf) * height - hheight
        val w = (z / levelsf) * depth - hhdepth
        vertex(u, v, w)
        tc(u, v)
      }
    }
    
    // right
    for(y <- 0 until cols) {
      for(z <- 0 until levels) {
        val u = hwidth
        val v = (y / colsf) * height - hheight
        val w = (z / levelsf) * depth - hhdepth
        vertex(u, v, w)
        tc(v, w)
      }
    }
    */
    
    info("vertices put", vertices.position)
    vertices.rewind
    coords.rewind
  }	
  
  override def draw {
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, coords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    
    
    
    if(indexCount > 0) {
      geometryType = Mesh.TRIANGLES
      gl.glDrawElements(geometryType.id, indexCount, GL.GL_UNSIGNED_INT, indices)
    } else {
    //gl.glPointSize(4f)
//	geometryType = Mesh.POINTS
//	gl.glDrawArrays(geometryType.id, 0, vertexCount)
    }
    
    gl.glPointSize(4f)
    gl.glDrawArrays(Mesh.POINTS.id, 0, vertexCount)
    
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    
    disableStates
  }
}
*/