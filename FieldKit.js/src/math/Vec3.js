/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: July 05, 2009
 */

// =============================================================================
// Vector 3D class
// =============================================================================
fk.math.Vec3 = fk.Class.extend({
	x: 0, y: 0, z: 0,
	
	init: function() {
		switch(arguments.length) {
			case 1:
				this.setV(arguments[0]);
				break;
			
			case 3:
				this.set(arguments[0], arguments[1], arguments[2]);
				break;
		};
	},
	
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
	/** Resets this vectors components all to zero */
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
	
	/** Calculates the dot product of this vector a provided vector. */
	dot: function(v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	},
	
	/** Calculates the cross product of this vector with a parameter vector v. */
	cross: function(v, result) {
    	var rx = (this.y * v.z) - (this.z * v.y);
    	var ry = (this.z * v.x) - (this.x * v.z);
    	var rz = (this.x * v.y) - (this.y * v.x);
		
		var r = (result==undefined) ? new fk.math.Vec3() : result;
		r.set(rx, ry, rz);
		return r;
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

	equals: function(x,y,z) {
		this.x == x && this.y == y && this.z == z;
	},
	
	toString: function() {
		return "Vec3("+ this.x +","+ this.y +","+ this.z +")";
	},
});

// unit vectors
fk.math.Vec3.ZERO = new fk.math.Vec3(0,0,0); 
fk.math.Vec3.UNIT_X = new fk.math.Vec3(1,0,0);
fk.math.Vec3.UNIT_Y = new fk.math.Vec3(0,1,0);
fk.math.Vec3.UNIT_Z = new fk.math.Vec3(0,0,1);

