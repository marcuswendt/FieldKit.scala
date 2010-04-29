/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 11, 2010 */
package field.kit.physics
{
	import field.kit.math.MathUtil;
	import field.kit.math.Vec;
	import field.kit.math.Vec2;
	import field.kit.math.Vec3;

	public class Spring
	{
		public var a:Particle
		public var b:Particle
		
		/** Spring rest length to which it always wants to return too */
		public var restLength:Number
		
		/** Spring strength, possible value range depends on engine configuration */
		public var strength:Number
		
		/** Flag, if either particle is locked in space */
		public var isALocked:Boolean = false
		public var isBLocked:Boolean = false
		
		// internal
		protected var delta:Vec
		protected var tmp:Vec

		public function Spring(a:Particle=null, b:Particle=null,
							   strength:Number = 0.5,
							   restLength:Number = Number.NaN) {
			this.a = a
			this.b = b
			this.strength = strength
				
			if(isNaN(restLength))
				this.restLength = a.position.distance(b.position) + MathUtil.EPSILON
				
			// create temp vectors
			delta = new Vec3
			tmp = new Vec3
		}		
		
		/**
		 * Updates both a & b's positions taking their weight and the springs strength and restlength into account
		 */
		public function update(applyConstraints:Boolean):void {				
			delta.set(b.position).subSelf(a.position)
			var dist:Number = delta.length
				
			if(dist == 0) {
				trace("WARNING: distance between two spring particles is 0!", a.position, b.position)
//				return
				dist = 0.1
			}
			
			var normDistStrength:Number = (dist - restLength) / (dist * (a.invWeight + b.invWeight)) * strength
			
			if(!a.isLocked && !isALocked) {
				tmp.set(delta).mulSelfS(normDistStrength * a.invWeight)
				a.position.addSelf(tmp)
				if(applyConstraints)
					a.applyConstraints()
			}
			
			if(!b.isLocked && !isBLocked) {
				tmp.set(delta).mulSelfS(-normDistStrength * b.invWeight)
				b.position.addSelf(tmp)
				if(applyConstraints)
					b.applyConstraints()
			}
		}
	}
}