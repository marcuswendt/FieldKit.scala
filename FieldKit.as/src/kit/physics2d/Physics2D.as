/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 14, 2010 */
package field.kit.physics2d
{
	import field.kit.physics.Physics;
	
	public class Physics2D extends Physics
	{
		public function Physics2D() {
			super();
			emitter = new Emitter2D(this);
		}
	}
}