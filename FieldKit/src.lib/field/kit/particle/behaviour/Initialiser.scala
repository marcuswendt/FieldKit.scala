/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 25, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._
import field.kit.math.Common._
import field.kit.math.Vec3

/**
 * Sets a particles life time, maximum velocity and maximum steer
 * @author Marcus Wendt
 */
class Initialiser extends Behaviour {
  var acceleration = 1f
  var accelerationVariation = 0f // [0,1]
  
  var velocity = 10f
  var velocityVariation = 0f // [0,1]
  
  var lifeTime = 1000f
  var lifeTimeVariation = 0f // [0,1]
  
  var isPerpetual = false
  
  def apply(p:Particle, dt:Float) {
    p.steerMax = value(acceleration, accelerationVariation)
    p.velocityMax = value(velocity, velocityVariation)
    p.lifeTime = if(isPerpetual) Particle.UNDEFINED else value(lifeTime, lifeTimeVariation)
  }
  
  /**
   * calculates a value based on a base value and a variant part
   */
  private def value(value:Float, variation:Float) = {
    val invariant = value * (1f - variation)
    val variant = value * variation * random
    invariant + variant
  }
}


/**
 * randomly place the particle within a defined cube
 * note: min and max should be given as positive normalized vectors [0, 1]
 * @author Marcus Wendt
 */
class EmitterRandomize extends Behaviour {
  val min = Vec3(0f)
  val max = Vec3(1f)
  var weight = 1f

  protected val minAbs = Vec3()
  protected val maxAbs = Vec3()
  
  override def prepare(dt:Float) {
    minAbs := min *= ps.space.dimension
    maxAbs := max -= min *= ps.space.dimension
  }
  
  def apply(p:Particle, dt:Float) {
    p.x = minAbs.x + maxAbs.x * random
    p.y = minAbs.y + maxAbs.y * random
    p.z = minAbs.z + maxAbs.z * random
  }
}