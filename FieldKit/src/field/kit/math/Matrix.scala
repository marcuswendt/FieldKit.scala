/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.math

/**
 * Base class for all Matrizes
 * @author Marcus Wendt
 */
abstract class Matrix(val size:Int) {
  import java.nio.FloatBuffer
  
  /** @return the value stored in this matrix at row, column. */
  def apply(row:Int, column:Int):Float

  /** Sets the value at the given row and column in this matrix */
  def update(row:Int, column:Int, value:Float)
  
  /** Puts this matrix into the given FloatBuffer */
  def put(buffer:FloatBuffer) {
    for (i <- 0 until size) {
      for (j <- 0 until size) {
        buffer put apply(i,j)
      }
    }
  }
}