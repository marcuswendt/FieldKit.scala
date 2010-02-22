/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.util.Nameable
import field.kit.Logger

/** 
 * a block of code that is applied to all particles, makes them do something
 * @author Marcus Wendt
 */
abstract class Behaviour extends Logger with Nameable {
  
  def this(name:String) {
    this()
    this.name = name
  }
  
  var ps:ParticleSystem = null
  var flock:Flock[_ <: Particle] = null
  
  var isEnabled = true
  def toggle = isEnabled = !isEnabled
  def init {}
  def prepare(dt:Float) {}
  def apply(p:Particle, dt:Float)
}