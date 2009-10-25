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
 * makes sure the particle stays within a defined square by wrapping it around its edges
 * @author Marcus Wendt
 */
class Wrap2D extends Behaviour {
  import field.kit.math.Vec3
  
  var margin = 0f 
    
  /** the absolute minimum coord */
  protected var min = Vec3()
  
  /** the absolute minimum coord */
  protected var max = Vec3()
  
  /** update the absolute coords */
  override def prepare(dt:Float) {
     min := (-margin * .5f) *= ps.space.dimension
     max := (1 + margin * .5f) *= ps.space.dimension
  }
    
  def apply(p:Particle, dt:Float) {
    if(p.x < min.x)
      p.x = max.x
    else if(p.x > max.x)
      p.x = min.x

    if(p.y < min.y)
      p.y = max.y
    else if(p.y > max.y)
      p.y = min.y
  }  
}

/**
 * makes sure the particle stays within a defined cube-volume by wrapping it around its edges
 * @author Marcus Wendt
 */
class Wrap3D extends Wrap2D {
  import field.kit.math.Vec3
    
  override def apply(p:Particle, dt:Float) {
    if(p.x < min.x)
      p.x = max.x
    else if(p.x > max.x)
      p.x = min.x

    if(p.y < min.y)
      p.y = max.y
    else if(p.y > max.y)
      p.y = min.y

    if (p.z < min.z)
      p.z = max.z
    else if (p.z > max.z)
      p.z = min.z
  }  
}
