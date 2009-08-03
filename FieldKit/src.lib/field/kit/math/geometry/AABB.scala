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
class AABB extends Vec3 {
  var extent = new Vec3
  var min = new Vec3
  var max = new Vec3
  
  def this(position:Vec3, extent:Vec3) {
    this()
    this := position
    this.extent := extent
    updateBounds
  }
  
  def this(extent:Vec3) {
    this()
  	this.extent := extent
  	updateBounds
  }
  
  def updateBounds {
    min := this -= extent
    max := this += extent
  }
  
  /**
   * @return true, when the given AABB intersects with itself
   */
  def intersects(box:AABB) = {
  	val t = box - this
  	FMath.abs(t.x) <= extent.x + box.extent.x &&
    FMath.abs(t.y) <= extent.y + box.extent.y &&
    FMath.abs(t.z) <= extent.z + box.extent.z
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
