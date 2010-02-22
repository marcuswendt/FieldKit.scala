/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math.geometry

import field.kit.math._

/**
 * Defines a mathematical Circle
 */
class Circle(var radius:Float) extends Vec2 {
  
  def this(position:Vec, radius:Float) = {
    this(radius)
    this := position
  }
  
  /** @return true if this circle contains the given point, false otherwise */
  def contains(p:Vec) = {
    val dx = this.x - p.x
    val dy = this.y - p.y
    val d = dx*dx + dy*dy
    (d <= radius * radius)
  }
  
  /** @return true if this circle intersects with the given circle, false otherwise */
  def intersects(circle:Circle) = {
    (x - circle.x) * (x - circle.x) +
    (y - circle.y) * (y - circle.y) < 
    (radius - circle.radius) * (radius - circle.radius)
  }
}
