/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 13, 2010 */
package field.kit.math.geometry
{
	import field.kit.math.Vec;

	/**
	 * Polyline class
	 * @author Marcus Wendt
	 */
	public class Polyline extends Curve
	{
		private var prev:Vec = createVec()
		private var next:Vec = createVec()
		private var tmp:Vec = createVec()
			
		public function Polyline() {
			super();
		}
		
		/**
		 * Note this only returns the closest vertex point not a point inbetween two vertices
		 */
		public override function point(time:Number, result:Vec=null):Vec {
			if(result == null) result = createVec()
			if(size == 0) return result
				
			// first point
			if(time <= 0 || size <= 1) {
				result.set(vertices[0])
				
			// last point
			} else if(time >= 1) {
				result.set(vertices[size-1])
				
			// in between
			} else {
				var median:Number = time * (size-1)
				
				prev.set(vertices[int(Math.floor(median))])
				next.set(vertices[int(Math.ceil(median))])
					
				tmp.set(next).subSelf(prev)
				result.set(prev).addSelf(tmp)
			}
			
			return result
		}
	}
}