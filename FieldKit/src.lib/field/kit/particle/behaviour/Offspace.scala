/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._
import field.kit.math.Common._
import field.kit.math.Vec3

/**
 * 2D Behaviour
 * Sets a particles position to be on one of the simulation space edges
 * @author Marcus Wendt
 */
class Offspace2D extends Behaviour {
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
    val edge = random(0, 3).toInt
    val position = random(0f, 1f)
    
    edge match {
      // left 
      case 0 =>
        p.x = 0
        p.y = position * ps.space.height
        
      // top
      case 1 =>
        p.x = position * ps.space.width
        p.y = ps.space.height
         
      // right
      case 2 =>
        p.x = ps.space.width
        p.y = position * ps.space.height
          
      // bottom
      case 3 =>
        p.x = position * ps.space.width
        p.y = 0
    }
  }
}