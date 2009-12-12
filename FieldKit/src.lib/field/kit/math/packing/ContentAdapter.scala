/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

import field.kit.math.Vec3

import field.kit.math.geometry.AABB

trait BoundedShape[T] extends AABB {
  var shape:T = _
  def initBounds
  def updateShape
}

/**
 * Allows to bridge any list of shapes to be used as contents for the shape packing 
 */
abstract class ContentAdapter[T](var contents:Seq[T]) {
  
  protected val current = createBoundedShape
  protected val packed = createBoundedShape
  
  def createBoundedShape:BoundedShape[T]
  
  var currentIndex = 0
  
  def shapes = new Iterator[BoundedShape[T]] {
	currentIndex = -1
 
    def next = {
      currentIndex += 1
      current.shape = contents(currentIndex)
      current.initBounds
      current
    }
    
    def hasNext = currentIndex + 1 < contents.length
  }
  
  def packedShapes = new Iterator[BoundedShape[T]] {
    var packedIndex = -1
    
    def next = {
      packedIndex += 1
      packed.shape = contents(packedIndex)
      packed.initBounds
      packed
    }
    
    def hasNext = packedIndex + 1 < currentIndex
  }
}