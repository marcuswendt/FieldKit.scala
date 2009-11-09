/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle.behaviour

import kit.math._
import kit.math.Common._

/**
 * a simple force that is applied to the steering vector of a particle
 * e.g. Wind or Gravity
 * @author Marcus Wendt
 */
class DirectionalForce extends Behaviour {
  var weight = 1f
  protected var _direction = Vec3()
  protected val tmp = Vec3()
  
  override def prepare(dt:Float) = tmp := _direction *= weight
  
  def apply(p:Particle, dt:Float) = p.steer += tmp
  
  def direction_=(v:Vec3) = _direction := v normalize
  def direction = _direction
}

class DirectionalVelocity extends DirectionalForce {
  override def apply(p:Particle, dt:Float) = p.velocity += tmp
}

class Wind extends DirectionalForce {
  logName = "Wind"
  direction = Vec3(1f, 0f, 0f)
}

class Gravity extends DirectionalForce {
  logName = "Gravity"
  direction = Vec3(0f, 1f, 0f)
}