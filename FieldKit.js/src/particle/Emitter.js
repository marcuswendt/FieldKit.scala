/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2010 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: January 15, 2010
 */

/**
 * 2D Emitter
 */
fk.particle.Emitter = fk.Class.extend({

	init: function(flock) {
		this.flock = flock
		
		this.position = new fk.math.Vec2();
		this.behaviours = new Array();
		this.rate = 1;
		this.interval = 1000.0;
		this.particleMax = 100;

		// private
		this.time = 0;
	},

	updateLogic: function(dt) {
		this.time += dt;

		if(this.time >= this.interval) {
			this.time = 0;

			if(this.emissionCount() > 0) {
				// prepare behaviours
				for(var i=0; i<this.behaviours.length; i++)
					this.behaviours[i].prepare(dt);

				for(var i=0; i<this.rate; i++)
					if(this.emissionCount() > 0)
						this.emit();
			}
		}
	},

	emissionCount: function() {
		return this.particleMax - this.flock.particles.length;
	},

	emit: function() {
		var p = this.createParticle();
		p.position.setV(this.position);

		// apply behaviours
		for(var i=0; i<this.behaviours.length; i++)
			this.behaviours[i].apply(p, 0);

		// add particle to flock			
		this.flock.particles.push(p);
	},
	
	createParticle: function() {
		return new fk.particle.Particle(this.flock);
	},

	add: function(behaviour) { this.behaviours.push(behaviour) },
});