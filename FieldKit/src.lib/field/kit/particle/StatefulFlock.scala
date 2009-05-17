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
 * A flock for stateful particles
 */
class StatefulFlock[P <: StatefulParticle](implicit m:Manifest[P]) extends Flock[P] {
  
  // use a stateful emitter instead of the default
  emitter = new StatefulEmitter[P](this)
  
  override def update(dt:Float) {
    // update emitter / creates new particles
    emitter.update(dt)
      
    // prepare behaviours
    behaviours foreach { b => 
      if(b.isEnabled) b.prepare(dt)
    }

    particles foreach { p =>
      // only update alive particels
      if(p.isAlive) {
        // apply behaviours      
        behaviours foreach { b =>
          if(b.isEnabled) b.apply(p,dt)
        }
        
        // update particles
        p.update(dt)
      }
    }
  }
}
