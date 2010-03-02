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
	
	// -- Constructors ---------------------------------------------------------
	def this(position:Vec, radius:Float) {
		this()
		this := position
		this.radius = radius
	}
	
	def this(radius:Float) {
		this()
		this.radius = radius
	}

	// -- Sphere properties ----------------------------------------------------
	final def diameter = radius * 2f
	
	final def diameter_=(value:Float) = this.radius = value*0.5f	
	
	/** Sets this sphere to be an exact copy of the passed in sphere */
	def :=(sphere:Sphere) {
		super.:=(sphere)
		this.radius = radius
	}
	
	// -- Bounding Volume methods ----------------------------------------------
	final def size = diameter
	
	final def size_=(value:Float) = diameter = value
	
	private val tmp = new Vec3
	
	final def contains(p:Vec) = {
		tmp.set(this) -= p
		(tmp.lengthSquared <= radius * radius)
	}
	
	final def intersects(s:Sphere) = {
		val delta = s - this
		val d = delta.length
		val r1 = radius
		val r2 = s.radius
		d <= r1 + r2 && d >= Math.abs(r1 - r2)
	}
	
	final def intersects(box:AABB) = box.intersects(this)
}
