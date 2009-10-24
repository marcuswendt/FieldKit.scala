/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import util.Named

/** 
 * a block of code that is applied to all particles, makes them do something
 * @author Marcus Wendt
 */
abstract class Behaviour extends Logger with Named {
  
  def this(name:String) {
    this()
    this.name = name
  }
  
  var ps:ParticleSystem = null
  var flock:Flock[_] = null
  var isEnabled = true
  def toggle = isEnabled = !isEnabled
  def init {}
  def prepare(dt:Float) {}
  def apply(p:Particle, dt:Float)
}