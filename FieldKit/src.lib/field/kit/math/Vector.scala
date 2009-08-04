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
abstract class Vector[T] {
  type V = Vector[T]
  
  /**
   * Attempts to interpret this String to set this Vectors components
   * @return itself
   */
  def :=(s:String):V
  
  def update(i:Int, value:T)
 
  // -- Float Operations -------------------------------------------------------
  /**
   * Adds the given Float to this vector
   * @return result as new vector 
   */
  def +(s:Float):V
  
  /**
   * Subtracts the given float from this vector
   * @return result as new vector 
   */
  def -(s:Float):V
  
  /** 
   * Multiplies the given float with this vector
   * @return result as new vector 
   */
  def *(s:Float):V
  
  /** 
   * Divides this vector through the given float
   * @return result as new vector 
   */
  def /(s:Float):V
  
  
  // -- Local Operations -------------------------------------------------------
  /**
   * Adds the given Float to this vector
   * @return itself
   */
  def +=(s:Float):V
  
  /**
   * Subtracts the given float from this vector
   * @return itself
   */
  def -=(s:Float):V
  
  /**
   * Multiplies this vector with the given float
   * @return itself
   */
  def *=(s:Float):V
  
  /**
   * Divides this vector through the given float
   * @return itself
   */
  def /=(s:Float):V
}

/**
 * Base class for all Float Vector Types
 * @author Marcus Wendt
 */
abstract class VecF extends Vector[Float] {
  // -- Other Operations -------------------------------------------------------
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
