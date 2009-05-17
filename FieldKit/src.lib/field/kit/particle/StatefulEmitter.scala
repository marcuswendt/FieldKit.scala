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
    flock.find(_.isDead) match {
      // no particle available -> create new
      case None => super.create
      // otherwise reuse previous particle
      case Some(p:StatefulParticle) =>
        p.reinit
        p.asInstanceOf[P]
    }
  }
}
