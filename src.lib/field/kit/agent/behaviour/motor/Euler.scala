/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 23, 2009 */
package field.kit.agent.behaviour.motor

import field.kit.agent.Behaviour

/** performs euler integration of the velocity on the location vector */
class Euler extends Behaviour("euler") {
  import field.kit.Vector
  
  var velocity:Vec3 = null
  var steer:Vec3 = null
  var location:Vec3 = null
  
  override def switch {
  	// get fields
    velocity = parent.value[Vec3]("velocity")
    steer = parent("steer", Vec3()).get
    location = parent("location", Vec3()).get    
  }
  
  def apply = {
    val friction = parent.value("friction", 0.97f)
        
    velocity += steer
    steer.zero
    velocity *= friction
    location += velocity
    
    // always continue
    true
  }
}
