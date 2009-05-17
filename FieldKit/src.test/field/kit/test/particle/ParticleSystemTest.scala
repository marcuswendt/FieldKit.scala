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
object ParticleSystemTest extends field.kit.Sketch {
  import field.kit.particle._
  import field.kit.particle.behaviour._
  import field.kit.util.Timer
  
  val ps = new ParticleSystem
  val f = new Flock[Particle]
  f.emitter.rate = 1
  f.emitter.interval = 100
  
  ps += f
  f += new Wind
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
    
    for(p <- f) {  
      rect(p.position.x, p.position.y, 3, 3) 
    }
  }
}
