/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 05, 2009 */
package field.kit.test.particle

import field.kit.test.Sketch

/** 
 * tests two flocks repelling from each other
 */
object OtherFlockRepelTest extends Sketch {
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  import field.kit.math.Common
  import field.kit.math.geometry._
  
  val PARALLEL = true
  
  var ps:ParticleSystem = _
  
  if(!PARALLEL) {
    ps = new ParticleSystem
    
  } else {
    ps = new ParallelParticleSystem(2) 
  }
  
  // -- basic flock ------------------------------------------------------------
  val f = new Flock[Particle]
  ps += f
  f.emitter.rate = 100
  f.emitter.interval = 1
  f.emitter.max = 2000
  
  
  val initialiser = new Initialiser
  initialiser.lifeTime = 4000
  initialiser.isPerpetual = false
  initialiser.acceleration = 20f
  initialiser.accelerationVariation = 0.5f
  initialiser.velocity = 20f
  initialiser.velocityVariation = 0.25f
  
  f.emitter += initialiser
  f.emitter += new Randomise {
    size.x = 0.25f
  }
  
  f += new Wind
  f += new BorderWrap2D
  val repel = new FlockRepel
  
  // makes particles bounce of all other flocks particles
  val repelMouse = new FlockRepel {
    isAppliedToOwnFlock = false
    range = 0.05f
    weight = 20
  }
  f += repelMouse
  
  // -- mouse flock ------------------------------------------------------------
  val mf = new Flock[Particle]
  ps += mf
  mf.emitter.max = 1
  
  mf += new Behaviour {
    def apply(p:Particle, dt:Float) {
      p.x = mouseX
      p.y = mouseY
      p.z = 0
      p.size = repelMouse.rangeAbs
    }
  }
  
  // -- misc -------------------------------------------------------------------
  val timer = new Timer
  
  // toggles
  var showParticles = true
  
  // view rotation
  var xrot = Common.THIRD_PI
  var zrot = 0.1f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    ps.space = new QuadtreeSpace(width, height, height)
    f.emitter := ps.space
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
    
//    if(!PARALLEL) {
	if(frameCount > 10) {
	  ps.update(timer.update)
	}
    
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
    
    if(showParticles)
      drawParticles
    
    if(showDebug)
      drawDebug
    
    popMatrix
  }
  
  def drawParticles {
    // draw small particles
//    f.particles foreach {p => 
//      if(showDebug) {
//        noFill
//        stroke(255)
//        ellipse(p.x, p.y, repel.rangeAbs, repel.rangeAbs)
//      }
//      
//      fill(255)
//      noStroke
//      rect(p.x, p.y, 3, 3)
//    }
    
    import javax.media.opengl.GL 
    beginGL
    gl.glPointSize(3f)
    gl.glColor4f(1f,1f,1f,1f)
    gl.glBegin(GL.GL_POINTS)
    f.particles foreach {p => 
      gl.glVertex2f(p.x, p.y)
    }
    gl.glEnd
    endGL
    
    // draw mouse particle
    val mp = mf.particles(0)
    if(mp == null) return
    
    fill(255,0,0)
    ellipse(mp.x, mp.y, mp.size*2f, mp.size*2f)
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
