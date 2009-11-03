/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math.geometry

/**
 * Defines a mathematical Circle
 */
class Circle(var radius:Float) extends Vec2 {
  
  def this(position:Vec, radius:Float) = {
    this(radius)
    this := position
  }
  
  def contains(p:Vec) = {
    val dx = this.x - p.x
    val dy = this.y - p.y
    val d = dx*dx + dy*dy
    (d <= radius * radius)
  }
}
