/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

import field.kit.Logger

import scala.collection.mutable.ArrayBuffer

/**
 * Base class for all shape packers
 */
abstract class Packer[T] extends Logger {
  
  var margin = 1f
  
  /** subclasses have to set this */
  protected var packingStrategy:(() => Boolean) = null  

  /** list of elements that are to be packed */
  protected var elements = new ArrayBuffer[T]
  
  /** index of the current element being packed */
  protected var index = 0
  
  /** the current element being packed - used by packingStrategy */
  protected var current:T = _

  protected var _isFinished = false
  
  def +=(e:T) = elements += e
  def apply(i:Int) = elements(i)
  def clear = elements.clear
  
  /** @return the number of elements available to pack */
  def count = elements.size

  /** @return the number of elements in the packing */
  def size = index
    
  def init = {
    index = 0
    _isFinished = false
  }
  
  def update {
    if(_isFinished) return
    
    if(index + 1 < elements.length) {
      current = elements(index)
      if(placeCurrent) {
        index += 1
      } else {
        _isFinished = true
      }
    }
  }

  /**
   * @return true if the current element could be placed in the map, false otherwise
   */
  protected def placeCurrent = packingStrategy()
  
  /**
   * @return the number of elements that were fitted into the mapping rectangle
   */
  def updateAll {
    //info("index", index, "elements", elements.length)
    
    while(index < elements.length) {
      current = elements(index)
      if(!placeCurrent) {
        _isFinished = true    
        return
      }
      index += 1
    }
  
  	_isFinished = true
  }
}
