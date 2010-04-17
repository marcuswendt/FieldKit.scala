/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 16, 2009 */
package field.kit.math.geometry

import field.kit._

/**
 * A Catmull-Rom spline (which is a special form of the cubic hermite curve) implementation, 
 * generates a smooth curve/interpolation from a number of points. 
 * 
 * Used in keyframe-based animation and graphics
 * 
 * @see <a href="http://www.mvps.org/directx/articles/catmull/">Catmull-Rom Splines</a>
 * @see <a href="http://en.wikipedia.org/wiki/Spline_(mathematics)">Spline (mathematics)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Hermite_curve#Catmull.E2.80.93Rom_spline">Hermite Curve</a>
 * @author Marcus Wendt
 */
class Spline(capacity:Int) extends Curve(capacity) {

	// internal fields used for calculating points on the curve
	protected val tmp0 = Vec3()
	protected val tmp1 = Vec3()
	protected val tmp2 = Vec3()
	protected val tmp3 = Vec3()
	protected val tmp4 = Vec3()
	protected val tmpResult = Vec3()

	protected val first = Vec3()
	protected val second = Vec3()
	protected val beforeLast = Vec3()
	protected val last = Vec3()

	protected var needsUpdate = false

	/** alternative constructor */
	def this() = this(100)

	override def +=(x:Float, y:Float, z:Float) {
		super.+=(x,y,z)
		needsUpdate = true
	}

	override def clear {
		super.clear
		needsUpdate = true
	}

	/** needs to be called when modifcations to the control vertices were made */
	def update {
		needsUpdate = false
		vertex(0, first)
		vertex(1, second)
		vertex(size-2, beforeLast)
		vertex(size-1, last)
	}

	/**
	* <code>point</code> calculates a point on a Catmull-Rom curve from a
	* given time value within the interval [0, 1]. If the value is zero or less,
	* the first control point is returned. If the value is one or more, the last
	* control point is returned. Using the equation of a Catmull-Rom Curve, the
	* point at the interval is calculated and returned.
	* 
	* @see field.kit.math.geometry.Curve#point(float)
	*/
	def point(time:Float, result:Object) {
		import java.nio.FloatBuffer

		if(size > 0) {
			if(needsUpdate) update

			// first point
			if(time < 0) {
				tmpResult := first

			// last point
			} else if(time > 1) {
				tmpResult := last

			// in between
			} else {
				val partPercentage = 1.0f / (size - 1)
				val timeBetween = time / partPercentage
				
				// this is maybe the more proper way to do it
				//var i = java.lang.Math.floor(timeBetween).asInstanceOf[Int]
				
				// however just casting toInt gives us a considerable performance boost when using large numbers of curves
				var i = timeBetween.toInt

				val normalizedTime = timeBetween - i

				val t = normalizedTime * 0.5f
				val t2 = t * normalizedTime
				val t3 = t2 * normalizedTime
				i -= 1

				if(i == -1) {
					(tmp0 := second -= first).normalizeTo(EPSILON)
					tmp1 := first -= tmp0
				} else {
					tmp1 := (vertices, i)
				}

				i += 1
				tmp2 := (vertices, i)

				i += 1
				tmp3 := (vertices, i)

				i += 1
				if(i == size) {
					(tmp0 := beforeLast -= last).normalizeTo(EPSILON)
					tmp4 := last -= tmp0
				} else {
					tmp4 := (vertices, i)
				}

				// calculate point
				tmp1 *= -t3 + 2 * t2 -t
				tmpResult := tmp1

				tmp2 *= 3 * t3 - 5 * t2 + 1
				tmpResult += tmp2

				tmp3 *= -3 * t3 + 4 * t2 + t
				tmpResult += tmp3

				tmp4 *= t3 - t2
				tmpResult += tmp4
			}

			// set result
			result match {
				case b:FloatBuffer => 
					b put tmpResult.x
					b put tmpResult.y
					b put tmpResult.z
	
				case v:Vec3 => 
					v := tmpResult
			}
		}
	}
}