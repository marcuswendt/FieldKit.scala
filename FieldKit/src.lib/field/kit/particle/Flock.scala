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
  import scala.collection.mutable.ArrayBuffer
  
  var ps:ParticleSystem = null
  var emitter = new Emitter[P](this)
  var particles = new ArrayBuffer[P]
  var behaviours = new ArrayBuffer[Behaviour]
  
  /** called automatically when the flock is added to the particle system */
  def init {
    emitter := ps.space
  }
  
  /** simple, single threaded update */
  def update(dt:Float) {
    prepare(dt)
    update(dt, 0, 1)
  }
  
  /** prepare needs to be called once per flock per update */
  def prepare(dt:Float) {
	// update emitter / creates new particles
    emitter.update(dt)
    
    // prepare behaviours
    var i = 0
    while(i < behaviours.size) {
      val b = behaviours(i)
      if(b.isEnabled) b.prepare(dt)
      i += 1
    }
  }
  
  /** multi-threaded update */
  def update(dt:Float, worker:Int, teamSize:Int) {
    var i = 0
    var limit = particles.size
    
    val jobSize = particles.size/teamSize
    if(jobSize > 0) {
      i = jobSize*worker
      limit = jobSize*(worker+1)
    }
    
    while(i < limit && i < particles.size) {
      val p = particles(i)
      i += 1
      
      // apply behaviours
      var j = 0
      while(j < behaviours.size) {
        val b = behaviours(j)
        j += 1
        if(b.isEnabled) b.apply(p,dt)
      }
      
      // update particles
      p.update(dt)
      
      // remove dead particles
      if(p.age > p.lifeTime && p.lifeTime != Particle.UNDEFINED) this -= p
    }
  }
  
  // ---------------------------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------------------------
  def +=(b:Behaviour) {
    b.flock = this
    b.ps = ps
    b.init
    behaviours += b
  }
   
  def +=(p:P) {
    p.flock = this
    p.ps = ps
    p.id = nextId
    p.init
    particles += p
  }
  
  def -=(p:P) {
    particles -= p 
  }
  
  /** starts the particle animation again */
  def reset = particles.clear
  
  /** @return the next free unique id */
  protected def nextId = { _nextId += 1; _nextId }
  private var _nextId = -1
  
  // -- Collection Helpers -----------------------------------------------------
  def size = particles.size
  
  def apply(i:Int) = particles.apply(i)
}
