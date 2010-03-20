/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

import field.kit._

/**
 * Axis-aligned bounding box used for Octrees and other optimisation techniques
 */
class AABB extends BoundingVolume {
	
	protected val _extent = new Vec3

	private val tmp = new Vec3
	
	var min = Vec3()
	var max = Vec3()

	// -- Constructors ---------------------------------------------------------
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

	// -- Setters --------------------------------------------------------------
	override def :=(v:Vec) = {
		super.:=(v)
		updateBounds
	}

	override def :=(v:Float) = {
		super.:=(v)
		updateBounds
	}
	
	/** Sets this AABB to be an exact copy of the passed in AABB */
	def :=(box:AABB) {
		super.:=(box)
		this.extent = box.extent
	}


	def updateBounds = {
		min := this -= extent
		max := this += extent
		this
	}

	// -- Getters & Setters ----------------------------------------------------
	def extent = _extent
	def extent_=(value:Vec3) {
		_extent := value
		updateBounds
	}

	def extent_=(value:Float) {
		_extent := value
		updateBounds
	}

	def width = extent.x * 2f	
	def width_=(v:Float) {
		extent.x = v * 0.5f
		updateBounds
	}
	
	def height = extent.y * 2f
	def height_=(v:Float) {
		extent.y = v * 0.5f
		updateBounds
	}
	
	def depth = extent.z * 2f
	def depth_=(v:Float) {
		extent.z = v * 0.5f
		updateBounds
	}
	
	def halfWidth = extent.x
	def halfHeight = extent.y
	def halfDepth = extent.z
	
	// -- Bounding Volume methods ----------------------------------------------
	
	def size = extent.x * 2f
	
	def size_=(value:Float) {
		extent.normaliseTo(value)
		updateBounds
	}
	
	/** 
	* @return true, if the given Vec3 lies within this bounding volume 
	*/
	final def contains(p:Vec):Boolean = {
		if(p.x < min.x || p.x > max.x) return false
		if(p.y < min.y || p.y > max.y) return false
		if(p.z < min.z || p.z > max.z) return false
		true
	}
	
	/**
	* @return true, when this and the given AABB intersect with each other
	*/
	final def intersects(box:AABB) = {
		tmp.set(box) -= this
		abs(tmp.x) <= (extent.x + box.extent.x) &&
		abs(tmp.y) <= (extent.y + box.extent.y) &&
		abs(tmp.z) <= (extent.z + box.extent.z)
	}

	/**
	* @return true, when the given Sphere intersects with this AABB
	*/
	final def intersects(sphere:Sphere):Boolean = intersects(sphere, sphere.radius)

	/**
	* @return true, when the given Sphere intersects with this AABB
	*/
	final def intersects(center:Vec, radius:Float) = {
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
}
