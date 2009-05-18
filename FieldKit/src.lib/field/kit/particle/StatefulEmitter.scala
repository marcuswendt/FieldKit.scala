/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 17, 2009 */
package field.kit.particle

import scala.reflect.Manifest

/**
 * An emitter for stateful particles that reuses dead particles 
 */
class StatefulEmitter[P <: StatefulParticle](flock:Flock[P])(implicit m:Manifest[P]) 
extends Emitter[P](flock) {
  
  override def create = {
    // look for a reuseable dead particle first
    var i = 0
    var result:Any = null
    while(i < flock.size) {
      val p = flock.particles(i)
      if(p.isDead) {
        result = p
        i = flock.size
      }
      i += 1
    }
    
    if(result == null) {
      super.create
    } else {
      val p = result.asInstanceOf[P]
      p.reinit
      p
    }
    /*
    // look for a reuseable dead particle first
    flock.particles.find(_.isDead) match {
      // no particle available -> create new
      case None => super.create
      // otherwise reuse previous particle
      case Some(p:StatefulParticle) =>
        p.reinit
        p.asInstanceOf[P]
    }
    */
  }
}
