/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

/**
 * Base class for all shape packers
 */
abstract class Packer[T](val shapes:Seq[T]) {
  
  var margin = 1
  
  protected var index = 0
  protected var shapeFilled = false
  protected var current:T = _

  /**
   * @return the number of elements that were fitted into the mapping rectangle
   */
  def updateAll:Int = {
    index = 0
    while(index < shapes.length) {
      current = shapes(index)
      if(!next) return index
      index += 1
    }
  
  	shapes.length
  }
  
  def update:Int = {
    if(shapeFilled) return index
    
    if(index + 1 < shapes.length) {
      current = shapes(index)
      if(!next) {
        shapeFilled = true
        return index
      }
      index += 1
    }
    
    index
  }
  
  def reset = {
    index = 0
    shapeFilled = false
  }

  /**
   * @return true if the current element could be placed in the map, false otherwise
   */
  protected def next:Boolean
  
}
