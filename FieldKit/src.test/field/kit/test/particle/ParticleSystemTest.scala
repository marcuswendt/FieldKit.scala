/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 08, 2009 */
package field.kit.test.particle

/** 
 * quick test for the particle system
 */
object ParticleSystemTest extends test.Sketch {
  import kit.particle._
  import kit.particle.behaviour._
  import kit.util.Timer
  import kit.math.Common
  import kit.math.geometry._
  
  val ps = new ParticleSystem
  val f = new Flock[Particle]
  f.emitter.rate = 1
  f.emitter.interval = 10
  f.emitter.max = 20
  
  ps += f
//  f += new Wind
  f += new Wrap2D
  
  f.emitter += new Randomize
  
//  f += new Behaviour {
//    logName = "perlin"
//    var time = 0f
//    var tmp = 0f
//    
//    override def prepare(dt:Float) = time += dt
//    
//    def apply(p:Particle, dt:Float) {
//      val weight = 10f
//      p.steer.x += (noise(tmp, p.age) * 2f - 1f) * weight
//      p.steer.y += (noise(time, p.age) * 2f - 1f) * weight
//      tmp += dt
//    }
//  }
  
  f.emitter += new Behaviour {
    logName = "perlin"
    var time = 0f
    var tmp = 0f
    
    override def prepare(dt:Float) = time += dt
    
    def apply(p:Particle, dt:Float) {
      val weight = 100f
      p.steer.x += (noise(tmp, p.age) * 2f - 1f) * weight
      p.steer.y += (noise(time, p.age) * 2f - 1f) * weight
      tmp += dt
    }
  } 
  
  val repel = new Repel
  repel.range = 200f
  f += repel
  
  val timer = new Timer
  
  // view rotation
  var xrot = Common.THIRD_PI
  var zrot = 0.1f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
//    ps.space = new OctreeSpace(width, height, 500)
    ps.space = new QuadtreeSpace(width, height, height)
    f.emitter := ps.space
    info("space width", ps.space.width, "height", ps.space.height, "depth", ps.space.depth)
  })
  
  var showDebug = false
  
  def render {
    import processing.core.PConstants._
    
    // update
    if (mousePressed) {
      xrot += (mouseY*0.01f-xrot)*0.1f
      zrot += (mouseX*0.01f-zrot)*0.1f
    }
    
    val dt = timer.update 
    ps.update(dt)
    
    // render
    background(64)
    rectMode(CENTER)
    noStroke
    fill(255)
    
    pushMatrix
    lights
    translate(width/2,height/2,0)
    rotateX(xrot)
    rotateZ(zrot)
    translate(-width/2, -height/2,0)
    
    f.particles foreach {p => 
      noFill
      stroke(255)
      ellipse(p.x, p.y, repel.range, repel.range)
      
      fill(255)
      noStroke
      rect(p.x, p.y, 3, 3)
    }
    
    if(showDebug)
      drawDebug
    
    popMatrix
  }
  
  def drawDebug {
    ps.space match {
      case os:OctreeSpace =>
        drawOctreeNode(os.tree)
        
      case qs:QuadtreeSpace =>
        drawQuadtreeNode(qs.tree)
    }
  }
  
  def drawQuadtreeNode(n:Quadtree) {
    if(n.numChildren > 0) {
      noFill
      stroke(255 - n.depth, 128)
      rect(n.x, n.y, n.size.x, n.size.y)
//      pushMatrix
//      translate(n.x, n.y, n.z)
//      box(n.size.x, n.size.y, n.size.z)
//      popMatrix
      
      for(i <- 0 until 4) {
        val child = n.children(i)
        if(child != null)
          drawQuadtreeNode(child)
      }
    }
  }
  
  def drawOctreeNode(n:Octree) {
    if(n.numChildren > 0) {
      noFill
      stroke(255 - n.depth, 20)
      pushMatrix
      translate(n.x, n.y, n.z)
      box(n.size.x, n.size.y, n.size.z)
      popMatrix
      
      for(i <- 0 until 8) {
        val child = n.children(i)
        if(child != null)
          drawOctreeNode(child)
      }
    }
  }
  
  override def keyPressed {
    key match {
      case 'd' => showDebug = !showDebug
      case ' ' => xrot = 0; zrot = 0
      case _ =>
    }
  }
}
