/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics
{
	import field.kit.math.*;

	/**
	 * 3D Verlet Particle class with support for multiple behaviours
	 * 
	 * TODO state handling, colour integration
	 */
	public class Particle extends Behavioural
	{
		public var age:Number = 0
	
		/** the radial size of this particle in the physics space */
		public var size:Number = 1
		
		/** mass of this particle */
		protected var _weight:Number
		
		protected var _invWeight:Number
		
		/** Whether this particle can be moved */ 
		public var isLocked:Boolean = false
			
		/** Position in the physical space */
		public var position:Vec
		
		/** Position in the previous frame */
		public var prev:Vec

		/** internal use */
		protected var tmp:Vec
		
		/** Steering force */
		public var force:Vec
		
		/** Air resistance or fluid resistance, force opposed to the relative motion of this particle */
		public var drag:Number = 0.03

		public function Particle() {
			position = new Vec3()
			prev = new Vec3()
			tmp = new Vec3()
			force = new Vec3()
			weight = 1
		}

		// -- Update -----------------------------------------------------------
		public function update(dt:Number):void {
			updateState(dt)
			if(isLocked) return
			updatePosition()
			applyBehaviours()
			applyConstraints()
		}
		
		public function lock():void {
			isLocked = true
		}
		
		public function unlock():void {
			isLocked = false
		}
		
		// -- State Machine ----------------------------------------------------
		protected function updateState(dt:Number):void {
			age += dt
		}
		
		// -- Verlet Integration -----------------------------------------------
		protected function updatePosition():void {
			tmp.set(position)
			
			position.x += (position.x - prev.x) + force.x
			position.y += (position.y - prev.y) + force.y
			position.z += (position.z - prev.z) + force.z
			
			prev.set(tmp)
			scaleVelocity(1.0 - drag)
			force.zero()
		}
		
		/** sets the velocity of this particle to zero */
		public function clearVelocity():void {
			prev.set(position)
		}
		
		public function scaleVelocity(s:Number):void {
			prev.interpolate(position, 1.0 - s)
		}
		
		public function setPosition(v:Vec):void {
			position.set(v)
			clearVelocity()
		}
			
		// -- Behaviours -------------------------------------------------------
		/**
		 * Applies all assigned behaviours to this particle
		 */
		public function applyBehaviours():void {
			if(behaviours == null) return
			var l:int = behaviours.length
			for(var i:int=0; i < l; i++) {
				behaviours[i](this)
			}
		}
		
		// -- Constraints ------------------------------------------------------
		/**
		 * Applies all assigned constraints to this particle
		 */
		public function applyConstraints():void {
			if(constraints == null) return
			var l:int = constraints.length
			for(var i:int=0; i < l; i++) {
				constraints[i](this)
			}
		}

		public function get weight():Number { return _weight }

		public function set weight(value:Number):void {
			_weight = value;
			_invWeight = 1.0 / weight
		}
		
		public function get invWeight():Number { return _invWeight }

		public function toString():String {
			return "Particle[position="+ position +",size="+ size +"]"			
		} 
	}
}