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
 * An emitter for stateful particles that reuses dead particles 
 */
class StatefulEmitter[P <: StatefulParticle](flock:Flock[P])(implicit m:Manifest[P]) 
extends Emitter[P](flock) {
  
  /** emits a single particle and applies the emitter behaviours */
  override def emit = {
    val p = create
    p := this 
    
    var i = 0
    while(i < behaviours.size) {
      val b = behaviours(i)
      if(b.isEnabled) b.apply(p,0)
      i += 1
    }
    p
  }
  
  override def create = {
    // look for a reuseable dead particle first
    var i = 0
    var result:Any = null
    while(i < flock.particles.size) {
      val p = flock.particles(i)
      if(p.isDead) {
        result = p
        i = flock.particles.size
      }
      i += 1
    }
    
    // either create a new particle or reuse the old one
    // only add the new particle to the flock again
    if(result == null) {
      val p = super.create
      flock += p
      p
      
    } else {
      val p = result.asInstanceOf[P]
      p.reinit
      p
    }
  }
}
