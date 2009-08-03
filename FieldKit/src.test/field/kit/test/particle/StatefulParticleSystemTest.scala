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
object StatefulParticleSystemTest extends field.kit.Sketch {
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  
  val ps = new ParticleSystem
  
  val f = new StatefulFlock[StatefulParticle]
  f.emitter.rate = 1
  f.emitter.interval = 50
  f.emitter += new Behaviour("initializer") {
    def apply(p:Particle, dt:Float) {
      p.steerMax = 2f
      p.velocityMax = 45f
    }
  }
  
  ps += f
  f += new Wind
  f += new Gravity
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
  
  init(1280, 768, false, {
    ps.space.set(width, height, 100) 
    f.emitter.position := ps.space.center
    info("initialized", ps.space)
  })
  
  def render {
    // update
    val dt = if(rec.isRecording) 1000/30f else timer.update
    ps.update(dt)
    
    // render
    //background(64)
    rectMode(CENTER)
    fill(64, 10)
    rect(width/2f,height/2f,width,height)
    
    noStroke
    fill(255)
    
    var i=0
    while(i < f.particles.size) {
      val p = f.particles(i)
      rect(p.position.x, p.position.y, 3, 3)
      i += 1
    }
  }
}
