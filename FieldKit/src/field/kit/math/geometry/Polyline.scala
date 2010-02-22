/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created Februrary 16, 2010 */
package field.kit.math.geometry

import field.kit._

import java.nio.FloatBuffer

/**
 * Polyline class
 * @author Marcus Wendt
 */
class Polyline(capacity:Int) extends Curve(capacity) {

	protected val tmpResult = Vec3()
	protected val tmp0 = Vec3()
	protected val tmp1 = Vec3()
	protected val tmp2 = Vec3()

	override def point(time:Float, result:Object) {
		// first point
		if(time <= 0 || size <= 1) {
			vertex(0, tmpResult)
		
		// last point
		} else if(time >= 1) {
			vertex(size-1, tmpResult)
			
		// in between
		} else {
			val median = time * size
			val prev = vertex(floor(median).toInt, tmp0)
			val next = vertex(ceil(median).toInt, tmp1)
			
			tmp2 := next -= prev *= 0.5f
			tmpResult := prev += tmp2
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