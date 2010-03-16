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
class Circle extends Vec2 {

	var radius = 0f
	
	def diameter = 2f*radius
	
	def this(position:Vec, radius:Float) = {
		this()
		this.radius = radius
		this := position
	}

	def this(radius:Float) {
		this()
		this.radius = radius
	}

	/** @return true if this circle contains the given point, false otherwise */
	def contains(p:Vec) = distanceSquared(p) <= radius * radius

	/** @return true if this circle contains the given circle */
	def contains(c:Circle) = distance(c) + c.radius <= radius
	
	/** @return true if this circle intersects with the given circle, false otherwise */
	def intersects(c:Circle) = {
		val delta = c - this
		val d = delta.length
		val r1 = radius
		val r2 = c.radius
		d <= r1 + r2 && d >= math.abs(r1 - r2)
	}
}
