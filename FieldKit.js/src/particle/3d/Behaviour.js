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
		
		this.range = new fk.math.Vec3();
		this.rangeAbs = new fk.math.Vec3();
	},
	
	prepare: function(dt) {
		this.ps.toAbsolute(this.min, this.minAbs);
		this.range.setV(this.max).subV(this.min);
		this.ps.toAbsolute(this.range, this.rangeAbs);
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
		
		this.minAbs = new fk.math.Vec3();
		this.maxAbs = new fk.math.Vec3();
	},
	
	prepare: function(dt) {
		this.ps.toAbsolute(this.min, this.minAbs);
		this.ps.toAbsolute(this.max, this.maxAbs);
	},
	
	apply: function(p, dt) {
		var pos = p.position;
		
		var min = this.minAbs;
		var max = this.maxAbs;

		if(pos.x < min.x) pos.x = max.x;
		if(pos.x > max.x) pos.x = min.x;
		
		if(pos.y < min.y) pos.y = max.y;
		if(pos.y > max.y) pos.y = min.y;
		
		if(pos.z < min.z) pos.z = max.z;
		if(pos.z > max.z) pos.z = min.z;
	}
});