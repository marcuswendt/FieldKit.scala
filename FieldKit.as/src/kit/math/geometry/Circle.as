/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math.geometry
{
	import field.kit.math.Vec;
	import field.kit.math.Vec2;

	/**
	 * Defines a mathematical Circle
	 */
	public class Circle extends Vec2
	{
		public var radius:Number
		
		public function Circle(x:Number=0, y:Number=0, r:Number=0) {
			super(x, y);
			this.radius = r
		}
		
		public function get diameter():Number {
			return radius * 2.0;
		}
		
		public function contains(p:Vec):Boolean {
			return distanceSquared(p) <= radius * radius;
		}
		
		public function point(alpha:Number, result:Vec2=null):Vec2 {
			if(result == null) result = new Vec2()
			result.x = this.x + Math.sin(alpha) * radius
			result.y = this.y + Math.cos(alpha) * radius
			return result
		}
		
		public function intersects(c:Circle):Boolean {
			var delta:Vec = c.sub(this)
			var d:Number = delta.length
			var r1:Number = radius
			var r2:Number = c.radius
			return d <= r1 + r2 && d >= Math.abs(r1 - r2)
		}
		
		public override function toString():String {
			return "Circle["+ x +","+ y +",radius="+ radius +"]"			
		}
	}
}