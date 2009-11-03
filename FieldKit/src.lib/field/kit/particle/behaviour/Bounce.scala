/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 2, 2009 */
package field.kit.particle.behaviour


/**
 * reflects a particle at the edges of a defined plane
 * @author Marcus Wendt
 */
class Bounce2D extends Behaviour {
  import math.Common._
  import math.Vec3
  
  var margin = 0f
  
  var velocityMin = 0f
  
  var velocityBoost = 2f
    
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
    if(p.x < min.x || p.x > max.x) {
      p.velocity *= -1
      if(abs(p.velocity.x) < velocityMin) {
        p.velocity.x = velocityBoost * signum(p.velocity.x)
        p.velocity.y = velocityBoost * signum(p.velocity.y)
      }
    }

    if(p.y < min.y || p.y > max.y) {
      p.velocity *= -1
      if(abs(p.velocity.y) < velocityMin) {
        p.velocity.x = velocityBoost * signum(p.velocity.x)
        p.velocity.y = velocityBoost * signum(p.velocity.y)
      }
    }
  }  
}

/**
 * reflects a particle at the sides of a cube
 * @author Marcus Wendt
 */
class Bounce3D extends Bounce2D {
  import math.Common._
  import math.Vec3
    
  override def apply(p:Particle, dt:Float) {
    if(p.x < min.x || p.x > max.x) {
      p.velocity *= -1
      if(abs(p.velocity.x) < velocityMin) {
        p.velocity.x = velocityBoost * signum(p.velocity.x)
        p.velocity.y = velocityBoost * signum(p.velocity.y)
        p.velocity.z = velocityBoost * signum(p.velocity.z)
      }
    }

    if(p.y < min.y || p.y > max.y) {
      p.velocity *= -1
      if(abs(p.velocity.y) < velocityMin) {
        p.velocity.x = velocityBoost * signum(p.velocity.x)
        p.velocity.y = velocityBoost * signum(p.velocity.y)
        p.velocity.z = velocityBoost * signum(p.velocity.z)
      }
    }
    
    if(p.z < min.z || p.z > max.z) {
      p.velocity *= -1
      if(abs(p.velocity.z) < velocityMin) {
        p.velocity.x = velocityBoost * signum(p.velocity.x)
        p.velocity.y = velocityBoost * signum(p.velocity.y)
        p.velocity.z = velocityBoost * signum(p.velocity.z)
      }
    }
  }  
}
