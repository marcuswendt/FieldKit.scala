/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._
import field.kit.colour._
import field.kit.math.Common._
import field.kit.math.Vec4

/**
 * Applies a directional force to the particles HSVA colour steering
 */
class ColourDirectionalForce extends Behaviour {
  var weight = 1f
  
  protected var d = HSVA()
  protected val tmp = HSVA()
  
  override def prepare(dt:Float) = tmp := d *= weight
  
  def apply(p:Particle, dt:Float) = p.colourSteer += tmp
  
  def direction_=(v:HSVA) = d := v normalize
  def direction = d
}