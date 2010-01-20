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
// MATH UTILITIES & HELPERS
// =============================================================================
fk.math = {
	PI: Math.PI,
	HALF_PI: Math.PI * 0.5,
	TWO_PI: Math.PI * 2,
	
	// -- Classes ----------------------------------------------------------------
	Rect: function() {
		this.x1 = 0
		this.y1 = 0
		this.x2 = 0
		this.y2 = 0
		
		if(arguments.length==4) {
			this.x1 = arguments[0]
			this.y1 = arguments[1]
			this.x2 = arguments[2]
			this.y2 = arguments[3]
		}
		
		this.intersects = function(r) {
			return r.x2 > this.x1 && 
						 r.y2 > this.y1 &&
			    	 r.x1 < this.x2 && 
			    	 r.y1 < this.y2
		}
		
		this.contains = function(r) {
			return r.x1 >= this.x1 && 
						 r.y1 >= this.y1 &&
			    	 r.x2 <= this.x2 && 
			    	 r.y2 <= this.y2
		}
	},
	
	// -- Utilities --------------------------------------------------------------
	random: function(min, max) {
		return Math.random() * (max - min) + min
	},
	
	randomInt: function(min, max) { 
		return parseInt(Math.random() * (max - min) + min)
	}, 

	slerp: function(cur, to, delta) {
		return cur * (1 - delta) + to * delta
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
}
