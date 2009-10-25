/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 25, 2009 */
package field.kit.particle.behaviour

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
    import kit.math.Common._
    val invariant = value * (1f - variation)
    val variant = value * variation * random
    invariant + variant
  }
}
