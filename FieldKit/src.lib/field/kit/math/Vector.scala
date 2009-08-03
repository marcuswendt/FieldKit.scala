/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 07, 2009 */
package field.kit.math

// —————————————————————————————————————————————————————————————————————————————
// Base classes 
// —————————————————————————————————————————————————————————————————————————————
/**
 * Base class for all Vectors
 * @author Marcus Wendt
 */
abstract class Vector[T](val size:Int) extends Collection[T] {
  /**
   * Attempts to interpret this String to set this Vectors components
   * @return itself
   */
  def :=(s:String):Vector[T]
  
  def update(i:Int, value:T)
}

/**
 * Base class for all Float Vector Types
 * @author Marcus Wendt
 */
abstract class VecF(size:Int) extends Vector[Float](size) {
  // -- Local Operations -------------------------------------------------------
  def +=(s:Float)
  def -=(s:Float)
  def *=(s:Float)
  def /=(s:Float)
  
  def zero
  def negate = this *= -1
  
  def length = Math.sqrt(lengthSquared).asInstanceOf[Float]
  def lengthSquared:Float
  
  def normalize = {
    val l = length
    if(l != 0)
      this /= l
    else
      this /= 1
    this
  }
}
