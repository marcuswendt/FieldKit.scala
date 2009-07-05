/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.math

// —————————————————————————————————————————————————————————————————————————————
// Base classes
// —————————————————————————————————————————————————————————————————————————————
/**
 * Base class for all Matrizes
 * @author Marcus Wendt
 */
abstract class Matrix[T](val size:Int) {
  /** @return Returns the value at the given position in the matrix. */
  def apply(i:Int, j:Int):T
  
  /** @return Returns the value at the given position in the matrix. */
  def get(i:Int, j:Int) = this.apply(i, j)
  
  /** Sets the value at position i,j in the matrix. */
  def update(i:Int, j:Int, s:T):Matrix[T]

  /** Sets the value at position i,j in the matrix. */
  def set(i:Int, j:Int, s:T) = this.update(i,j,s)
  
  /** Sets all of the values in this matrix to zero. */
  def zero:Matrix[T]
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity:Matrix[T]
}

/**
 * Base class for all Float Matrizes
 * @author Marcus Wendt
 */
abstract class MatrixF(size:Int) extends Matrix[Float](size) {
  
}