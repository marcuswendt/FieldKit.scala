/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger
import scala.reflect.Manifest

/** 
 * represents a group of particles within the system
 * @author Marcus Wendt
 */
class Flock[P <: Particle](val ps:ParticleSystem)(implicit m:Manifest[P]) 
extends Logger with Collection[P] {
  import scala.collection.mutable.ArrayBuffer
  fine("init")
  
  var behaviours = new ArrayBuffer[Behaviour]
  var emitter = new Emitter[P](this)
  var particles = new ArrayBuffer[P]

  emitter.position(ps.space.center)
  
  def update(dt:Float) = {
    // update emitter / creates new particles
    emitter.update(dt)
      
    // prepare behaviours
    behaviours foreach { b => 
      if(b.isEnabled) b.prepare(dt)
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
    b.init
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
  
  def size = particles.size
  
  def elements = particles.elements
}
