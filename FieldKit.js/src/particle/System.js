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

// SUBSUMPTION PARTICLE SYSTEM
fk.particle.System = fk.Class.extend({

	init: function(updateTicks, logicTicks) {
		this.updateTicks = updateTicks;
		this.logicTicks = logicTicks;	
		
		this.flocks = new Array();
		this.friction = 0.97;
		this.width = 640;
		this.height = 480;

		this.timerSim = new Date();
		this.timerLogic = new Date();
	},
	
	update: function() {
		var now = new Date();
		var dtSim = now - this.timerSim;
		this.timerSim = now;

		// update logic
		var dtLogic = now - this.timerLogic;
		if(dtLogic > 1000/this.logicTicks) {
			this.timerLogic = now;
			for(var i=0; i<this.flocks.length; i++)
				this.flocks[i].updateLogic(dtLogic);
		}
		
		// update simulation
		for(var i=0; i<this.flocks.length; i++)
			this.flocks[i].updateSimulation(dtSim);
	},

	add: function(flock) { 
		flock.ps = this;
		this.flocks.push(flock) ;
	},
});