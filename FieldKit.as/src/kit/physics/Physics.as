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
	 * Manager class to combine Particles, String, Behaviours and Constraints into one simulation
	 * 
	 * TODO neighbour search
	 */
	public class Physics extends Behavioural
	{
		public var space:Space;
		public var emitter:Emitter;
		public var particles:Vector.<Particle> = new Vector.<Particle>();
		
		public function Physics() {
			super()
			space = new Space()
			space.setDimensionS(1000, 1000, 1000)
			emitter = new Emitter(this)
		}
		
		public function update(dt:Number):void {
			if(emitter != null)
				emitter.update(dt)
					
			updateParticles(dt)
			updateSprings()
		}
		
		public function clear():void {
			particles.splice(0, Number.MAX_VALUE)
			
			if(behaviours != null)
				behaviours.splice(0, Number.MAX_VALUE)
			
			if(constraints != null)
				constraints.splice(0, Number.MAX_VALUE)
			
			if(springs != null)
				springs.splice(0, Number.MAX_VALUE)
		}
		
		public function get numParticles():int {
			return particles.length;
		}
		
		public function get numSprings():int {
			return (springs == null) ? 0 : springs.length
		}
		
		// -- Particles --------------------------------------------------------
		protected function updateParticles(dt:Number):void {
			var l:int = particles.length
			for(var i:int =0; i < l; i++) {
				particles[i].update(dt)
			}
		}
		
		/**
		 * Creates a new particle using the emitter
		 */
		public function createParticle(position:Vec = null):Particle {
			if(emitter == null) return null
			return emitter.emit(position)
		}
		
		/**
		 * Adds a new particle to this flock
		 */
		public function addParticle(p:Particle):void {
			particles.push(p)
				
			// add behaviours & constraints  to particle
			if(behaviours != null)
				for(var i:int=0; i<behaviours.length; i++)
					p.addBehaviour(behaviours[i])
			
			if(constraints != null)
				for(var j:int=0; j<constraints.length; j++)
					p.addConstraint(constraints[j])
		}
		
		/**
		 * Removes a particle from this flock
		 */
		public function removeParticle(p:Particle):void {
			var k:Number = particles.indexOf(p)
			if(k != -1)
				particles.splice(k, 1)
			
			// remove behaviours & constraints from particle
			if(behaviours != null)
				for(var i:int=0; i<behaviours.length; i++)
					p.removeBehaviour(behaviours[i])
			
			if(constraints != null)
				for(var j:int=0; j<constraints.length; j++)
					p.removeConstraint(constraints[j])
		}
		
		// -- Behaviours -------------------------------------------------------
		public override function addBehaviour(e:Function):void {
			super.addBehaviour(e)
			// add new behaviour to all particles
			for(var i:int=0; i<particles.length; i++)
				particles[i].addBehaviour(e)
		}
		
		public override function removeBehaviour(e:Function):void {
			super.removeBehaviour(e)
			// remove behaviour from all particles
			for(var i:int=0; i<particles.length; i++)
				particles[i].removeBehaviour(e)
		}
		
		
		// -- Constraints ------------------------------------------------------
		public override function addConstraint(e:Function):void {
			super.addConstraint(e)
			// add new constraint to all particles
			for(var i:int=0; i<particles.length; i++)
				particles[i].addConstraint(e)
		}
		
		public override function removeConstraint(e:Function):void {
			super.removeConstraint(e)
			// remove constraint from all particles
			for(var i:int=0; i<particles.length; i++)
				particles[i].removeConstraint(e)
		}
		
		// -- Springs ----------------------------------------------------------
		public var springs:Vector.<Spring>
		
		public function addSpring(e:Spring):void {
			if(springs == null) springs = new Vector.<Spring>()
			springs.push(e)
		}
		
		public function removeSpring(e:Spring):void {
			if(springs == null) return
	
			var i:Number = springs.indexOf(e)
			if(i != -1)
				springs.splice(i, 1)
		}
		
		/**
		 * Updates all spring connections based on new particle positions
		 */
		protected function updateSprings():void {
			if(springs == null) return
				
			var l:int = springs.length
			for(var i:int =0; i < l; i++) {
				springs[i].update(true)
			}
		}
	}
}