/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle.behaviours

import field.kit.particle._
import field.kit.math._

/**
 * a simple force that is applied to the steering vector of a particle
 * e.g. Wind or Gravity
 */
class DirectionalForce(name:String) extends Behaviour(name) {
  protected var _weight = 1f
  protected var _direction = new Vec3
  protected val tmp = new Vec3
  
  override def prepare = tmp(_direction) *= (_weight)
  
  def apply(p:Particle, dt:Float) = p.steer += tmp
  
  def weight_=(v:Float) = _weight = FMath.clamp(v, 0, 1)
  def weight = _weight
  
  def direction_=(v:Vec3) = _direction(v).normalize
  def direction = _direction
}

class Wind extends DirectionalForce("Wind") {
  direction = new Vec3(1f, 0, 0)
}

class Gravity extends DirectionalForce("Gravity") {
  direction = new Vec3(0, -1f, 0)
}