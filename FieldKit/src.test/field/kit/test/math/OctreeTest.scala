/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.test.math

/**
 * Test for the Octree geometry class
 * 
 * This is a direct port of Karsten Schmidts OctreeDemo
 * @see http://www.toxiclibs.org
 */
object OctreeTest extends test.Sketch {
  import processing.core.PConstants._
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
  
  // -- Fields -----------------------------------------------------------------
  // sphere clip radius
  val RADIUS = 20f

  // number of particles to add at once
  val NUM = 100

  // octree dimensions
  val DIM = 100f
  val DIM2 = DIM/2f

  // setup empty octree so that it's centered around the world origin
  val octree = new VisibleOctree(new Vec3(-DIM2,-DIM2,-DIM2), DIM)
  // add an initial particle at the origin
  octree insert new Vec3    
  
  // start with one particle
  var numParticles = 1
  
  // show octree debug info
  var showOctree = true
  
  // use clip sphere or axis aligned bounding box
  var useSphere = false
  
  val pointer = new Vec3
  
  // -- Init -------------------------------------------------------------------
  init {}

  // -- Render -----------------------------------------------------------------
  def render {
    background(255)
    
    pushMatrix
    lights
    translate(width/2,height/2,0)
    scale(4)
    
    // show debug view of tree
    if (showOctree) octree.draw
    
    // show crosshair 3D cursor
    stroke(255,0,0)
    noFill
    beginShape(LINES)
    vertex(pointer.x,-DIM2,0)
    vertex(pointer.x,DIM2,0)
    vertex(-DIM2,pointer.y,0)
    vertex(DIM2,pointer.y,0)
    endShape
    popMatrix
    
    if(useSphere)
      octree(new Sphere(pointer, RADIUS))
    else
      octree(new AABB(pointer, RADIUS))
    
    match {
      case null =>
        
      case _ =>
    }
  }
  
  override def keyPressed {
    key match {
      case ' ' =>
        info("adding", NUM, "particles")
        // add NUM new particles within a sphere of radius DIM2
        for(i <- 0 until NUM)
           octree insert (Vec3.random *= random(DIM2))
        numParticles += NUM
        
      case 'o' => showOctree = !showOctree
      case 's' => useSphere = !useSphere
      case _ =>
    }
  }
}
