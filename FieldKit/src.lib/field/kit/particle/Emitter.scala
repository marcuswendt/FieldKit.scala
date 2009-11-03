/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import kit.math.Vec3
import scala.reflect.Manifest

/** 
 * represents a point based emitter
 * @author Marcus Wendt
 */
class Emitter[P <: Particle](val flock:Flock[P])(implicit m:Manifest[P]) extends Vec3 with Logger {
  import field.kit.math.Vec3
  import field.kit.util.datatype.collection.ArrayBuffer
  fine("init")
  
  var rate = 1
  var interval = 1000f
  var max = 1000
  
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
      while(j < rate && flock.size < max) {
        emit
        j += 1
      }
    }
  }
  
  /** emits a single particle and applies the emitter behaviours */
  def emit = {
    // create particle
    val p = create
    p := this 
    
    // add particle to flock
    flock += p
    
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
  
   override def toString = "Emitter["+ toLabel +"]"
}