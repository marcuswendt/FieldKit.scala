/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 23, 2009 */
package field.kit.agent.behaviour.motor

import field.kit.agent._

/** makes sure an agents location is within a defined cube-volume */
class Wrap extends Behaviour("wrap") {
  import field.kit.Vector
  
  var min = Vec3()
  var max = Vec3()
  
  def apply = {
    val location = parent("location", Vec3()).get
    
    var outside = false
    def out = outside=true
    
    if(location.x < min.x) {
      location.x = max.x
      out
    } else if(location.x > max.x) {
      location.x = min.x
      out
    }

    if(location.y < min.y) {
      location.y = max.y
      out
    } else if(location.y > max.y) {
      location.y = min.y
      out
    }

    if (location.z < min.z) {
      location.z = max.z
      out
    } else if (location.z > max.z) {
      location.z = min.z
      out
    }
    
    outside
  }
}