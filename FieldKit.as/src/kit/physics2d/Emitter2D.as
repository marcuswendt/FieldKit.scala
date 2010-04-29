/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 14, 2010 */
package field.kit.physics2d
{
	import field.kit.math.Vec2;
	import field.kit.physics.Emitter;
	import field.kit.physics.Particle;
	import field.kit.physics.Physics;
	
	public class Emitter2D extends Emitter
	{
		public function Emitter2D(physics:Physics) {
			super(physics)
			position = new Vec2()
		}
		
		/** creates a new particle object from the parameterized type */
		public override function createParticle():Particle {
			return new Particle2D()
		}
	}
}