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
	import field.kit.math.Vec
	import scala.collection.mutable.ArrayBuffer
	
	var friction = 0.97f
	var timeStep = 60f
	var useSpatialOptimisation = true
  
	var space:Space = new OctreeSpace(1000f, 1000f, 1000f)
	var flocks = new ArrayBuffer[Flock[_]]
  
  /**
   * Prepare particle space and update all flocks
   */
  def update(dt:Float) = {
    if(useSpatialOptimisation)
      updateSpace
    
    var i = 0
    while(i < flocks.size) {
      flocks(i).update(dt)
      i += 1
    }
  }
  
  def updateSpace {
    space.clear
    var i = 0
    var j = 0
    while(i < flocks.size) {
      val flock = flocks(i)
      i += 1
      
      j = 0
      while(j < flock.size) {
        space.insert(flock(j).asInstanceOf[Vec])
        j += 1
      }
    }
  }
  
  def +=(f:Flock[_]) {
    f.ps = this
    f.init
    flocks += f
  }
}