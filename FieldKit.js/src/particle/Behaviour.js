/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: June 20, 2009
 */

// =============================================================================
// PARTICLE BEHAVIOURS
// =============================================================================
fk.particle.Behaviour = fk.Class.extend({
	prepare: function(dt) {},
	apply: function(particle, dt) {}
});

// -- Emitter Behaviours ---------------------------------------------------
fk.particle.RandomEmit = fk.particle.Behaviour.extend({
	apply: function(p, dt) {
		p.position.x = Math.random() * p.flock.ps.width;
		p.position.y = Math.random() * p.flock.ps.height;
	},
});

// -- Continous Behaviours -------------------------------------------------
fk.particle.Gravity = fk.particle.Behaviour.extend({
	apply: function(p, dt) {
		p.steer.add(0.0, 0.01);
	}
});
	
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
	
fk.particle.RandomSteer = fk.particle.Behaviour.extend({
	apply: function(p, dt) {
		p.steer.x += (Math.random() * 2.0 - 1.0) * 0.2;
		p.steer.y += (Math.random() * 2.0 - 1.0) * 0.2;
	}
});
