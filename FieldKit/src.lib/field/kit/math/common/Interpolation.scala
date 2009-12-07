/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 31, 2009 */
package field.kit.math

/**
 * Various interpolation related methods for math.Common
 */
trait Interpolation {
	import Common._
  
  // --------------------------------------------------------------------------
  // implements various interpolation and easing functions
  // @see http://en.wikipedia.org/wiki/Interpolation
  // --------------------------------------------------------------------------
  /** 
   * spherical linear interpolation
   * @see http://en.wikipedia.org/wiki/Slerp
   */
  final def slerp(current:Float, target:Float, delta:Float):Float = 
    current * (1 - delta) + target * delta
  
  final def slerp(current:Double, target:Double, delta:Double):Double = 
    current * (1.0 - delta) + target * delta
  
  final def linear(current:Float, target:Float, delta:Float):Float =
    (target - current) * delta + current  
  
  final def linear(current:Double, target:Double, delta:Double):Double =
    (target - current) * delta + current  

  final def slerpAngle(cur:Float, to:Float, delta:Float) = {
    var result = cur
    if(cur < 0 && to > 0) {
      if(abs(cur) > HALF_PI && abs(to) > HALF_PI) result += TWO_PI
    } else if (cur > 0 && to < 0) {
      if (abs(cur) > HALF_PI && abs(to) > HALF_PI) result -= TWO_PI
    }
    result * (1 - delta) + to * delta
  }
}
