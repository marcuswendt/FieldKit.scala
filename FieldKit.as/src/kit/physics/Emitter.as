/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics
{
	import field.kit.math.Vec;
	import field.kit.math.Vec3;

	/**
	 * Instantiates particles and adds it to the physics system
	 */
	public class Emitter extends Behavioural
	{
		public var position:Vec
			
		public var rate:Number = 1
		public var interval:Number = 1000
		public var max:Number = 100

		protected var physics:Physics
		protected var time:Number
		
		public function Emitter(physics:Physics) {
			this.physics = physics
			position = new Vec3()
		}
		
		public function update(dt:Number):void {
			time += dt
			if(time < interval) return
			time = 0
				
			// emit particles
			var j:int = 0
			while(j < rate && physics.numParticles < max) {
				emit(position)
				j += 1
			}
		}
		
		/** emits a single particle and applies the emitter behaviours */
		public function emit(position:Vec = null):Particle {
			var p:Particle = createParticle()
			
			// set particle to start at the emitters position
			p.setPosition(position == null ? this.position : position) 
			
			// add particle to physics
			physics.addParticle(p)
			
			// apply behaviours
			applyBehaviours(p)
			applyConstraints(p)
			
			return p
		}
			
		/** creates a new particle object from the parameterized type */
		public function createParticle():Particle {
			return new Particle()
		}
		
		// -- Behaviours -------------------------------------------------------
		/**
		 * Applies all assigned behaviours to this particle
		 */
		protected function applyBehaviours(p:Particle):void {
			if(behaviours == null) return
			var l:int = behaviours.length
			for(var i:int=0; i < l; i++) {
				behaviours[i](p)
			}
		}
		
		// -- Constraints ------------------------------------------------------
		/**
		 * Applies all assigned constraints to this particle
		 */
		protected function applyConstraints(p:Particle):void {
			if(constraints == null) return
			var l:int = constraints.length
			for(var i:int=0; i < l; i++) {
				constraints[i](p)
			}
		}
	}
}