/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle

/** represents a cubic space */
class Space(w:Float, h:Float, d:Float) {
  import field.kit.math._
  
  protected val _center = new Vec3
  protected var _dimension = new Vec3(w, h, d)
  update
  
  def this() = this(1000f, 1000f, 1000f)
  
  def set(w:Float, h:Float, d:Float) {	
    _dimension.set(w,h,d)
    update
  }
  
  def update {
    _center.x = _dimension.x * .5f
    _center.y = _dimension.y * .5f
    _center.z = _dimension.z * .5f
  }
  
  // getters
  def width = _dimension.x
  def height = _dimension.y
  def depth = _dimension.z
  def dimension = _dimension
  def center = _center
}