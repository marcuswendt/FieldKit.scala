/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 20, 2009 */
package field.kit.particle.behaviours

import field.kit.particle.Behaviour

/**
 * makes sure the particle stays within a defined cube-volume by wrapping it around its edges
 */
class Wrap extends Behaviour("Wrap") {
  import field.kit.math.Vec3
  
  protected var _min = new Vec3
  protected var _max = new Vec3
  
  // set default values
  min(0,0,0)
  max(1,1,1)
  
  def apply(p:Particle, dt:Float) {
    val pos = p.position
    
    if(pos.x < _min.x)
      pos.x = _max.x
    else if(pos.x > _max.x)
      pos.x = _min.x

    if(pos.y < _min.y)
      pos.y = _max.y
    else if(pos.y > _max.y)
      pos.y = _min.y

    if (pos.z < _min.z)
      pos.z = _max.z
    else if (pos.z > _max.z)
      pos.z = _min.z
  }
  
  // setters
  def margin(offset:Float) {
    min(-offset, -offset, -offset)
    max(1 + offset, 1 + offset, 1 + offset)
  }
  
  def min(x:Float, y:Float, z:Float) = _min(x,y,z) *= ps.space.dimension
  def max(x:Float, y:Float, z:Float) = _max(x,y,z) *= ps.space.dimension
}
