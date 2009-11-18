/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 24, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._
import field.kit.math.Vec3
import field.kit.math.Common._  

/**
 * randomly place the particle within a defined cube
 * note: min and max should be given as positive normalized vectors [0, 1]
 * @author Marcus Wendt
 */
class Randomise extends Behaviour {
  val min = Vec3(0f)
  val max = Vec3(1f)
  var weight = 1f

  protected val minAbs = Vec3()
  protected val maxAbs = Vec3()
  
  override def prepare(dt:Float) {
    minAbs := min *= ps.space.dimension
    maxAbs := max -= min *= ps.space.dimension
  }
  
  def apply(p:Particle, dt:Float) {
    p.x = minAbs.x + maxAbs.x * random
    p.y = minAbs.y + maxAbs.y * random
    p.z = minAbs.z + maxAbs.z * random
  }
}

// deprecated, alias for randomise
class Randomize extends Randomise