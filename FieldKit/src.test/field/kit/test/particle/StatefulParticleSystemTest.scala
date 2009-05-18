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
  
  ps += f
  f += new Wind
  f += new Gravity
  f += new Wrap
  
  val timer = new Timer
  
  init(1280, 768, false, {
    info("initializer")
    ps.space.set(width, height, 100) 
    info("space width", ps.space.width, "height", ps.space.height, "depth", ps.space.depth)
  })
  
  def render {
    // update
    val dt = timer.update 
    ps.update(dt)
    
    // render
    background(64)
    rectMode(CENTER)
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