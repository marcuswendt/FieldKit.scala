/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 13, 2009 */
package field.kit.test.particle

import field.kit.test.Sketch

/** 
 * testing the particle packing behaviour
 */
object PackedParticlesTest extends Sketch {
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  import field.kit.math.Common
  import field.kit.math.Common._
  import field.kit.math.geometry._
  
  val ps = new ParticleSystem
  val f = new Flock[Particle]
  ps += f
  f.emitter.rate = 1
  f.emitter.interval = 1
  f.emitter.max = 1000
  
  f.emitter += new Initialiser {
    lifeTime = 30000
    lifeTimeVariation = 0.25f
    acceleration = 2f
    velocity = 25f
    size = 10f
    sizeVariation = 1f
  }
  
  f.emitter += new Randomise
  
  f.emitter += new ColourInitialiser {
    this.colour := (1f, 0f, 0f, 1f)
    this.hueVariation = 0.5f
  }
  
  f += new BorderWrap2D()
  
  f += new ColourDirectionalForce {
    direction.h += 0.01f
    weight = 0.001f
  }
  
  f += new AttractorPoint {
    weight = -2f
    range = 0.1f
    override def prepare(dt:Float) {
      position.x = mouseX / width.toFloat
      position.y = mouseY / height.toFloat
      super.prepare(dt)
    }
  }
  
  f += new AttractorRandomPoint {
    weight = 0.1f
    range = 0.25f
  }

  // used as pack map  
  f += new ImageMapPacking {

    weight = 1f
    
    position.x = 0.25f
    position.y = 0.15f
    
    image = "res/test/flow.png"
    threshold = 0.5f
  }
  
//  val wind = new Wind
//  wind.weight = 0.25f
//  f += wind
  
  // toggles
  var showParticles = true
  val timer = new Timer

  // view rotation
  var xrot = Common.THIRD_PI
  var zrot = 0.1f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    ps.space = new QuadtreeSpace(width, height, 0)
    f.emitter.x = width/2f
    f.emitter.y = height/2f
    info("space width", ps.space.width, "height", ps.space.height, "depth", ps.space.depth)
  })
  
  var showDebug = false
  
  def render {
    import processing.core.PConstants._
    
    // update
    frameInfo
    
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

//    translate(width/2,height/2,0)
//    rotateX(xrot)
//    rotateZ(zrot)
//    translate(-width/2, -height/2,0)
    
    if(showParticles)
      drawParticles
    
    if(showDebug)
      drawDebug
    
    popMatrix
  }
  
  def drawParticles {
    noStroke
    f.particles foreach {p => 
      fill(p.colour.r * 255, p.colour.g * 255, p.colour.b * 255)
      rect(p.x, p.y, p.size, p.size)
    }
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
  
  val timerInfo = new Timer
  def frameInfo {
    val dt = timerInfo.update
    if(frameCount % 100 == 0) {
      val used = Common.round(Runtime.getRuntime.totalMemory / 1048576, 2)
      val free = Common.round(Runtime.getRuntime().freeMemory / 1048576, 2)
      info("frame", frameCount, "fps", 1000f/ dt ,"used", used, "free", free)
    }
  }
  
  override def keyPressed {
    key match {
      case 'p' => showParticles = !showParticles
      case 'd' => showDebug = !showDebug
      case ' ' => xrot = 0; zrot = 0
      case _ =>
    }
  }
}
