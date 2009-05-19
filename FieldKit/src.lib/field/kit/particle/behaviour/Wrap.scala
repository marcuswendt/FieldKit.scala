/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 20, 2009 */
package field.kit.particle.behaviour

import field.kit.particle.Behaviour

/**
 * makes sure the particle stays within a defined cube-volume by wrapping it around its edges
 * @author Marcus Wendt
 */
class Wrap extends Behaviour("Wrap") {
  import field.kit.math.Vec3
  
  /** the normalized minimum coord */
  var min = new Vec3
  /** the normalized maximum coord */
  var max = new Vec3
  
  /** the absolute minimum coord */
  protected var _min = new Vec3
  
  /** the absolute minimum coord */
  protected var _max = new Vec3
  
  // set detaults
  override def init {
    min(0,0,0)
    max(1,1,1)
  }
  
  /** update the absolute coords */
  override def prepare(dt:Float) {
     _min(min) *= ps.space.dimension
     _max(max) *= ps.space.dimension
  }
  
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
}
