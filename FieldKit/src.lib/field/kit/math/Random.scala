/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 07, 2009 */
package field.kit.math

/** 
 * provides various randomness-generators
 * @author Marcus Wendt
 */
object Random {
  val rnd = new scala.util.Random
  
  def apply() = rnd.nextFloat
  
  def apply(min:Float, max:Float) = rnd.nextFloat * (max - min) + min
  
  def apply(min:Int, max:Int) = (rnd.nextFloat * (max - min) + min).asInstanceOf[Int]
  
  def apply(scale:Float) = rnd.nextFloat * scale
  
  /**
   * Returns a random number in the interval -1 .. +1. 
   * @return random float
   */
  def normalized = rnd.nextFloat * 2f - 1f
}
