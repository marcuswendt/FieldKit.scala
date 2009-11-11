/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour

/**
 * A static attractor towards a point
 */
class AttractorPoint extends Behaviour {
  import math.Vec3
  
  /** normalized value [0,1] */
  var position = Vec3()
  
  /** normalized value [0,1] */
  var range = 0.1f
  
  var weight = 0.1f
  
  protected var rangeAbs = 0f
  protected var positionAbs = Vec3()
  protected var tmp = Vec3()
  
  override def prepare(dt:Float) {
    rangeAbs = ps.space.toAbsolute(range)
    positionAbs := position *= ps.space.dimension
  }
  
  def apply(p:Particle, dt:Float) {
    tmp := positionAbs -= p
    tmp.normalize
    tmp *= weight
    p.steer += tmp
  }
}

/**
 * An attactor that jumps to a random position from time to time
 */
class AttractorRandomPoint extends AttractorPoint {
  import kit.math.Common._
  import math.Vec3
  
  var min = Vec3(0f,0f,0f)
  var max = Vec3(1f,1f,1f)
  var interval = 1000
  
  protected var timer = 100000000f
    
  override def prepare(dt:Float) {
    if(timer > interval) {
      timer = 0
      position.x = random(min.x, max.x)
      position.y = random(min.y, max.y)
      position.z = random(min.z, max.z)
      super.prepare(dt)
    }
    timer += dt
  }
}


/** 
 * An attractor the drags particles on an orbit around a point
 * It is essentially a 2D behaviour that operates on a plane (z=0)
 */
class AttractorCircular extends AttractorPoint {
  import math.Vec3
  
  /** weight of the homing force */
  var homing = 0.5f
  
  /** weight of the tangential force */
  var tangential = 0.5f
  
  /** normalized value [0,1] */
  var radius = 0.2f
  
  /** left and right turn variation */
  var variation = -1
  
  protected var radiusAbs = 0f
  protected var centerForce = Vec3()
  protected var homingForce = Vec3()
  protected var tangentForce = Vec3()
  protected var result = Vec3()
  
  override def prepare(dt:Float) {
    super.prepare(dt)
    radiusAbs = ps.space.toAbsolute(radius)
  }
  
  override def apply(p:Particle, dt:Float) {
    val index = p.id
    val indexSpeed = p.id / flock.size.toFloat + 0.1f
    val homingVariation = if(index % variation == 0) 1f else 0.1f
    val tangentVariation = if(index % variation == 0) .5f else 1f

    // calculate force towards center position
    (centerForce := p -= positionAbs).normalize *= radiusAbs
    
    // calculate homing force
    homingForce := centerForce 
    homingForce += positionAbs -= p
    homingForce *= homing * homingVariation * indexSpeed
    result := homingForce
    
    // calculate tangential force
    tangentForce.x = centerForce.y
    tangentForce.y = -centerForce.x
    tangentForce.z = 0
    tangentForce.normalize
    tangentForce *= tangential * tangentVariation * indexSpeed
    if(index % variation == 0) 
      tangentForce *= -1f
    
    result += tangentForce
    result *= weight
    
    p.steer += result
  }
}