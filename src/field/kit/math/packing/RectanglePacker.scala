/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.math.packing

import field.kit.math.geometry.Rect
import field.kit.math.Vec2

object RectanglePacker {
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
  abstract class Map {
    def contains(x:Float, y:Float):Boolean
    def width:Float
    def height:Float
  }
    
  import java.awt.image.BufferedImage

  /**
   * Treats a BufferedImage as black & white pack map where all pixels above a certain
   * threshold [0, 1] are considered inside the packing shape
   * 
   * NOTE: This class only considers the first / red-channel of an image
   * You might want to subclass it and override the valueAt method for more advanced thresholding
   */
  class BufferedImageMap(image:BufferedImage, var threshold:Int) extends Map {
    val raster = image.getRaster
    
    def width = image.getWidth.toFloat
    def height = image.getHeight.toFloat
    
    def contains(x:Float, y:Float):Boolean = {
      if(x < 0 || y < 0 || x > width || y > height) 
        return false
      
      raster.getSampleFloat(x.toInt, y.toInt, 0) > threshold
    }
  }
}

/**
 * Packs a set of smaller rectangles into a larger rectangle
 */
class RectanglePacker(var rect:Rect) extends Packer[Rect] {
  
  def this() = this(new Rect())
  
  protected var _map:RectanglePacker.Map = _
  def map = _map
  def map_=(map:RectanglePacker.Map) {
    this._map = map
    rect.width = map.width
    rect.height = map.height
  }

  /** Setting this to an appropriate size can significantly improve performance */
  var minArea = 10f
  
  protected var _mode:RectanglePacker.Mode.Value = _ 
  protected var originMode:RectanglePacker.Origin.Value = _
  protected var origin = Vec2()
  
  // init
  mode = RectanglePacker.Mode.HorizontalDown
  
  def mode = _mode
  def mode_=(mode:RectanglePacker.Mode.Value) {
    this._mode = mode
    
    mode match {
      case RectanglePacker.Mode.HorizontalDown => 
        packingStrategy = packHorizontalDown
        originMode = RectanglePacker.Origin.TopLeft
        
      case RectanglePacker.Mode.VerticalDown => 
        packingStrategy = packVerticalDown
        originMode = RectanglePacker.Origin.TopLeft
        
      case RectanglePacker.Mode.VerticalUp => 
        packingStrategy = packVerticalUp
        originMode = RectanglePacker.Origin.BottomLeft
        
      case RectanglePacker.Mode.VerticalUpUsingMap => 
        if(map == null) {
          warn("This packing mode requires a RectanglePacker.ShapeMap to be set!")
          return this.mode_=(RectanglePacker.Mode.VerticalUp)
        }
        packingStrategy = packVerticalUpUsingShapeMap
        originMode = RectanglePacker.Origin.BottomLeft
    }
  }
  
  def resetOrigin {
     originMode match {
      case RectanglePacker.Origin.TopLeft =>
        origin.x = rect.x1
        origin.y = rect.y1
        
      case RectanglePacker.Origin.BottomLeft =>
        origin.x = rect.x1
        origin.y = rect.y2 - current.height
    }
  }
  
  /** place current rect at origin, then try to find a place in the packing */
  override def placeCurrent = {
    if(index == 0) resetOrigin
    current.x1 = origin.x
    current.y1 = origin.y
    
    val isPlaced = packingStrategy() 
    if(isPlaced && current.area <= minArea) {
      origin.x = current.x1
      origin.y = current.y1
    }
    isPlaced
  }
  
  /** A left-right top-down packing algorithm */
  def packHorizontalDown():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = elements(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.x1 = packed.x2 + margin
        
          if(current.x2 > rect.x2) {
            current.x1 = rect.x1
            current.y1 += margin
          } 
        }
      }
    
      if(!intersects) return true
      if(!rect.contains(current)) return false
    }
    true
  }
  
  /** A top-down left-right packing algorithm */
  def packVerticalDown():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = elements(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y2 + margin
          
          if(current.y2 > rect.y2) {
            current.y1 = rect.y1
            current.x1 += margin
          } 
        }
      }
    
      if(!rect.contains(current)) return false
      if(!intersects) return true
    }
    true
  }
  
  /** A bottom-up left-right packing algorithm */
  def packVerticalUp():Boolean = {
    while(true) {
      var intersects = false
      for(j <- 0 until index) {
        val packed = elements(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y1 - current.height - margin
          
          if(current.y1 <= rect.y1) {
            current.y1 = rect.y2 - current.height
            current.x1 += margin
          } 
        }
      }
      
      if(!rect.contains(current)) return false
      if(!intersects) return true
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
        val packed = elements(j)
      
        if(current.intersects(packed)) {
          intersects = true
          current.y1 = packed.y1 - current.height - margin
        
          if(current.y1 < rect.y1) {
            current.y1 = rect.y2 - current.height
            current.x1 += margin
          } 
        }
      }
      
      if(!intersects) {
        // check if the found position is within the shape
        if(!map.contains(current.centerX - rect.x1, current.centerY - rect.y1)) {
          current.y1 -= margin
      
          if(current.y1 < rect.y1) {
            current.y1 = rect.y2 - current.height
            current.x1 += margin
          }
        } else {
          // within shape & not intersecting
          return true
        }  
      }
    
      if(!rect.contains(current)) return false
    }
    true
  }
}
