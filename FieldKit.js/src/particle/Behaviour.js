/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: June 20, 2009
 */

// =============================================================================
// PARTICLE BEHAVIOURS
// =============================================================================
fk.particle.Behaviour = fk.Class.extend({
	prepare: function(dt) {},
	apply: function(particle, dt) {}
});

// -- Emitter Behaviours -------------------------------------------------------
/*
fk.particle.Initialiser = fk.particle.Behaviour.extend({
	
	apply: function(p, dt){
	
	}
});
*/
	
/**
 * Sets the particles position to a random point within the area defined by min, max
 */
fk.particle.RandomEmit = fk.particle.Behaviour.extend({
	
	// reference to the particle system
	ps: null,
	
	// normalized minimum and maximum positions [0,1]
	min: null, max: null,
	
	init: function(ps) {
		this.ps = ps;
		
		this.min = new fk.math.Vec2(0,0);
		this.max = new fk.math.Vec2(1,1);
		
		this.minAbs = new fk.math.Vec2();
		this.rangeAbs = new fk.math.Vec2();
	},
	
	prepare: function(dt) {
		this.minAbs.setV(this.min).mul(this.ps.width, this.ps.height, this.ps.depth)
		this.rangeAbs.setV(this.max).subV(this.min).mul(this.ps.width, this.ps.height, this.ps.depth)
	},
	
	apply: function(p, dt) {
		// range * random + minimum
		p.position.setV(this.rangeAbs).mul(Math.random(), Math.random(), Math.random()).addV(this.minAbs)
	},
});


// -- Steering Behaviours ------------------------------------------------------

fk.particle.Gravity = fk.particle.Behaviour.extend({
	force: null,
	
	init: function() {
		this.force = new fk.math.Vec2(0,0.01);
	},
	
	apply: function(p, dt) {
		p.steer.addV(this.force);
	},
});

fk.particle.RandomSteer = fk.particle.Behaviour.extend({
	apply: function(p, dt) {
		p.steer.x += (Math.random() * 2.0 - 1.0) * 0.2;
		p.steer.y += (Math.random() * 2.0 - 1.0) * 0.2;
	}
});


// -- Simulation Space Behaviours ----------------------------------------------

/**
 * Makes sure the particle never leaves the area defined by min, max
 */
fk.particle.Wrap = fk.particle.Behaviour.extend({
	
	min: new fk.math.Vec2(),
	
	max: new fk.math.Vec2(),
	
	init: function(ps) {
		this.ps = ps;
	},
	
	prepare: function(dt) {
		this.min.set(0,0);
		this.max.set(this.ps.width, this.ps.height);
	},
	
	apply: function(p, dt) {
		var pos = p.position;
		if(pos.x < this.min.x) pos.x = this.max.x;
		if(pos.y < this.min.y) pos.y = this.max.y;
		if(pos.x > this.max.x) pos.x = this.min.x;
		if(pos.y > this.max.y) pos.y = this.min.y;
	}
});
