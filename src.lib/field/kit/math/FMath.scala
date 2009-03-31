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
  def slerp(current:Float, target:Float, delta:Float) = 
    current * (1 - delta) + target * delta
  
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
}
