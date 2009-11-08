/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 8, 2009 */
package field.kit.particle.behaviour

import math.Common._  
  
/**
 * Base class for all 2D/ 3D border behaviours
 * @author Marcus Wendt
 */
abstract class BorderBehaviour extends Behaviour {
  import math.Vec3
  
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
}


// -----------------------------------------------------------------------------

/**
 * makes sure the particle stays within a defined square by wrapping it around its edges
 */
class BorderWrap2D extends BorderBehaviour {
  def apply(p:Particle, dt:Float) {
    if(p.x < min.x)
      p.x = max.x
    else if(p.x > max.x)
      p.x = min.x

    if(p.y < min.y)
      p.y = max.y
    else if(p.y > max.y)
      p.y = min.y
    
    // constrain to plane
    p.z = 0
  }  
}

/**
 * makes sure the particle stays within a defined cube-volume by wrapping it around its edges
 */
class BorderWrap3D extends BorderWrap2D {
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

// -----------------------------------------------------------------------------

/**
 * reflects a particle at the edges of a defined plane
 */
class BorderBounce2D extends BorderBehaviour {
  var velocityMin = 0f
  var velocityBoost = 2f
  
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
 */
class BorderBounce3D extends BorderBounce2D {
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
