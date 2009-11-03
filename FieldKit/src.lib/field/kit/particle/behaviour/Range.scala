/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour

/**
 * base class for most distance/range based behaviours
 * @author Marcus Wendt
 */
abstract class RangedBehaviour extends Behaviour {
  import math.Common._
  import math._
  import scala.collection.mutable.ArrayBuffer
  
  var range = 10f  
  var weight = 1f
  
  protected val tmp = Vec3()
  protected val neighbours = new ArrayBuffer[Vec] 
}
