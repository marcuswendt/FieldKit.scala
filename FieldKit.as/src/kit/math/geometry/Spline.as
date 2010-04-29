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

	public class Spline extends Curve
	{
		// internal fields used for calculating points on the curve
		protected var first:Vec = createVec()
		protected var second:Vec = createVec()
		protected var beforeLast:Vec = createVec()
		protected var last:Vec = createVec()
		
		protected var tmp0:Vec = createVec()
		protected var tmp1:Vec = createVec()
		protected var tmp2:Vec = createVec()
		protected var tmp3:Vec = createVec()
		protected var tmp4:Vec = createVec()
			
		protected var needsUpdate:Boolean = false

		protected var partPercentage:Number = 0
			
		public function Spline() {
			super();
		}
		
		public override function addVertex(v:Vec):void {
			super.addVertex(v)
			needsUpdate = true
		}
		
		public override function clear():void {
			super.clear()
			needsUpdate = true
		}
		
		/** has to be called when modifcations to the control vertices were made */
		public function update():void {
			needsUpdate = false
				
			if(size < 4) return
				
			first.set(vertices[0])
			second.set(vertices[1])
			beforeLast.set(vertices[vertices.length - 2])
			last.set(vertices[vertices.length - 1])

			if(isClosed) {
				partPercentage = 1.0 / size
			} else {
				partPercentage = 1.0 / (size - 1)
			}
		}
						
		/**
		 * <code>point</code> calculates a point on a Catmull-Rom curve from a
		 * given time value within the interval [0, 1]. If the value is zero or less,
		 * the first control point is returned. If the value is one or more, the last
		 * control point is returned. Using the equation of a Catmull-Rom Curve, the
		 * point at the interval is calculated and returned.
		 */
		public override function point(time:Number, result:Vec=null):Vec {
			if(result == null) result = createVec()
			if(size == 0) return result
				
			if(needsUpdate) update()
			time = checkTime(time)
				
			// first point
			if(time < 0) {
				result.set(first)
				
			// last point
			} else if(time > 1) {
				result.set(last)
				
			// in between
			} else {
				var timeBetween:Number = time / partPercentage
				
				var i:int = int(timeBetween)				
				var normalizedTime:Number = timeBetween - i
				
				var t:Number = normalizedTime * 0.5
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
				tmp1.mulSelfS(-t3 + 2 * t2 - t)
				result.set(tmp1)
				
				tmp2.mulSelfS(3 * t3 - 5 * t2 + 1)
				result.addSelf(tmp2)
				
				tmp3.mulSelfS(-3 * t3 + 4 * t2 + t)
				result.addSelf(tmp3)
				
				tmp4.mulSelfS(t3 - t2)
				result.addSelf(tmp4)
			}
			
			return result
		}
	}
}