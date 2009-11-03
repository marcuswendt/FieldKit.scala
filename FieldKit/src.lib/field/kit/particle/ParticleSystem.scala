/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

/** 
 * A basic particle system
 * @author Marcus Wendt
 */
class ParticleSystem extends Logger {
  import field.kit.util.datatype.collection.ArrayBuffer
  fine("init")
  
  var friction = 0.97f
  var timeStep = 60f
  var useSpatialOptimisation = false
  
  var space = Space()
  var flocks = new ArrayBuffer[Flock[_]]
  
  /**
   * Prepare particle space and update all flocks
   */
  def update(dt:Float) = {
    // particle space is filled during flock.update
//    if(useSpatialOptimisation)
//      space.clear

    // update flocks & particles
    flocks.foreach(_.update(dt))
  }
  
  def +=(f:Flock[_]) {
    f.ps = this
    f.init
    flocks += f
  }
}