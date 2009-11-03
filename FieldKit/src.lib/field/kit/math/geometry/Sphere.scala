/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

/**
 * Defines a mathematical Sphere
 */
class Sphere(var radius:Float) extends Vec3 {
  
  def this(position:Vec, radius:Float) = {
    this(radius)
    this := position
  }
  
  def contains(p:Vec) = {
    val d = (this - p).lengthSquared
    (d <= radius * radius)
  }
}
