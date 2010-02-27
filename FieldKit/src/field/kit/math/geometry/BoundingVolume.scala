/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 27, 2010 */
package field.kit.math.geometry

import field.kit._

/**
 * Base trait for all Bounding Volumes (Sphere and AABB)
 */
trait BoundingVolume extends Vec3 {
	/** @return true, when the given point lies within this bounding volume */
	def contains(p:Vec):Boolean
	
	final def intersects(v:BoundingVolume):Boolean = {
		v match {
			case box:AABB => intersects(box)
			case sphere:Sphere => intersects(sphere)
			case _ => false
		}
	}
	
	def intersects(box:AABB):Boolean
	
	def intersects(sphere:Sphere):Boolean
}
