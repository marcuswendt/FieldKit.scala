/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created October 24, 2009 */
package field.kit.particle.behaviour

/**
 * randomly place the particle within a defined cube
 * note: min and max should be given as positive normalized vectors [0, 1]
 * @author Marcus Wendt
 */
class Randomize extends Behaviour {
  import field.kit.math.Vec3
  
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
    import kit.math.Common._
    p.x = minAbs.x + maxAbs.x * random
    p.y = minAbs.y + maxAbs.y * random
    p.z = minAbs.z + maxAbs.z * random
  }
}
