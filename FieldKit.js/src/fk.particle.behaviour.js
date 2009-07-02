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
fk.particle.behaviour = {
	Behaviour: function() {
		this.prepare = function(dt) {}
		this.apply = function(particle, dt) {}
	},
	
	// -- Emitter Behaviours -----------------------------------------------------
	RandomEmit: function() {
		this.prepare = function(dt) {}
		this.apply = function(p, dt) {
			p.position.x = Math.random() * p.flock.ps.width()
			p.position.y = Math.random() * p.flock.ps.height()
		}
	},
	
	// -- Continous Behaviours ---------------------------------------------------
	Gravity: function() {
		this.prepare = function(dt) {}
		this.apply = function(p, dt) {
			p.steer.add(0.0, 0.01)
		}
	},
	
	Wrap: function(ps) {
		this.min = new fk.math.Vec()
		this.max = new fk.math.Vec()
		
		this.prepare = function(dt) {
			this.min.set(0,0)
			this.max.set(ps.width(), ps.height())
		}
		
		this.apply = function(p, dt) {
			var pos = p.position
			if(pos.x < this.min.x) pos.x = this.max.x
			if(pos.y < this.min.y) pos.y = this.max.y
			if(pos.x > this.max.x) pos.x = this.min.x
			if(pos.y > this.max.y) pos.y = this.min.y
		}	
	},
	
	RandomSteer: function() {
		this.prepare = function(dt) {}
		this.apply = function(p, dt) {
			p.steer.x += (Math.random() * 2.0 - 1.0) * 0.2
			p.steer.y += (Math.random() * 2.0 - 1.0) * 0.2
		}
	},
}
