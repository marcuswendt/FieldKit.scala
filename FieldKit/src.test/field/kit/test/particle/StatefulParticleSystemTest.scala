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
object StatefulParticleSystemTest extends test.Sketch {
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  
  val ps = new ParticleSystem
  
  val f = new StatefulFlock[StatefulParticle]
  f.emitter.rate = 1
  f.emitter.interval = 25
  f.emitter.max = 2000
  
  f.emitter += new Behaviour {
    def apply(p:Particle, dt:Float) {
      p.steerMax = 2f
      p.velocityMax = 25f
      p.lifeTime = 1000 * 10
    }
  }
  
  ps += f
  f += new Wind
  f += new Gravity
  f += new Wrap2D
  
  f += new Behaviour {
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
    ps.space = new Space(width, height, 100) 
    f.emitter := ps.space
    info("initialized", ps.space)
  })
  
  def render {
    // update
    val dt = if(rec.isRecording) 1000/30f else timer.update
    ps.update(dt)
    
    // render
    background(64)
    noStroke
    fill(255)
    
    var i=0
    while(i < f.particles.size) {
      val p = f.particles(i)
      if(p.isActive)
        rect(p.x, p.y, 3, 3)
      
      i += 1
    }
  }
}
