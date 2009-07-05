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
}
