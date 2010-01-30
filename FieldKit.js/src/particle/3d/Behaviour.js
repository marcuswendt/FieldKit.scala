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
// 3D PARTICLE BEHAVIOURS
// =============================================================================

// -- Emitter Behaviours -------------------------------------------------------

/**
 * 3D random area emitter
 */
fk.particle3d.RandomEmit = fk.particle.Behaviour.extend({
	
	// reference to the particle system
	ps: null,
	
	// normalized minimum and maximum positions [0,1]
	min: null, max: null,
	
	init: function(ps) {
		this.ps = ps;
		
		this.min = new fk.math.Vec3(0,0,0);
		this.max = new fk.math.Vec3(1,1,1);
		
		this.minAbs = new fk.math.Vec3();
		this.rangeAbs = new fk.math.Vec3();
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

/**
 * Simple 3D Gravity
 */
fk.particle3d.Gravity = fk.particle.Behaviour.extend({
	force: null,
	
	init: function() {
		this.force = new fk.math.Vec3(0,0.01,0);
	},
	
	apply: function(p, dt) {
		p.steer.addV(this.force);
	},
});

/**
 * 3D Space Wrap
 */
fk.particle3d.Wrap = fk.particle.Behaviour.extend({
	min: new fk.math.Vec3(),
	max: new fk.math.Vec3(),
	
	init: function(ps) {
		this.ps = ps;
	},
	
	prepare: function(dt) {
		this.min.set(0,0,0);
		this.max.set(this.ps.width, this.ps.height, this.ps.depth);
	},
	
	apply: function(p, dt) {
		var pos = p.position;
		if(pos.x < this.min.x) pos.x = this.max.x;
		if(pos.x > this.max.x) pos.x = this.min.x;
		
		if(pos.y < this.min.y) pos.y = this.max.y;
		if(pos.y > this.max.y) pos.y = this.min.y;
		
		if(pos.z < this.min.z) pos.z = this.max.z;
		if(pos.z > this.max.z) pos.z = this.min.z;
	}
});