/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 26, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._
import field.kit.math.Common._
import field.kit.math._
import field.kit.util.Buffer

/**
 * Companion object to class <code>Box</code>
 */
object Box {
  val DEFAULT_USE_QUADS = true
  
  /** Creates a new default Box */
  def apply() = new Box("Box", Vec3(), 1f, 1f, 1f)
  
  def apply(extent:Float) = 
    new Box("Box", Vec3(), extent, extent, extent)
  
  def apply(name:String, extent:Float) = 
    new Box(name, Vec3(), extent, extent, extent)
}

/** 
 * Implements a 6-sided axis-aligned box
 * @author Marcus Wendt
 */
class Box(name:String, 
          var center:Vec3, 
          var extentX:Float, var extentY:Float, var extentZ:Float)
          extends Mesh(name) {
            
  var useQuads = Box.DEFAULT_USE_QUADS
  
  init(center, extentX, extentY, extentZ)
  
  /**
   * initializes the geometry data of this Box
   */
  def init(center:Vec3, extentX:Float, extentY:Float, extentZ:Float) {
    this.center := center
    this.extentX = extentX
    this.extentY = extentY
    this.extentZ = extentZ
    
    // -- Vertices -------------------------------------------------------------
    val vertices = data.allocVertices(24)
    val vd = computeVertices
    
    // back
    Buffer put (vd(0), vertices, 0)
    Buffer put (vd(1), vertices, 1)
    Buffer put (vd(2), vertices, 2)
    Buffer put (vd(3), vertices, 3)
    
    // right
    Buffer put (vd(1), vertices, 4)
    Buffer put (vd(4), vertices, 5)
    Buffer put (vd(6), vertices, 6)
    Buffer put (vd(2), vertices, 7)
    
    // front
    Buffer put (vd(4), vertices, 8)
    Buffer put (vd(5), vertices, 9)
    Buffer put (vd(7), vertices, 10)
    Buffer put (vd(6), vertices, 11)
    
    // left
    Buffer put (vd(5), vertices, 12)
    Buffer put (vd(0), vertices, 13)
    Buffer put (vd(3), vertices, 14)
    Buffer put (vd(7), vertices, 15)
    
    // top
    Buffer put (vd(2), vertices, 16)
    Buffer put (vd(6), vertices, 17)
    Buffer put (vd(7), vertices, 18)
    Buffer put (vd(3), vertices, 19)
    
    // bottom
    Buffer put (vd(0), vertices, 20)
    Buffer put (vd(5), vertices, 21)
    Buffer put (vd(4), vertices, 22)
    Buffer put (vd(1), vertices, 23)
    
    // -- Normals --------------------------------------------------------------
    val normals = data.allocNormals(24)
    
    // back
    for(i <- 0 until 4)
      normals put 0 put 0 put -1
    
    // right
    for(i <- 0 until 4)
      normals put 1 put 0 put 0
    
    // front
    for(i <- 0 until 4)
      normals put 0 put 0 put 1
    
    // left
    for(i <- 0 until 4)
      normals put -1 put 0 put 0
    
    // top
    for(i <- 0 until 4)
      normals put 0 put 1 put 0
    
    // bottom
    for(i <- 0 until 4)
      normals put 0 put -1 put 0
    
    // -- Texture Coordinates --------------------------------------------------
    val textureCoords = data.allocTextureCoords(24)
    
    for(i <- 0 until 6) {
      textureCoords put 1 put 0
      textureCoords put 0 put 0
      textureCoords put 0 put 1
      textureCoords put 1 put 1
    }
    
    // -- Indices --------------------------------------------------------------
    if(useQuads) {
      data.indexModes(0) = IndexMode.QUADS
      
    } else {
      val indices = data.allocIndices(36)
      indices put Array(2, 1, 0, 
                        3, 2, 0, 
                        6, 5, 4, 
                        7, 6, 4, 
                        10, 9, 8, 
                        11, 10, 8, 
                        14, 13, 12, 
                        15, 14, 12, 
                        18, 17, 16, 
                        19, 18, 16, 
                        22, 21, 20, 
                        23, 22, 20)
    }
  }
  
  /**
   * @return a size 8 array of Vectors representing the 8 points of the box.
   */
  protected def computeVertices = {
    val a = new Array[Vec3](8)
    a(0) = Vec3(center) += (-extentX, -extentY, -extentZ)
    a(1) = Vec3(center) += (extentX, -extentY, -extentZ)
    a(2) = Vec3(center) += (extentX, extentY, -extentZ)
    a(3) = Vec3(center) += (-extentX, extentY, -extentZ)
    a(4) = Vec3(center) += (extentX, -extentY, extentZ)
    a(5) = Vec3(center) += (-extentX, -extentY, extentZ)
    a(6) = Vec3(center) += (extentX, extentY, extentZ)
    a(7) = Vec3(center) += (-extentX, extentY, extentZ)
    a
  }
}