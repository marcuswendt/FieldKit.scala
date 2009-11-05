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
  import util.datatype.collection.ArrayBuffer
  import math.Vec

  var friction = 0.97f
  var timeStep = 60f
  var useSpatialOptimisation = true
  
  var space:Space = new OctreeSpace(1000f, 1000f, 1000f)
  var flocks = new ArrayBuffer[Flock[_]]
  
  /**
   * Prepare particle space and update all flocks
   */
  def update(dt:Float) = {
    // prepare space
    if(useSpatialOptimisation) {
      space.clear
          
      flocks foreach { f =>
        f.particles foreach { p => space.insert(p.asInstanceOf[Vec]) }
      }
    }
    
    // update flocks & particles
    flocks.foreach(_.update(dt))
  }
  
  def +=(f:Flock[_]) {
    f.ps = this
    f.init
    flocks += f
  }
}