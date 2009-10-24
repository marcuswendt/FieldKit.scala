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
class Wrap extends Behaviour {
  import field.kit.math.Vec3
  
  /** the normalized minimum coord */
  var min = Vec3()
  
  /** the normalized maximum coord */
  var max = Vec3()
  
  /** the absolute minimum coord */
  protected var _min = Vec3()
  
  /** the absolute minimum coord */
  protected var _max = Vec3()
  
  // set detaults
  override def init {
    min := 0f
    max := 1f
  }
  
  /** update the absolute coords */
  override def prepare(dt:Float) {
     _min := min *= ps.space.dimension
     _max := max *= ps.space.dimension
  }
  
  def apply(p:Particle, dt:Float) {
    if(p.x < _min.x)
      p.x = _max.x
    else if(p.x > _max.x)
      p.x = _min.x

    if(p.y < _min.y)
      p.y = _max.y
    else if(p.y > _max.y)
      p.y = _min.y

    if (p.z < _min.z)
      p.z = _max.z
    else if (p.z > _max.z)
      p.z = _min.z
  }
  
  // setters
  def margin(offset:Float) {
    min := -offset
    max := 1 + offset
  }
}
