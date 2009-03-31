/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.math

/**
 * various helpers
 */
object FMath {
  
  // --------------------------------------------------------------------------
  // implements various interpolation and easing functions
  // @see http://en.wikipedia.org/wiki/Interpolation
  // --------------------------------------------------------------------------
  /** 
   * spherical linear interpolation
   * @see http://en.wikipedia.org/wiki/Slerp
   */
  def slerp(current:Float, target:Float, delta:Float):Float = 
    current * (1 - delta) + target * delta
  
  def slerp(current:Double, target:Double, delta:Double):Double = 
    current * (1.0 - delta) + target * delta
  
  def linear(current:Float, target:Float, delta:Float):Float =
    (target - current) * delta + current  
  
  def linear(current:Double, target:Double, delta:Double):Double =
    (target - current) * delta + current  
    
  // --------------------------------------------------------------------------
  // helpers from PApplet
  // --------------------------------------------------------------------------
  def abs(n:Int) = if(n < 0) -n else n
  def abs(n:Float) = if(n < 0) -n else n
 
  def sq(n:Float) = n*n
  def sqrt(a:Float) = Math.sqrt(a).asInstanceOf[Float]
  def log(a:Float) = Math.log(a).asInstanceOf[Float]
  def exp(a:Float) = Math.exp(a).asInstanceOf[Float]
  def pow(a:Float, b:Float) = Math.pow(a,b).asInstanceOf[Float]
  
  def max(a:Int, b:Int) = if(a > b) a else b
  def max(a:Float, b:Float) = if(a > b) a else b
  def min(a:Int, b:Int) = if(a > b) b else a
  def min(a:Float, b:Float) = if(a > b) b else a

  // --------------------------------------------------------------------------
  // trigonomentry
  // --------------------------------------------------------------------------
  val PI = (java.lang.Math.PI).asInstanceOf[Float]
  val HALF_PI = PI / 2f
  val THIRD_PI = PI / 3f
  val QUARTER_PI = PI / 4f
  val TWO_PI = PI * 2f
  
  val DEG_TO_RAD = PI / 180f
  val RAD_TO_DEG = 180f / PI
  
  def sin(f:Float) = Math.sin(f).asInstanceOf[Float]
  def cos(f:Float) = Math.cos(f).asInstanceOf[Float]
  def acos(f:Float) = Math.acos(f).asInstanceOf[Float]
  def tan(f:Float) = Math.tan(f).asInstanceOf[Float]
  def atan(f:Float) = Math.atan(f).asInstanceOf[Float]
  
  def sin(f:Double) = Math.sin(f)
  def cos(f:Double) = Math.cos(f)
  def acos(f:Double) = Math.acos(f)
  def tan(f:Double) = Math.tan(f)
  def atan(f:Double) = Math.atan(f)
  
  // --------------------------------------------------------------------------
  // misc
  // --------------------------------------------------------------------------
  def round(value:Float, precision:Int):Float = {
    val exp = Math.pow(10, precision).asInstanceOf[Float]
    Math.round(value * exp) / exp
  }
  
  def round(value:Double, precision:Int):Double = {
    val exp = Math.pow(10, precision)
    Math.round(value * exp) / exp
  }
}
