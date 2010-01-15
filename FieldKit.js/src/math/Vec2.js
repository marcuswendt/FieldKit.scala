/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: July 05, 2009
 */

// =============================================================================
// Vector 2D class
// =============================================================================
fk.math.Vec2 = fk.Class.extend({
	x: 0, y: 0,

	// -- Setters --------------------------------------------------------------
	set: function(x, y) {
		this.x = x;
		this.y = y;
		return this;
	},
	
	setV: function(v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	},

	setS: function(s) {
		this.x = s;
		this.y = s;
		return this;
	},

	// -- Vector Operations ----------------------------------------------------
	add: function(x, y) { 
		this.x += x;
		this.y += y;
		return this;
	},

	sub: function(x, y) { 
		this.x -= x;
		this.y -= y;
		return this;
	},

	mul: function(x, y) { 
		this.x *= x;
		this.y *= y;
		return this;
	},

	div: function(x, y) { 
		this.x /= x;
		this.y /= y;
		return this;
	},

	// -- Vector Object Operations ---------------------------------------------
	addV: function(v) { 
		this.x += v.x;
		this.y += v.y;
		return this;
	},

	subV: function(v) { 
		this.x -= v.x;
		this.y -= v.y;
		return this;		
	},

	mulV: function(v) { 
		this.x *= v.x;
		this.y *= v.y;
		return this;
	},

	divV: function(v) { 
		this.x /= v.x;
		this.y /= v.y;
		return this;		
	},

	// -- Scalar Operations ----------------------------------------------------
	addS: function(s) { 
		this.x += s;
		this.y += s;
		return this;
	},

	subS: function(s) { 
		this.x -= s;
		this.y -= s;
		return this;
	},

	mulS: function(s) { 
		this.x *= s;
		this.y *= s;
		return this;
	},

	divS: function(s) { 
		this.x /= s;
		this.y /= s;
		return this;
	},

	// -- Other Operations -----------------------------------------------------
	zero: function() { 
		this.x = 0; this.y = 0;
		return this;
	},

	normalize: function() {
		var l = this.length();
		return (l != 0) ? this.divS(l) : this;
	},

	lengthSquared: function() {
		return this.x * this.x + this.y * this.y;
	},

	length: function() { return Math.sqrt(this.lengthSquared()); },

	clamp: function(max) {
		var l = this.length();
		if(l > max) {
			this.divS(l);
			this.mulS(max);
		}
		return this;
	},

	// -- Distance Calculations ------------------------------------------------
	distance: function(x,y) {
		return Math.sqrt(this.distanceSquared(x,y));
	},
	
	distanceV: function(v) {
		return Math.sqrt(this.distanceSquared(v.x,v.y));
	},
	
	distanceSquared: function(x, y) {
		var dx = this.x - x;
    	var dy = this.y - y;
		return dx * dx + dy * dy;
	},
	
	// -- Helpers --------------------------------------------------------------
	toString: function() {
		return "Vec2("+ this.x +","+ this.y +")";
	},
});