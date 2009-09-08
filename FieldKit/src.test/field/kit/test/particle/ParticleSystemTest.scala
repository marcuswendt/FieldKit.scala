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
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  
  val ps = new ParticleSystem
  val f = new Flock[Particle]
  f.emitter.rate = 1
  f.emitter.interval = 2
  f.emitter.max = 2000
  
  ps += f
  f += new Wind
  f += new Wrap
  f += new Behaviour("perlin") {
    var time = 0f
    var tmp = 0f
    
    override def prepare(dt:Float) = time += dt
    
    def apply(p:Particle, dt:Float) {
      val weight = 10f
      p.steer.x += (noise(tmp, p.age) * 2f - 1f) * weight
      p.steer.y += (noise(time, p.age) * 2f - 1f) * weight
      tmp += dt
    }
  }
  
  val timer = new Timer
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    info("initializer")
    ps.space.set(width, height, 100) 
    info("space width", ps.space.width, "height", ps.space.height, "depth", ps.space.depth)
  })
  
  def render {
    import processing.core.PConstants._
    
    // update
    val dt = timer.update 
    ps.update(dt)
    
    // render
    background(64)
    rectMode(CENTER)
    noStroke
    fill(255)
    
    f.particles foreach(p => rect(p.x, p.y, 3, 3))
  }
}
