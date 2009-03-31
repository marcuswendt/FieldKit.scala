/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

/** represents a group of particles within the system */
class Flock[P <: Particle](ps:ParticleSystem) extends Logger {
  import scala.collection.mutable.ArrayBuffer
  fine("init")
  
  var behaviours = new ArrayBuffer[Behaviour]
  var emitter = new Emitter
  var particles = new ArrayBuffer[P]

  def update(dt:Float) = {
    // emit new particles
    val emission = emitter.update(dt)
    for(i <- 0 until emission) {
      0
    }
    
    // prepare behaviours
    behaviours foreach { b => 
      if(b.isEnabled) b.prepare
    }

    particles foreach { p =>
      // apply behaviours      
      behaviours foreach { b =>
        if(b.isEnabled) b.apply(p,dt)
      }

      // update particles
      p.update(dt)
      
      // remove dead particles
      if(p.age > p.lifeTime) this -= p
    }
  }
  
  // ---------------------------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------------------------
  def +=(b:Behaviour) = {
    fine("adding", b)
    b.flock = this
    b.ps = ps
    behaviours += b
  }
   
  def +=(p:P) = {
    fine("adding", p)
    p.flock = this
    p.ps = ps
    particles += p
  }
  
  def -=(p:P) = {
    fine("removing", p)
    particles -= p 
  }
}
