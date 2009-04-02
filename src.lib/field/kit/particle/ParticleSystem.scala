/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

/** a simple particle system */
class ParticleSystem extends Logger {
  import scala.collection.mutable.ArrayBuffer
  fine("init")
  var friction = 0.97f
  var timeStep = 60f
  
  var flocks = new ArrayBuffer[Flock[_]]
  def update(dt:Float) = flocks.foreach(_.update(dt))
  
  def +=(f:Flock[_]) = flocks += f
}