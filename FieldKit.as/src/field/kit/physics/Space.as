/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics
{
	import field.kit.math.geometry.AABB;

	/**
	 * Defines the 3D space a physics simulation works in
	 * 
	 * TODO add optimisd methods to find particles at arbitrary positions e.g. Octree, Quadtree, Spatial Hashing
	 */
	public class Space extends AABB
	{
		public function Space() {}
		
		public function setSpace(s:Space):void {
			setAABB(s)
		}
	}
}