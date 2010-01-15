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
// Vector 3D class
// =============================================================================
fk.math.Vec3 = fk.Class.extend({
	x: 0, y: 0, z: 0,
	
	// -- Setters --------------------------------------------------------------
	set: function(x, y, z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	},
	
	setV: function(v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	},

	setS: function(s) {
		this.x = s;
		this.y = s;
		this.z = s;
		return this;
	},

	// -- Vector Operations ----------------------------------------------------
	add: function(x, y, z) { 
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	},

	sub: function(x, y, z) { 
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	},

	mul: function(x, y, z) { 
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	},

	div: function(x, y, z) { 
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	},

	// -- Vector Object Operations ---------------------------------------------
	addV: function(v) { 
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	},

	subV: function(v) { 
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;		
	},

	mulV: function(v) { 
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;		
		return this
	},

	divV: function(v) { 
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		return this;		
	},

	// -- Scalar Operations ----------------------------------------------------
	addS: function(s) { 
		this.x += s;
		this.y += s;
		this.z += s;
		return this;
	},

	subS: function(s) { 
		this.x -= s;
		this.y -= s;
		this.z -= s;
		return this;
	},

	mulS: function(s) { 
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	},

	divS: function(s) { 
		this.x /= s;
		this.y /= s;
		this.z /= s;
		return this;
	},

	// -- Helpers --------------------------------------------------------------
	zero: function() { 
		this.x = 0; this.y = 0; this.z = 0;
		return this;
	},

	normalize: function() {
		var l = this.length();
		return (l != 0) ? this.divS(l) : this;
	},

	length: function() { return Math.sqrt(this.lengthSquared()); },

	lengthSquared: function() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	},

	clamp: function(max) {
		var l = this.length();
		if(l > max) {
			this.divS(l);
			this.mulS(max);
		}
		return this;
	},
	
	// -- Distance Calculations ------------------------------------------------
	distance: function(x,y,z) {
		return Math.sqrt(this.distanceSquared(x,y,z));
	},
	
	distanceV: function(v) {
		return Math.sqrt(this.distanceSquared(v.x,v.y,v.z));
	},
	
	distanceSquared: function(x, y, z) {
		var dx = this.x - x;
    	var dy = this.y - y;
    	var dz = this.z - z;
		return dx * dx + dy * dy + dz * dz;
	},

	toString: function() {
		return "Vec3("+ this.x +","+ this.y +","+ this.z +")";
	},
});