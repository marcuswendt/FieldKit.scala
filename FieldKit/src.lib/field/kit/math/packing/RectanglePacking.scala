/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

import field.kit.math.geometry.Rect

/**
 * Packs a set of smaller rectangles into a larger rectangle
 */
class RectanglePacking(map:Rect, shapes:Seq[Rect]) extends Packer(shapes) {
  
  /**
   * Packs the given set of rectangles onto the map from left-top to bottom-down
   */
  def next:Boolean = {
    current.x1 = map.x1
    current.y1 = map.y1
    
    var intersectsAny = false
    
    do {
      intersectsAny = false
      for(j <- 0 until index) {
        val packed = shapes(j)
        if(current.intersects(packed)) {
          intersectsAny = true
          current.x1 = packed.x2 + margin
          if(current.x2 > map.x2) {
            current.x1 = map.x1
            current.y1 += margin
          } 
        }
      }
      
      // return remaining shapes
      if(!map.contains(current)) return false
    } while(intersectsAny)
    true
  }
}
