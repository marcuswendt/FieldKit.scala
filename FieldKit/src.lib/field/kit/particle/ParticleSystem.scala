/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

/** 
 * a simple particle system
 * @author Marcus Wendt
 */
class ParticleSystem extends Logger {
  import field.kit.util.datatype.collection.ArrayBuffer
  fine("init")
  
  var friction = 0.97f
  var timeStep = 60f
  
  var space = new Space
  var flocks = new ArrayBuffer[Flock[_]]
  def update(dt:Float) = {
    //flocks.foreach(_.update(dt))
    var i = 0
    while(i < flocks.size) {
      flocks(i).update(dt)
      i += 1
    }
  }
  
  def +=(f:Flock[_]) {
    f.ps = this
    f.init
    flocks += f
  }
}