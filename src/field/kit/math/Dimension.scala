/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 27, 2009 */
package field.kit.math

/**
 * Utility class; defines the width and height of a 2 dimensional area
 */
class Dim2[T](var x:T, var y:T) {
  
  def this() { 
    this(0.asInstanceOf[T], 0.asInstanceOf[T])
  }
  
  def :=(d:Dim2[T]) {
    this.x = d.x
    this.y = d.y
  }
  
  def :=(x:T, y:T) {
    this.x = x
    this.y = y
  }
  
  // -- Aliases ----------------------------------------------------------------
  def width = x
  def width_=(x:T) = this.x = x
    
  def height = y
  def height_=(y:T) = this.y = y
  
  def columns = x
  def columns_=(x:T) = this.x = x
    
  def rows = y
  def rows_=(y:T) = this.y = y
    
  // -- Helpers ----------------------------------------------------------------
  override def toString = "Dim2["+ toLabel +"]"
  def toLabel = "x:"+ x +" y:"+ y 
}