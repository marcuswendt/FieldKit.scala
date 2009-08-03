/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.test.math.geometry

/**
 * Test for the Octree geometry class
 */
object OctreeTest extends test.Sketch {
  import kit.math.geometry._
  import kit.math._
  
  class VisibleOctree(offset:Vec3, size:Float) extends Octree(offset, size) {
    def draw = drawNode(this)
    
    def drawNode(n:Octree) {
      if(n.numChildren > 0) {
        noFill
        stroke(n.depth, 20)
        pushMatrix
        translate(n.x, n.y, n.z)
        box(n.size)
        popMatrix
      }
    }
  }
  
  init {}
  
  def render {
    background(255)
  }
}
