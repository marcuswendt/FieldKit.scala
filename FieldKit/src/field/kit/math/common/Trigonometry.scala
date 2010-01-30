/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 31, 2009 */
package field.kit.math

/**
 * Trigonometry related methods and constants for math.Common
 */
trait Trigonometry {
  import Common._
  
  /** The value PI as a float. (180 degrees) */
  final val PI = Math.Pi.toFloat
  final val π = PI
  
  /** The value PI/2 as a float. (90 degrees) */
  final val HALF_PI = PI / 2f
  
  /** The value PI/3 as a float. (60 degrees) */
  final val THIRD_PI = PI / 3f
  
  /** The value PI/4 as a float. (45 degrees) */
  final val QUARTER_PI = PI / 4f
  
  /** The value 2PI as a float. (360 degrees) */
  final val TWO_PI = PI * 2f
  
  /** The value 1/PI as a float. */
  final val INV_PI = 1f / PI
  
  /** A value to multiply a degree value by, to convert it to radians. */
  final val DEG_TO_RAD = PI / 180f
  
  /** A value to multiply a radian value by, to convert it to degrees. */
  final val RAD_TO_DEG = 180f / PI
  
  final def sin(f:Float) = Math.sin(f).toFloat
  final def asin(f:Float) = Math.asin(f).toFloat
  final def cos(f:Float) = Math.cos(f).toFloat
  final def acos(f:Float) = Math.acos(f).toFloat
  final def tan(f:Float) = Math.tan(f).toFloat
  final def atan(f:Float) = Math.atan(f).toFloat
  final def atan2(x:Float, y:Float) = Math.atan2(x,y).toFloat
  
  /**
   * Reduces the given angle into the -PI/4 ... PI/4 interval. 
   * 
   * @param theta
   * @return reduced angle
   */
  final def reduceAngle(angle:Float) = {
    var theta = angle % TWO_PI
    
    if(abs(theta) > PI)
      theta = theta - TWO_PI
    
    if(abs(theta) > HALF_PI) 
      theta = PI - theta
 
    theta
  }
}
