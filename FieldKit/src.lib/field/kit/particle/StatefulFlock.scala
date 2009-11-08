/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 17, 2009 */
package field.kit.particle

import scala.reflect.Manifest

/**
 * A flock for stateful particles
 */
class StatefulFlock[P <: StatefulParticle](implicit m:Manifest[P]) extends Flock[P] {
  
  // use a stateful emitter instead of the default
  emitter = new StatefulEmitter[P](this)

  override def update(dt:Float) {
    // update emitter / creates new particles
    emitter.update(dt)
    
    // prepare behaviours
    var i = 0
    while(i < behaviours.size) {
      val b = behaviours(i)
      if(b.isEnabled) b.prepare(dt)
      i += 1
    }
    
    i = 0
    var j = 0
    while(i < particles.size) {
      val p = particles(i)
      
      if(p.isActive) {
        // apply behaviours
        j = 0
        while(j < behaviours.size) {
          val b = behaviours(j)
          if(b.isEnabled) b.apply(p,dt)
          j += 1
        }
        
        // update particle
        p.update(dt)
      }
      i += 1
    }
  }
  
  /**
   * returns the number of active particles in the flock
   */
  override def size = {
    var count = 0
    var i = 0
    while(i < particles.size) {
      val p = particles(i)
      if(p.isActive)
        count += 1
      i += 1
    }
    count
  }
}
