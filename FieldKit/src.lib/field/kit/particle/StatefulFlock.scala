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

  /** multi-threaded update */
  override def update(dt:Float, worker:Int, teamSize:Int) {
	var i = 0
    var limit = particles.size
    
    val jobSize = particles.size/teamSize
    if(jobSize > 0) {
      i = jobSize*worker
      limit = jobSize*(worker+1)
    }
    
    while(i < limit) {
      val p = particles(i)
      i += 1
      
      if(p.isActive) {
        // apply behaviours
        var j = 0
        while(j < behaviours.size) {
          val b = behaviours(j)
          if(b.isEnabled) b.apply(p,dt)
          j += 1
        }
        
        // update particle
        p.update(dt)
      }
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
