/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour


/**
 * reflects a particle at the edges of a defined plane
 * @author Marcus Wendt
 */
class Offspace extends Behaviour {
  import math.Common._
  import math.Vec3
  
  var margin = 0f
  
  /** the absolute minimum coord */
  protected var min = Vec3()
  
  /** the absolute minimum coord */
  protected var max = Vec3()
  
  /** update the absolute coords */
  override def prepare(dt:Float) {
     min := (-margin * .5f) *= ps.space.dimension
     max := (1 + margin * .5f) *= ps.space.dimension
  }
    
  def apply(p:Particle, dt:Float) {
    
  }
}