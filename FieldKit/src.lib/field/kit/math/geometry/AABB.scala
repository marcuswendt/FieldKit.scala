/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

/**
 * Axis-aligned bounding box used for Octrees and other optimisation techniques
 */
class AABB(position:Vec3, var extent:Vec3) extends Vec3 {
  import kit.math.FMath._
  
  var min = new Vec3
  var max = new Vec3
  
  this := position
  updateBounds
  
  // -- Constructors -----------------------------------------------------------  
  def this(extent:Vec3) = 
    this(new Vec3, extent)
  
  def this(position:Vec3, extent:Float) = 
    this(position, new Vec3(extent, extent, extent))
  
  // -- Utilities --------------------------------------------------------------
  def updateBounds {
    min := this -= extent
    max := this += extent
  }
  
  /**
   * @return true, when the given AABB intersects with itself
   */
  def intersects(box:AABB) = {
  	val t = box - this
  	abs(t.x) <= extent.x + box.extent.x &&
    abs(t.y) <= extent.y + box.extent.y &&
    abs(t.z) <= extent.z + box.extent.z
  }

  /**
   * @return true, when the given Sphere intersects with itself
   */
  def intersects(sphere:Sphere):Boolean = intersects(sphere, sphere.radius)
  
  /**
   * @return true, when the given Sphere intersects with itself
   */
  def intersects(center:Vec3, radius:Float) = {
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
  def contains(p:Vec3):Boolean = {
    if(p.x < min.x || p.x > max.x) return false
    if(p.y < min.y || p.x > max.y) return false
    if(p.z < min.z || p.x > max.z) return false
    true
  }
}
