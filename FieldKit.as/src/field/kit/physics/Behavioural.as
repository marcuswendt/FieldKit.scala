/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics
{
	public class Behavioural
	{
		public function Behavioural() {}
		
		// -- Behaviours -------------------------------------------------------
		protected var behaviours:Vector.<Function>
		
		public function addBehaviour(e:Function):void {
			if(behaviours == null) behaviours = new Vector.<Function>()
			behaviours.push(e)
		}
		
		public function removeBehaviour(e:Function):void {
			if(behaviours == null) return
			var i:Number = behaviours.indexOf(e)
			if(i != -1)
				behaviours.splice(i, 1)
		}
		
		// -- Constraints ------------------------------------------------------
		protected var constraints:Vector.<Function>
		
		public function addConstraint(e:Function):void {
			if(constraints == null) constraints = new Vector.<Function>()
			constraints.push(e)
		}
		
		public function removeConstraint(e:Function):void {
			if(constraints == null) return
			var i:Number = constraints.indexOf(e)
			if(i != -1)
				constraints.splice(i, 1)
		}
	}
}