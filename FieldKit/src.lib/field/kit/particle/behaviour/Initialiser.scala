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
  
  var size = 10f
  var sizeVariation = 0f // [0,1]
  
  var isPerpetual = false
  
  def apply(p:Particle, dt:Float) {
    p.steerMax = value(acceleration, accelerationVariation)
    p.velocityMax = value(velocity, velocityVariation)
    p.lifeTime = if(isPerpetual) Particle.UNDEFINED else value(lifeTime, lifeTimeVariation)
    p.size = value(size, sizeVariation)
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
 * Sets a particles colour attributes
 * @author Marcus Wendt
 */
class ColourInitialiser extends Behaviour {
  
  var colour = Colour()
  
  var acceleration = 0.1f
  var accelerationVariation = 0f // [0,1]
  
  var velocity = 0.5f
  var velocityVariation = 0f // [0,1]
  
  var hueVariation = 0f // [0,1]
  var saturationVariation = 0f // [0,1]
  var valueVariation = 0f // [0,1]
  
  def apply(p:Particle, dt:Float) {
    p.colour := colour
    
    // shift initial base colour using variations
    p.colour.shiftHue(randomNormal * hueVariation)
    p.colour.shiftSaturation(randomNormal * saturationVariation)
    p.colour.shiftValue(randomNormal * valueVariation)
    
    p.colourSteerMax = value(acceleration, accelerationVariation)
    p.colourVelocityMax = value(velocity, velocityVariation)
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