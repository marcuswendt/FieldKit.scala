/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2010 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: June 20, 2009
 */

/**
 * 3D Particle
 */
fk.particle3d.Particle = fk.particle.Particle.extend({
	
	init: function(flock) {
		this._super(flock);
		
		this.position = new fk.math.Vec3();
		this.velocity = new fk.math.Vec3();
		this.steer = new fk.math.Vec3();
		
		this.absVelocity = new fk.math.Vec3();
	},
});