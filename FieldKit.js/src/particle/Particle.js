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
 * 2D Particle
 */
fk.particle.Particle = fk.Class.extend({
	
	init: function(flock) {
		this.flock = flock;

		this.position = new fk.math.Vec2();
		this.velocity = new fk.math.Vec2();
		this.steer = new fk.math.Vec2();
		
		this.age = 0;
		this.steerMax = 1.0;
		this.velocityMax = 10;
		this.rotation = 0;
		this.turningSpeed = 0.1;
		this.size = 10;

		// temp fields
		this.theta = 0;
		this.absVelocity = new fk.math.Vec2();
				
		// reset to defaults
		this.reset();
	},

	reset: function() {
		this.velocity.zero();
		this.steer.zero();
		
		this.age = 0;
		this.rotation = 0;
		this.size = 10;
		
		this.theta = 0;
		this.absVelocity.zero();
	},

	updateLogic: function(dt) {
		this.age += dt;
		this.steer.clamp(this.steerMax);
		this.velocity.addV(this.steer).clamp(this.velocityMax);
		this.steer.zero();
		this.theta = -Math.atan2(this.velocity.x, this.velocity.y);
	},
		
	updateSimulation: function(dt) {
		this.absVelocity.setV(this.velocity).mulS(dt / this.flock.ps.updateTicks);
		this.position.addV(this.absVelocity);
		this.velocity.mulS(this.flock.ps.friction);
		this.rotation = fk.math.slerpAngle(this.rotation, this.theta, this.turningSpeed);
	},
});
