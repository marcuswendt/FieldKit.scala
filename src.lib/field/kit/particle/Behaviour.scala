/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

/** a block of code that is applied to all particles, makes them do something */
abstract class Behaviour(name:String) extends Logger {
  logName = name +"Behaviour"
  fine("init")
  
  var ps:ParticleSystem = null
  var flock:Flock[_] = null
  var isEnabled = true
  def init {}
  def prepare(dt:Float) {}
  def apply(p:Particle, dt:Float)
}