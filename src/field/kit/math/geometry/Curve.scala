/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 16, 2009 */
package field.kit.math.geometry

/**
 * <code>Curve</code> defines a collection of vertices that make up a curve.
 * How this curve is constructed is undefined, and the job of a subclass.
 * Here <code>Vertex</code> means the 'bones' of the curve e.g. control and tangent points
 * where as <code>Point</code> refers to an arbitrary calculated point on this curve
 * 
 * @see Inspired by <a href="http://code.google.com/p/jmonkeyengine/source/browse/trunk/src/com/jme/curve/Curve.java">JMonkeyEngines Curve class</a>
 * @author Marcus Wendt
 */
abstract class Curve(var capacity:Int) {
	import java.nio.FloatBuffer
	import field.kit.math.Vec3

	/** stores the control vertices and tangents */
	var vertices:FloatBuffer = _

	/** the current number of vertices in the list */
	var size = 0

	// init defaults
	init(capacity)

	/** allocates the internal buffers */
	def init(capacity:Int) {
		import field.kit.util.Buffer
		this.capacity = capacity
		vertices = Buffer.float(capacity * 3)
		clear
	}

	/** adds a control point */
	def +=(x:Float, y:Float, z:Float) {
		if(size < capacity) {
			vertices put x
			vertices put y
			vertices put z
			size += 1
		}
	}

	/** adds the given <code>Vec3</codee as control point */
	def +=(v:Vec3):Unit = this += (v.x, v.y, v.z)

	def clear = {
		vertices.clear
		size = 0
	}

	/** @return the nth vertex of this curve as new <code>Vec3</code> */
	def vertex(n:Int):Vec3 = vertex(n, Vec3())

	/** @return sets the given <code>Vec3</code> to the nth vertex of this curve */
	def vertex(n:Int, v:Vec3) = {
		if(n >= 0 && n < size) {
			val index = n * 3
			v.x = vertices get index
			v.y = vertices get index + 1
			v.z = vertices get index + 2
		}
		v
	}

	/** 
	* calculates a point on the curve based on the time, where time is [0, 1]. 
	* How the point is calculated is defined by the subclass. 
	*/
	def point(time:Float):Unit = point(time, Vec3())

	/** 
	* calculates a point on the curve based on the time, where time is [0, 1]. 
	* How the point is calculated is defined by the subclass. 
	*/
	def point(time:Float, result:Object):Unit
	
	/** @return the closest point on this curve to the given point */
	def closestTo(p:Vec3, resolution:Int, result:Vec3 = null) = {
		val r = if(result==null) new Vec3 else result
		
		var i=0
		var distClosest = Float.MaxValue
		
		// TODO make this a member to avoid mem leaks? 
		val tmp = new Vec3

		// go through all curve points and look for closest match
		while(i < resolution) {
			point(i/ resolution.toFloat, tmp)
			val dist = tmp.distanceSquared(p)
			if(dist < distClosest) {
				r := tmp
				distClosest = dist 
			}
			i += 1
		}
		r
	}
}
