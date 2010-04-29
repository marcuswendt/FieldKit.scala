/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics2d
{
	import field.kit.math.Vec2;
	import field.kit.physics.Particle;
	
	/**
	 * 2D Verlet Particle class
	 */
	public class Particle2D extends Particle
	{
		public function Particle2D() {
			super();
			position = new Vec2()
			prev = new Vec2()
			tmp = new Vec2()
			force = new Vec2()
		}
		
		protected override function updatePosition():void {
			tmp.set(position)
			
			position.x += (position.x - prev.x) + force.x
			position.y += (position.y - prev.y) + force.y
			
			prev.set(tmp)
			scaleVelocity(1.0 - drag)
			force.zero()
		}

	}
}