/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, FIELD                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

import field.kit.math._

/**
 * Defines a mathematical Sphere (also used as Bounding Volume)
 */
class Sphere extends BoundingVolume {
	
	var radius = 0f
	
	def this(position:Vec, radius:Float) {
		this()
		this.radius = radius
		this := position
	}
	
	def this(radius:Float) {
		this()
		this.radius = radius
	}

	private val tmp = new Vec3
	
	def contains(p:Vec) = {
		tmp.set(this) -= p
		(tmp.lengthSquared <= radius * radius)
	}
	
	def intersects(s:Sphere) = {
		val delta = s - this
		val d = delta.length
		val r1 = radius
		val r2 = s.radius
		d <= r1 + r2 && d >= Math.abs(r1 - r2)
	}
	
	def intersects(box:AABB) = box.intersects(this)
}
