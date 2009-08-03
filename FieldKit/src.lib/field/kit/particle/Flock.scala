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
class Flock[P <: Particle](implicit m:Manifest[P]) 
extends Logger { 
  // with Collection[P] - DO NOT inherit from Collection because this circumvents
  // the optimized field ArrayBuffer and creates memory leaks
  
  import field.kit.util.datatype.collection.ArrayBuffer
  fine("init")
  
  var ps:ParticleSystem = null
  var emitter = new Emitter[P](this)
  var particles = new ArrayBuffer[P]
  var behaviours = new ArrayBuffer[Behaviour]

  /** called automatically when the flock is added to the particle system */
  def init {
    emitter.position := ps.space.center
  }
  
  def update(dt:Float) {
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
    while(i < particles.size) {
      val p = particles(i)
      
      // apply behaviours
      var j = 0
      while(j < behaviours.size) {
        val b = behaviours(j)
        if(b.isEnabled) b.apply(p,dt)
        j += 1
      }
      
      // update particles
      p.update(dt)
      
      // remove dead particles
      if(p.age > p.lifeTime && p.lifeTime != Particle.UNDEFINED) this -= p
      
      i += 1
    }
    /*
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
      if(p.age > p.lifeTime && p.lifeTime != Particle.UNDEFINED) this -= p
    }
    */
  }
  
  // ---------------------------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------------------------
  def +=(b:Behaviour) {
    fine("adding", b)
    b.flock = this
    b.ps = ps
    b.init
    behaviours += b
  }
   
  def +=(p:P) {
    fine("adding", p)
    p.flock = this
    p.ps = ps
    p.init
    particles += p
  }
  
  def -=(p:P) {
    fine("removing", p)
    particles -= p 
  }
  
  // -- Collection Helpers -----------------------------------------------------
  def size = particles.size
  
  def apply(i:Int) = particles.apply(i)
}
