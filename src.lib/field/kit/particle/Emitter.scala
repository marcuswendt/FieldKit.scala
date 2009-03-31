/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

/* represents a simple point-emitter */
class Emitter {
  import field.kit.math.Vec3
  var position = new Vec3
  var rate = 1
  var interval = 100f
  
  var time = 0f
  
  def update(dt:Float) = {
    time += dt
    
    if(time >= interval) {
      time = 0
      rate
    } else {
      0
    }
  }
}
