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
fk.math.Vec3 = function() {
	this.x = 0
	this.y = 0
	this.z = 0
	
	// -- Setters --------------------------------------------------------------
	this.set = function(x, y, z) {
		this.x = x
		this.y = y
		this.z = z
		return this
	}
	
	this.setV = function(v) {
		this.x = v.x
		this.y = v.y
		this.z = v.z
		return this
	}

	this.setS = function(s) {
		this.x = s
		this.y = s
		this.z = s
		return this
	}

	// -- Vector Operations ----------------------------------------------------
	this.add = function(x, y, z) { 
		this.x += x
		this.y += y
		this.z += z
		return this
	}

	this.sub = function(x, y, z) { 
		this.x -= x
		this.y -= y
		this.z -= z
		return this
	}

	this.mul = function(x, y, z) { 
		this.x *= x
		this.y *= y
		this.z *= z
		return this
	}

	this.div = function(x, y, z) { 
		this.x /= x
		this.y /= y
		this.z /= z
		return this
	}

	// -- Vector Object Operations ---------------------------------------------
	this.addV = function(v) { 
		this.x += v.x
		this.y += v.y
		this.z += v.z
		return this
	}

	this.subV = function(v) { 
		this.x -= v.x
		this.y -= v.y
		this.z -= v.z
		return this			
	}

	this.mulV = function(v) { 
		this.x *= v.x
		this.y *= v.y
		this.z *= v.z		
		return this
	}

	this.divV = function(v) { 
		this.x /= v.x
		this.y /= v.y
		this.z /= v.z
		return this			
	}

	// -- Scalar Operations ----------------------------------------------------
	this.addS = function(s) { 
		this.x += s
		this.y += s
		this.z += s
		return this
	}

	this.subS = function(s) { 
		this.x -= s
		this.y -= s
		this.z -= s
		return this
	}

	this.mulS = function(s) { 
		this.x *= s
		this.y *= s
		this.z *= s
		return this
	}

	this.divS = function(s) { 
		this.x /= s
		this.y /= s
		this.z /= s
		return this
	}

	// -- Helpers --------------------------------------------------------------
	this.zero = function() { 
		this.x = 0; this.y = 0; this.z = 0
		return this
	}

	this.normalize = function() {
		var l = this.length()
		if(l != 0)
			return this.divS(l)
		else
			return this
	}

	this.lengthSquared = function() {
		return this.x * this.x + this.y * this.y + this.z * this.z
	}

	this.length = function() { return Math.sqrt(this.lengthSquared()) }

	this.clamp = function(max) {
		var l = this.length()
		if(l > max) {
			this.divS(l)
			this.mulS(max)
		}
		return this
	}
	
	// -- Distance Calculations --------------------------------------------------
	this.distance = function(x,y,z) {
		return Math.sqrt(this.distanceSquared(x,y,z))
	}
	
	this.distanceV = function(v) {
		return Math.sqrt(this.distanceSquared(v.x,v.y,v.z))
	}
	
	this.distanceSquared = function(x, y, z) {
		var dx = this.x - x
    var dy = this.y - y
    var dz = this.z - z
		return dx * dx + dy * dy + dz * dz
	}

	this.toString = function() {
		return "Vec3("+ this.x +","+ this.y +","+ this.z +")"
	}
}