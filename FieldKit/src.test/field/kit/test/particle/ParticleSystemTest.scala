/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
  import kit.math.Common._
  import kit.math.geometry._
  
  val ps = new ParticleSystem
  val f = new Flock[Particle]
  ps += f
  f.emitter.rate = 10
  f.emitter.interval = 1
  f.emitter.max = 1000
  
  val initialiser = new Initialiser
  initialiser.isPerpetual = true
  initialiser.acceleration = 2f
  initialiser.velocity = 25f
  
  f.emitter += initialiser
  f.emitter += new Randomize
  
  val wind = new Wind
  wind.weight = 0.25f
//  f += wind
//  
//  f += new BorderWrap2D
//  f += new Wrap3D
  
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
  
  val repel = new FlockRepel
  repel.isAppliedToOwnFlock = true
  repel.range = 0.01f
  repel.weight = 1f
//  f += repel
  
//  val attract = new Attract
//  attract.range = 0.025f
//  attract.weight = 0.1f
//  f += attract
//  
//  val align = new Align
//  align.range = 0.05f
//  align.weight = 0.01f
//  f += align
  
  val circular = new AttractorCircular
  f += circular
  circular.position := (0.5f, 0.5f, 0f)
  circular.weight = 1f
  
  val timer = new Timer
  
  // toggles
  var showParticles = true
  
  // view rotation
  var xrot = Common.THIRD_PI
  var zrot = 0.1f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
  //init(1024, 768, DEFAULT_FULLSCREEN, DEFAULT_AA, {
//    ps.space = new OctreeSpace(width, height, height)
    ps.space = new QuadtreeSpace(width, height, height)
    f.emitter := ps.space
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
    f.particles foreach {p => 
      if(showDebug) {
        noFill
        stroke(255)
        ellipse(p.x, p.y, repel.rangeAbs, repel.rangeAbs)
      }
      
      fill(255)
      noStroke
      rect(p.x, p.y, 3, 3)
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
  
  val timerInfo = new kit.util.Timer
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
