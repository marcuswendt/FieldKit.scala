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
	import field.kit.physics.Particle;
	import field.kit.physics.Spring;
	
	public class Spring2D extends Spring
	{
		public function Spring2D(a:Particle=null, b:Particle=null, 
								 strength:Number=0.5, restLength:Number=Number.NaN) {
			super(a, b, strength, restLength);
			
			delta = new Vec2()
			tmp = new Vec2()
		}
	}
}