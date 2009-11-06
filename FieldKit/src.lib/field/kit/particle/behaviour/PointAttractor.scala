/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour

/**
 * A static
 */
class PointAttractor extends Behaviour {
  import math.Vec3
  var position = Vec3()
  var range = 0.1f
  var weight = 0.1f
  
  protected var rangeAbs = 0f
  protected var positionAbs = Vec3()
  protected var tmp = Vec3()
  
  override def prepare(dt:Float) {
    rangeAbs = ps.space.toAbsolute(range)
    positionAbs := position *= ps.space.dimension
  }
  
  def apply(p:Particle, dt:Float) {
    tmp := positionAbs -= p
    tmp.normalize
    tmp *= weight
    p.steer += tmp
  }
}

/**
 * An attactor that jumps to a random position from time to time
 */
class RandomPointAttractor extends PointAttractor {
  import kit.math.Common._
  import math.Vec3
  
  var min = Vec3(0f,0f,0f)
  var max = Vec3(1f,1f,1f)
  var interval = 1000
  
  protected var timer = 0f
    
  override def prepare(dt:Float) {
    timer += dt
    if(timer > interval) {
      timer = 0
      position.x = random(min.x, max.x)
      position.y = random(min.y, max.y)
      position.z = random(min.z, max.z)
      super.prepare(dt)
    }
  }
  
//  override def apply(p:Particle, dt:Float) {
//    
//  }
}