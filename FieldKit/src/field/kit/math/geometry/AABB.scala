/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

import field.kit.math._
import field.kit.math.Common._

/**
 * Axis-aligned bounding box used for Octrees and other optimisation techniques
 */
class AABB extends Vec3 {
  protected val _extent = new Vec3
  
  def extent = _extent
  def extent_=(value:Vec3) {
    _extent := value
    updateBounds
  }
  
  def extent_=(value:Float) {
	_extent := value
	updateBounds
  }
  
  var min = Vec3()
  var max = Vec3()
  updateBounds
  
  // -- Constructors -----------------------------------------------------------
  def this(extent:Vec3) {
	  this()
	  this.extent = extent
  }
  
  def this(position:Vec3, extent:Vec3) {
	  this()
	  this := position
	  this.extent = extent
  }
  
  def this(position:Vec3, extent:Float) {
	  this()
	  this := position
	  this.extent = extent
  }
  
  protected def updateBounds {
    min := this -= extent
    max := this += extent
  }
  
  /**
   * @return true, when this and the given AABB intersect with each other
   */
  def intersects(box:AABB) = {
  	val t = box - this
  	abs(t.x) <= (extent.x + box.extent.x) &&
    abs(t.y) <= (extent.y + box.extent.y) &&
    abs(t.z) <= (extent.z + box.extent.z)
  }

  /**
   * @return true, when the given Sphere intersects with this AABB
   */
  def intersects(sphere:Sphere):Boolean = intersects(sphere, sphere.radius)
  
  /**
   * @return true, when the given Sphere intersects with this AABB
   */
  def intersects(center:Vec, radius:Float) = {
    var s = 0f
    var d = 0f
    
    // find the square of the distance
    // from the sphere to the box
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

    if (center.z < min.z) {
      s = center.z - min.z
      d += s * s
    } else if (center.z > max.z) {
      s = center.z - max.z
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
    if(p.z < min.z || p.z > max.z) return false
    true
  }
}
