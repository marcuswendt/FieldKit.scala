/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
  import kit.math.Common._
  import kit.util.datatype.collection.ArrayBuffer

  class VisibleOctree(offset:Vec3, size:Float) extends Octree(null, offset, Vec3(size)) {
    def draw = drawNode(this)
    
    def drawNode(n:Octree) {
      if(n.numChildren > 0) {
        noFill
        stroke(n.treeDepth, 20)
        pushMatrix
        translate(n.x, n.y, n.z)
        box(n.size.x, n.size.y, n.size.z)
        popMatrix
        
        for(i <- 0 until 8) {
          val child = n.children(i)
          if(child != null)
            drawNode(child)
        }
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
  
  // view rotation
  var xrot = Common.THIRD_PI
  var zrot = 0.1f
  
  var points = new ArrayBuffer[Vec3]
  
  // -- Init -------------------------------------------------------------------
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})

  // -- Render -----------------------------------------------------------------
  def render {
    // -- update ---------------------------------------------------------------
    // rotate view on mouse drag
    if (mousePressed) {
      xrot += (mouseY*0.01f-xrot)*0.1f
      zrot += (mouseX*0.01f-zrot)*0.1f
      
    // or move cursor
    } else {
      pointer.x = -(width*0.5f-mouseX)/(width/2)*DIM2
      pointer.y = -(height*0.5f-mouseY)/(height/2)*DIM2
    }
  
    // -- render ---------------------------------------------------------------
    background(255)
    
    pushMatrix
    lights
    translate(width/2,height/2,0)
    rotateX(xrot)
    rotateZ(zrot)
    scale(4)
  
    // show debug view of tree
    if (showOctree) octree.draw
    
    // show crosshair 3D cursor
    drawCursor
    
    // show selected points
    drawPoints
    
    // show clipping sphere
    drawSphere
    
    //text("total: "+numParticles,10,30);
    //text("clipped: "+numClipped+" (time: "+dt+"ms)",10,50);
    popMatrix
  }
  
  def drawCursor {
    stroke(255,0,0)
    noFill
    beginShape(LINES)
    vertex(pointer.x,-DIM2,0)
    vertex(pointer.x,DIM2,0)
    vertex(-DIM2,pointer.y,0)
    vertex(DIM2,pointer.y,0)
    endShape
    noStroke
  }
  
  def drawPoints {
    points.clear
    
    if(useSphere)
      octree(new Sphere(pointer, RADIUS), points)
    else
      octree(new AABB(pointer, RADIUS), points)
    
    points foreach { p =>
      pushMatrix
      translate(p.x,p.y,p.z)
      fill(abs(p.x)*8, abs(p.y)*8, abs(p.z)*8)
      box(2)
      popMatrix
    }
  }
  
  
  def drawSphere {
    fill(0,30)
    pushMatrix
    translate(pointer.x,pointer.y,0)
    sphere(RADIUS)
    popMatrix
  }
  
  override def keyPressed {
    key match {
      case ' ' =>
        // add NUM new particles within a sphere of radius DIM2
        val v = Vec3.random *= random(DIM2)
        val insertNum = random(NUM).asInstanceOf[Int]
        for(i <- 0 until insertNum)
           octree insert v
        
        numParticles += insertNum
        
        info("added", insertNum, "particles => total:", numParticles)
      case 't' =>
        var v = new Vec3(-75f, -25f, -50f)
        octree insert v
        numParticles += 1
        
      case 'o' => showOctree = !showOctree
      case 's' => useSphere = !useSphere
      case _ =>
    }
  }
}
