/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: January 15, 2010
 */

fk.particle.Flock = fk.Class.extend({

	init: function() {
		this.particles = new Array();
		this.behaviours = new Array();
		this.emitter = new fk.particle.Emitter(this);
	},
	
	updateLogic: function(dt) {
		// update emitter
		this.emitter.updateLogic(dt);

		// prepare behaviours
		for(var i=0; i<this.behaviours.length; i++)
			this.behaviours[i].prepare(dt);

		// update particles
		for(var i=0; i<this.particles.length; i++) {
			var p = this.particles[i];

			for(var j=0; j<this.behaviours.length; j++)
				this.behaviours[j].apply(p, dt);

			p.updateLogic(dt);
		}
	},
	
	updateSimulation: function(dt) {
		for(var i=0; i<this.particles.length; i++)
			this.particles[i].updateSimulation(dt);
	},

	add: function(behaviour) { this.behaviours.push(behaviour); },

	toString: function() { return 'Flock'; },
});