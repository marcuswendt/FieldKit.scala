/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 07, 2009 */
package field.kit

/** provides various randomness-generators */
object Random {
  val rnd = new java.util.Random
  
  def apply(min:Float, max:Float) = rnd.nextFloat * (max - min) + min
  
  def apply(scale:Float) = rnd.nextFloat * scale
}
