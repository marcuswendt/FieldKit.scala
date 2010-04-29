/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math.geometry
{
	import field.kit.math.MathUtil;
	import field.kit.math.Vec;
	
	/**
	 * BSline aka Bezier-Curve class
	 */
	public class BSpline extends Spline
	{
		protected var s:Number = 1.0 / 6.0
			
		public function BSpline() {
			super();
		}
		
		/**
		 * <code>point</code> calculates a point on a Bezier curve
		 */
		public override function point(time:Number, result:Vec=null):Vec {
			if(result == null) result = createVec()
			if(size == 0) return result
			
			if(needsUpdate) update()
			time = checkTime(time)
				
			// first point
			if(!isClosed && time < 0) {
				result.set(first)
				
			// last point
			} else if(!isClosed && time > 1) {
				result.set(last)
				
			// in between
			} else {
				var timeBetween:Number = time / partPercentage
				var i:int = int(timeBetween)
				var normalizedTime:Number = timeBetween - i
					
				var t:Number = normalizedTime
				var t2:Number = t * normalizedTime
				var t3:Number = t2 * normalizedTime
				i -= 1
				
				if(!isClosed && i == -1) {
					tmp0.set(second).subSelf(first).normalizeSelfTo(MathUtil.EPSILON)
					tmp1.set(first).subSelf(tmp0)
				} else {
					vertex(i, tmp1)
				}
				
				i += 1
				vertex(i, tmp2)
				
				i += 1
				vertex(i, tmp3)
				
				i += 1
				if(!isClosed && i >= size) {
					tmp0.set(beforeLast).subSelf(last).normalizeSelfTo(MathUtil.EPSILON)
					tmp4.set(last).subSelf(tmp0)
				} else {
					vertex(i, tmp4)
				}
				
				// calculate point
				tmp1.mulSelfS(s * (-t3 + 3 * t2 - 3 * t + 1))
				result.set(tmp1)
				
				tmp2.mulSelfS(s * (3 * t3 -6 * t2 + 4))
				result.addSelf(tmp2)
				
				tmp3.mulSelfS(s * (-3 * t3 + 3 * t2 + 3 * t + 1))
				result.addSelf(tmp3)
				
				tmp4.mulSelfS(s* t3)
				result.addSelf(tmp4)
			}
			
			return result
		}
	}
}