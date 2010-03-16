package field.kit.physics

import field.kit._

/**
 * Ported from toxiclibs toxi.physics.VerletSpring
 */
class Spring {
	var a:Particle = _
	var b:Particle = _
	
	/** Spring rest length to which it always wants to return too */
	var restLength = 0f
	
	/** Spring strength, possible value range depends on engine configuration */
	var strength = 0f
	
	/** Flag, if either particle is locked in space */
	var isALocked = false
	var isBLocked = false
	
	def this(a:Particle, b:Particle, restLength:Float, strength:Float) {
		this()
		this.a = a
		this.b = b
		this.restLength = restLength
		this.strength = strength
	}
	
	protected val delta = new Vec3
	protected val tmp = new Vec3
	
	def update(applyConstraints:Boolean) {
		delta := b -= a
		val dist = delta.length + EPSILON
		val normDistStrength = (dist - restLength) / (dist * (a.invWeight + b.invWeight)) * strength

		//println("delta", delta, "dist", dist, "normDistStrength", normDistStrength)
		
        if (!a.isLocked && !isALocked) {
        	tmp := delta *= normDistStrength * a.invWeight
        	a += tmp
            if (applyConstraints)
                a.applyConstraints
        }
		
        if (!b.isLocked && !isBLocked) {
            tmp := delta *= -normDistStrength * b.invWeight
        	b += tmp
            if (applyConstraints)
                b.applyConstraints
        }
	}
}
