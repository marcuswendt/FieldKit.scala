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
fk.math.Vec2 = function() {
	this.x = 0
	this.y = 0

	// -- Setters --------------------------------------------------------------
	this.set = function(x, y) {
		this.x = x
		this.y = y
		return this
	}
	
	this.setV = function(v) {
		this.x = v.x
		this.y = v.y
		return this
	}

	this.setS = function(s) {
		this.x = s
		this.y = s
		return this
	}

	// -- Vector Operations ----------------------------------------------------
	this.add = function(x, y) { 
		this.x += x
		this.y += y
		return this
	}

	this.sub = function(x, y) { 
		this.x -= x
		this.y -= y
		return this
	}

	this.mul = function(x, y) { 
		this.x *= x
		this.y *= y
		return this
	}

	this.div = function(x, y) { 
		this.x /= x
		this.y /= y
		return this
	}

	// -- Vector Object Operations ---------------------------------------------
	this.addV = function(v) { 
		this.x += v.x
		this.y += v.y
		return this
	}

	this.subV = function(v) { 
		this.x -= v.x
		this.y -= v.y
		return this			
	}

	this.mulV = function(v) { 
		this.x *= v.x
		this.y *= v.y
		return this
	}

	this.divV = function(v) { 
		this.x /= v.x
		this.y /= v.y
		return this			
	}

	// -- Scalar Operations ----------------------------------------------------
	this.addS = function(s) { 
		this.x += s
		this.y += s
		return this
	}

	this.subS = function(s) { 
		this.x -= s
		this.y -= s
		return this
	}

	this.mulS = function(s) { 
		this.x *= s
		this.y *= s
		return this
	}

	this.divS = function(s) { 
		this.x /= s
		this.y /= s
		return this
	}

	// -- Helpers --------------------------------------------------------------
	this.zero = function() { 
		this.x = 0; this.y = 0 
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
		return this.x * this.x + this.y * this.y
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

	this.toString = function() {
		return "Vec2("+ this.x +","+ this.y +")"
	}
}