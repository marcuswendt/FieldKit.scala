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
// MATH UTILITIES & HELPERS
// =============================================================================
fk.math = {
	HALF_PI: Math.PI * 0.5,
	TWO_PI: Math.PI * 2,
	
	// -- Utilities --------------------------------------------------------------
	randomInt: function(min, max) { 
		return parseInt(Math.random() * (max - min) + min)
	}, 
	
	slerpAngle: function(cur, to, delta) {
		if (cur < 0 && to > 0) {
			if(Math.abs(cur) > this.HALF_PI &&
				 Math.abs(to) > this.HALF_PI)
					cur += this.TWO_PI
				
		} else if (cur > 0 && to < 0) {
			if(Math.abs(cur) > this.HALF_PI && 
				 Math.abs(to) > this.HALF_PI)
					cur -= this.TWO_PI
		}
		return cur * (1 - delta) + to * delta
	},


	// -- Vector 2D class --------------------------------------------------------
	Vec: function() {
		var v = function() {
			this.x = 0
			this.y = 0
		}
	
		// -- Setters --------------------------------------------------------------
		v.prototype.set = function(x, y) {
			this.x = x
			this.y = y
			return this
		}
		
		v.prototype.setV = function(v) {
			this.x = v.x
			this.y = v.y
			return this
		}
	
		v.prototype.setS = function(s) {
			this.x = s
			this.y = s
			return this
		}
	
		// -- Vector Operations ----------------------------------------------------
		v.prototype.add = function(x, y) { 
			this.x += x
			this.y += y
			return this
		}
	
		v.prototype.sub = function(x, y) { 
			this.x -= x
			this.y -= y
			return this
		}
	
		v.prototype.mul = function(x, y) { 
			this.x *= x
			this.y *= y
			return this
		}
	
		v.prototype.div = function(x, y) { 
			this.x /= x
			this.y /= y
			return this
		}

		// -- Vector Object Operations ---------------------------------------------
		v.prototype.addV = function(v) { 
			this.x += v.x
			this.y += v.y
			return this
		}
	
		v.prototype.subV = function(v) { 
			this.x -= v.x
			this.y -= v.y
			return this			
		}
	
		v.prototype.mulV = function(v) { 
			this.x *= v.x
			this.y *= v.y
			return this
		}
	
		v.prototype.divV = function(v) { 
			this.x /= v.x
			this.y /= v.y
			return this			
		}
	
		// -- Scalar Operations ----------------------------------------------------
		v.prototype.addS = function(s) { 
			this.x += s
			this.y += s
			return this
		}
	
		v.prototype.subS = function(s) { 
			this.x -= s
			this.y -= s
			return this
		}
	
		v.prototype.mulS = function(s) { 
			this.x *= s
			this.y *= s
			return this
		}
	
		v.prototype.divS = function(s) { 
			this.x /= s
			this.y /= s
			return this
		}
	
		// -- Helpers --------------------------------------------------------------
		v.prototype.zero = function() { 
			this.x = 0; this.y = 0 
			return this
		}
	
		v.prototype.normalize = function() {
			var l = this.length()
			if(l != 0)
				return this.divS(l)
			else
				return this
		}
	
		v.prototype.lengthSquared = function() {
			return this.x * this.x + this.y * this.y
		}
	
		v.prototype.length = function() { return Math.sqrt(this.lengthSquared()) }
	
		v.prototype.clamp = function(max) {
			var l = length
			if(l > max) {
				this.divS(l)
				this.mulS(max)
			}
			return this
		}
	
		// v.prototype.toString = function() {
		// 	return "Vec("+ this.x +","+ this.y +")"
		// }
		
		return new v()
	}
}
