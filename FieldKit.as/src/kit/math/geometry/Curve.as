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
	import field.kit.math.Vec2;

	/**
	 * Defines a collection of vertices that make up a curve.
	 * How this curve is constructed is the job of a subclass. 
	 */
	public class Curve
	{
		public var vertices:Vector.<Vec> = new Vector.<Vec>
		public var isClosed:Boolean = false
			
		public function Curve() {}
		
		public function addVertex(v:Vec):void {
			vertices.push(v)
		}
		
		public function get size():int {
			return vertices.length
		}
		
		public function clear():void {
			vertices.splice(0, Number.MAX_VALUE)
		}
		
		/** sets the given <code>Vec3</code> to the nth vertex of this curve */
		public function vertex(n:int, result:Vec=null):Vec {
			if(result == null) result = createVec()
				
			// curve is open
			if(!isClosed) {
				if(n >= vertices.length) return null
					
			// curve is closed
			} else {
//				if(n >= vertices.length) n -= vertices.length - 1
				if(n < 0) n += vertices.length
				n %= vertices.length
			}
			
			result.set(vertices[n])
			return result
		}
		
		/** Override this */
		public function point(time:Number, result:Vec=null):Vec {
			return null
		}
		
		public function checkTime(t:Number):Number {
			if(isClosed) {
				return t % 1.0
			} else {
				return t
			}
		}
		/** subclasses could override this to use other dimensions than the default 2 */
		protected static function createVec():Vec {
			return new Vec2()
		}
	}
}