/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 23, 2009 */
package field.kit.agent.behaviour.motor

import field.kit.agent.Behaviour

/** makes sure an agents location is within a defined cube-volume */
class Wrap(s:Simulation) extends Behaviour("wrap") {
  import field.kit.Vector
  
  protected var _min = Vec3()
  protected var _max = Vec3()
  
  // set default values
  min(0,0,0)
  max(1,1,1)

  var location:Vec3 = null
  
  override def switch {
  	location = parent.get[Vec3]("location")    
  }
  
  def apply = {
    var outside = false
    def out = outside=true
    
    if(location.x < _min.x) {
      location.x = _max.x
      out
    } else if(location.x > _max.x) {
      location.x = _min.x
      out
    }

    if(location.y < _min.y) {
      location.y = _max.y
      out
    } else if(location.y > _max.y) {
      location.y = _min.y
      out
    }

    if (location.z < _min.z) {
      location.z = _max.z
      out
    } else if (location.z > _max.z) {
      location.z = _min.z
      out
    }
    
    outside
  }
  
  // setters
  def margin(offset:Float) {
    min(-offset, -offset, -offset)
    max(1 + offset, 1 + offset, 1 + offset)
  }
  
  def min(x:Float, y:Float, z:Float) = _min(x,y,z) *= s.space.dimension
  def max(x:Float, y:Float, z:Float) = _max(x,y,z) *= s.space.dimension
}