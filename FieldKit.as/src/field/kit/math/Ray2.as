/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math
{
	public class Ray2 extends Vec2
	{
		protected var _direction:Vec2 = new Vec2()
		
		public function Ray2(x:Number=0, y:Number=0, dirX:Number=0, dirY:Number=0) {
			super(x, y);
			direction = new Vec2(dirX, dirY)
		}
		
		public function pointAtDistance(dist:Number):Vec2 {
			return Vec2(this.add(direction.mulS(dist)))
		}
		
		public function get direction():Vec2 {
			return _direction;
		}
		
		public function set direction(v:Vec2):void {
			_direction.set(v).normalizeSelf()
		}
	}
}