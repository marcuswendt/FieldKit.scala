/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math.geometry

import field.kit.math._

/**
 * Companion update to class <code>AABR</code>
 */
object AABR {
  // factory methods
  def apply() = new AABR(Vec2())
  def apply(position:Vec, extent:Float) = {
    val r = new AABR(Vec2(extent))
    r := position
    r.updateBounds
    r
  }
}

/**
 * Axis-aligned bounding rect used for Quadtrees and other optimisation techniques
 */
class AABR(var extent:Vec2) extends Vec2(0,0) {
  import field.kit.math.Common._
  
  var min = Vec2()
  var max = Vec2()
  
  def this(position:Vec2, extent:Vec2) = {
    this(extent)
    this := position
    updateBounds
  } 
  
  // -- Utilities --------------------------------------------------------------
  def updateBounds {
    min := this -= extent
    max := this += extent
  }
  
  /**
   * @return true, when the given AABB intersects with itself
   */
  def intersects(rect:AABR) = {
  	val t = rect - this
  	abs(t.x) <= (extent.x + rect.extent.x) &&
    abs(t.y) <= (extent.y + rect.extent.y)
  }

  /**
   * @return true, when the given Sphere intersects with itself
   */
  def intersects(circle:Circle):Boolean = intersects(circle, circle.radius)
  
  /**
   * @return true, when the given Sphere intersects with itself
   */
  def intersects(center:Vec, radius:Float) = {
    var s = 0f
    var d = 0f
    
    // find the square of the distance
    // from the circle to the rect
    if (center.x < min.x) {
      s = center.x - min.x
      d = s * s
    } else if (center.x > max.x) {
      s = center.x - max.x
      d += s * s
    }

    if (center.y < min.y) {
      s = center.y - min.y
      d += s * s
    } else if (center.y > max.y) {
      s = center.y - max.y
      d += s * s
    }
    
    d <= radius * radius
  }
  
  /** 
   * @return true, if the given Vec3 lies within this bounding volume 
   */
  def contains(p:Vec):Boolean = {
    if(p.x < min.x || p.x > max.x) return false
    if(p.y < min.y || p.y > max.y) return false
    true
  }
  
  /** @return the width of this rectangle */
  def width = max.x - min.x

  def width_=(value:Float) = max.x = min.x + value
  
  /** @return the height of this rectangle */
  def height = max.y - min.y
  
  def height_=(value:Float) = max.y = min.y + value
}
