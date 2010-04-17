/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created Februrary 05, 2010 */
package field.kit.math.geometry

import field.kit._

/**
* <code>BSpline</code> Curve
* @author Marcus Wendt
*/
class BSpline(capacity:Int) extends Spline(capacity) {

	override def point(time:Float, result:Object) {
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
				var i = floor(timeBetween).asInstanceOf[Int]

				val normalizedTime = timeBetween - i

				//val t = normalizedTime * 0.5f
				val t = normalizedTime
				val t2 = t * t
				val t3 = t2 * t
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
				if(i >= size) {
					(tmp0 := beforeLast -= last).normalizeTo(EPSILON)
					tmp4 := last -= tmp0
				} else {
					tmp4 := (vertices, i)
				}

				// calculate point
				val s = 1.0f/6f
				tmp1 *= s*(-t3 +3f*t2 -3f*t +1f)
				tmpResult := tmp1

				tmp2 *= s*(3f*t3 -6f*t2+4f)
				tmpResult += tmp2

				tmp3 *= s*(-3f*t3 +3f*t2 +3f*t +1f)
				tmpResult += tmp3

				tmp4 *= s*(t3) 
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