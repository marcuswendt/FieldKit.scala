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
class Repel extends RangedBehaviour {
  import math.Common._
  import math.Vec3
    
  def apply(p:Particle, dt:Float) {
    ps.space(p, range, neighbours)

	neighbours foreach { _n =>
	  val n = _n.asInstanceOf[Particle]
      if(n != p) {
        tmp := n -= p
        //val dist = tmp.length
        val dist = n.distance(p)
        if(dist < range*2f) {
          tmp *= -weight
          p.steer += tmp
        }
      }
    }
 
//    if(neighbours.size)
    info("neighbours", neighbours.size, p.steer)
  }
}

/**
 * reflects a particle at the edges of a defined plane
 * @author Marcus Wendt
 */
class Attract extends Behaviour {
    
  def apply(p:Particle, dt:Float) {
    
  }
}

/**
 * reflects a particle at the edges of a defined plane
 * @author Marcus Wendt
 */
class Align extends Behaviour {
    
  def apply(p:Particle, dt:Float) {
    
  }
}