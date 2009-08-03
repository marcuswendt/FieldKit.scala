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
 * represents a point based emitter
 * @author Marcus Wendt
 */
class Emitter[P <: Particle](val flock:Flock[P])(implicit m:Manifest[P]) extends Logger {
  import field.kit.math.Vec3
  import field.kit.util.datatype.collection.ArrayBuffer
  fine("init")
  
  var position = new Vec3
  var rate = 1
  var interval = 1000f

  var behaviours = new ArrayBuffer[Behaviour]
  
  // internal
  protected var time = 0f
  
  def update(dt:Float) {
    time += dt
    
    if(time >= interval) {
      time = 0
      
      // prepare behaviours
//      behaviours foreach { b => 
//      	if(b.isEnabled) b.prepare(dt)
//      }
      var i = 0
      while(i < behaviours.size) {
        val b = behaviours(i)
        if(b.isEnabled) b.prepare(dt)
        i += 1
      }
    
      // emit particles
      var j = 0
      while(j < rate) {
        emit
        j += 1
      }
    }
  }
  
  /** emits a single particle and applies the emitter behaviours */
  def emit = {
    // create particle
    val p = create
    p.position := position 
        
    // apply behaviours      
//    behaviours foreach { b =>
//      if(b.isEnabled) b.apply(p,0)
//    }
    var i = 0
    while(i < behaviours.size) {
      val b = behaviours(i)
      if(b.isEnabled) b.apply(p,0)
      i += 1
    }
    
    p
  }
  
  /** instantiates a new object from the parameterized type */
  def create = {
    fine("creating new "+ m)
    val clazz = Class.forName(m.toString)
    val p = clazz.newInstance.asInstanceOf[P]
    flock += p
    p
  }
  
  /** ignores the timer and forces the emitter to create new particles immediatly */
  def now = time = interval
  
  // ---------------------------------------------------------------------------
  // HELPERS
  // ---------------------------------------------------------------------------
  def +=(b:Behaviour) = {
    fine("adding", b)
    b.flock = flock
    b.ps = flock.ps
    b.init
    behaviours += b
  }
}
