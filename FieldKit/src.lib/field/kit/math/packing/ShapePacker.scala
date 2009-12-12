/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 10, 2009 */
package field.kit.math.packing

import field.kit.Logger

object ShapePacker {
  object Mode extends Enumeration {
    // 2D Modes
    val TopLeftBottomRight = Value
    val BottomLeftTopRight = Value
    
    // TODO add 3D Modes
  }
}


/**
 * Base class for all shape packing algorithms
 */
class ShapePacker(var shape:ShapeAdapter, var content:ContentAdapter[_]) extends Logger {
  
  /** the order in which to pack the elements */
  var mode = ShapePacker.Mode.TopLeftBottomRight
  
  protected var index = 0

  /** tries to pack as much of the contents elements into the shape as possible */
  def pack {
    mode match {
      case ShapePacker.Mode.TopLeftBottomRight =>
        packTopLeftBottomRight
        
      case _ =>
        warn("Unsupported packing mode!")
    }
  }
  
  protected def packTopLeftBottomRight {
//    info("packTopLeftBottomRight")
//    info("shape", "min", shape.min, "max", shape.max)
    
    // go through all contents
    for(current <- content.shapes) {
      current := shape.min
      current.updateBounds
      
      // try to find each a fitting place in the packing shape
      var intersectsAny = false
      do {
        intersectsAny = false
        
        for(packed <- content.packedShapes) {
//          info("packed", packed)
          
          if(current.intersects(packed)) {
//            info("intersects", packed)
            
            intersectsAny = true
            current.x = packed.max.x + 10
            current.updateBounds
            
            // check if we reached the end of the shape
            if(current.min.x > shape.max.x) {
              info("over edge")
              
              current.x = shape.min.x
              current.y += 10
              current.updateBounds
            }
          }
        }
        
        if(!shape.contains(current)) return //intersectsAny = false
      } while(intersectsAny)
        
      info("done", content.currentIndex)
      
      current.updateShape
    }
  }
}
