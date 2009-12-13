/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

import field.kit.math.geometry.Rect

object RectanglePacking {
  object Mode extends Enumeration {
    val HorizontalDown = Value
    val VerticalDown = Value
    val VerticalUp = Value
    val VerticalUpUsingMap = Value
  }
  
  object Origin extends Enumeration {
    val TopLeft = Value
    val BottomLeft = Value
  }
  
  // ---------------------------------------------------------------------------
  
  /** Base class for all types of shape maps */
  abstract class Map extends Rect {
    def isInside(x:Float, y:Float):Boolean
  }
    
  import java.awt.image.BufferedImage

  /**
   * Treats a BufferedImage as black & white pack map where all pixels above a certain
   * threshold [0, 1] are considered inside the packing shape
   * 
   * NOTE: This class only considers the first / red-channel of an image
   * You might want to subclass it and override the valueAt method for more advanced thresholding
   */
  class BufferedImageMap(image:BufferedImage, var threshold:Float) extends Map {
    val raster = image.getRaster
    width = image.getWidth
    height = image.getHeight
    
    def isInside(_x:Float, _y:Float):Boolean = {
      val x = _x - x1
      val y = _y - y1
      
      if(x < 0 || y < 0 || x > width || y > height) 
        return false
      
      raster.getSampleFloat(x.toInt, y.toInt, 0) > threshold
    }
  }
}

/**
 * Packs a set of smaller rectangles into a larger rectangle
 */
class RectanglePacking(parent:Rect, shapes:Seq[Rect]) extends Packer(shapes) {
  
  var map:RectanglePacking.Map = _ 
      
  protected var _mode:RectanglePacking.Mode.Value = _ 
  protected var origin:RectanglePacking.Origin.Value = _

  // init
  mode = RectanglePacking.Mode.HorizontalDown
  
  def mode = _mode
  def mode_=(mode:RectanglePacking.Mode.Value) {
    this._mode = mode
    
    mode match {
      case RectanglePacking.Mode.HorizontalDown => 
        packingStrategy = packHorizontalDown
        origin = RectanglePacking.Origin.TopLeft
        
      case RectanglePacking.Mode.VerticalDown => 
        packingStrategy = packVerticalDown
        origin = RectanglePacking.Origin.TopLeft
        
      case RectanglePacking.Mode.VerticalUp => 
        packingStrategy = packVerticalUp
        origin = RectanglePacking.Origin.BottomLeft
        
      case RectanglePacking.Mode.VerticalUpUsingMap => 
        if(map == null) {
          warn("This packing mode requires a RectanglePacking.ShapeMap to be set!")
          return this.mode_=(RectanglePacking.Mode.VerticalUp)
        }
        packingStrategy = packVerticalUpUsingShapeMap
        origin = RectanglePacking.Origin.BottomLeft
    }
  }
  
  /** place current rect at origin, then try to find a place in the packing */
  override def next = {
    origin match {
      case RectanglePacking.Origin.TopLeft =>
        current.x1 = parent.x1
        current.y1 = parent.y1
        
      case RectanglePacking.Origin.BottomLeft =>
        current.x1 = parent.x1
        current.y1 = parent.y2 - current.height
    }
    super.next
  }
  
  /** A left-right top-down packing algorithm */
  def packHorizontalDown():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = shapes(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.x1 = packed.x2 + margin
        
          if(current.x2 > parent.x2) {
            current.x1 = parent.x1
            current.y1 += margin
          } 
        }
      }
    
      if(!intersects) return true
      if(!parent.contains(current)) return false
    }
    true
  }
  
  /** A top-down left-right packing algorithm */
  def packVerticalDown():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = shapes(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y2 + margin
          
          if(current.y2 > parent.y2) {
            current.y1 = parent.y1
            current.x1 += margin
          } 
        }
      }
    
      if(!intersects) return true
      if(!parent.contains(current)) return false
    }
    true
  }
  
  /** A bottom-up left-right packing algorithm */
  def packVerticalUp():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = shapes(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y1 - current.height - margin
        
          if(current.y1 < parent.y1) {
            current.y1 = parent.y2 - current.height
            current.x1 += margin
          } 
        }
      }
      
      if(!intersects) return true
      if(!parent.contains(current)) return false
    }
    true
  }
  
  /** 
   * A bottom-up left-right packing algorithm that uses a map to determine
   * whether the current element within the shape
   */
  def packVerticalUpUsingShapeMap():Boolean = {
    while(true) {
      var intersects = false
      // try to find a place where the rect doesnt intersect any other rects
      for(j <- 0 until index) {
        val packed = shapes(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y1 - current.height - margin
        
          if(current.y1 < parent.y1) {
            current.y1 = parent.y2 - current.height
            current.x1 += margin
          } 
        }
      }
      
      if(!intersects) {
        // check if the found position is within the shape
        if(!map.isInside(current.centerX, current.centerY)) {
          current.y1 -= margin
      
          if(current.y1 < parent.y1) {
            current.y1 = parent.y2 - current.height
            current.x1 += margin
          }
        } else {
          // within shape & not intersecting
          return true
        }  
      }
    
      if(!parent.contains(current)) return false
    }
    true
  }
}
