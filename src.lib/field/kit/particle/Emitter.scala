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

/** represents a simple point-emitter */
class Emitter[P <: Particle](flock:Flock[P])(implicit m:Manifest[P]) extends Logger {
  import field.kit.math.Vec3
  import scala.collection.mutable.ArrayBuffer
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
      behaviours foreach { b => 
      	if(b.isEnabled) b.prepare(dt)
      }
    
      // emit particels
      for(i <- 0 until rate) {
        // create particle
        val p = create
        p.position(position) 
        flock += p
        
        // apply behaviours      
        behaviours foreach { b =>
        	if(b.isEnabled) b.apply(p,dt)
        }
      }
    }
  }
  
  /** instantiates a new object from the parameterized type */
  def create = {
    fine("creating new "+ m)
    val clazz = Class.forName(m.toString)
    clazz.newInstance.asInstanceOf[P]
  }
  
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
