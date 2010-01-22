/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: July 08, 2009
 */

// =============================================================================
// COLOUR CLASS
//
// For more info about HSV and HSL see
// http://en.wikipedia.org/wiki/HSL_and_HSV
//
// Uses RGB <> HSV, HSL conversion code from Michael Jackson
// http://www.mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
//
// =============================================================================
fk.Colour = fk.Class.extend({
	
	r:0, g:0, b: 0,

	init: function() {
		// parse arguments
		if(arguments.length == 3) {
			this.set(arguments[0], arguments[1], arguments[2])
			
		} else if(arguments.length == 1) {
			this.set(arguments[0])
		}
	},
	
	// -- Getters & Setters ------------------------------------------------------
	// Sets the R,G,B values of this Colour object
	// Can take either another Colour object, an RGB(r,g,b) String or three seperate R,G,B values as arguments
	set: function() {
		if(arguments.length == 1) {
			var arg = arguments[0]
			
			if(typeof(arg) == 'string') {
				// arg is a 6 character hex string
				if (arg.length == 6) {
					this.fromHex(arg);
				}
				// arg is in rgb(r,g,b) form
				else {
					var parts = arg.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/)
					this.red(parts[1]);
					this.green(parts[2]);
					this.blue(parts[3]);
				}
				
			} else if(typeof(arg) == 'object') {
				this.red(arg.r);
				this.green(arg.g);
				this.blue(arg.b)
				
			} else {
				fk.warn("fk.Colour.set: Invalid argument ("+ arg +" <"+ typeof(arg) +">)");
			}
		} else if(arguments.length == 3) {			
			this.red( arguments[0] );
			this.green( arguments[1] );
			this.blue( arguments[2] );
		}
	},
	
	// sets/ gets the RED component
	red: function(r) {
		if(arguments.length == 1) this.r = this.parseComponent(arguments[0]);
		return this.r;
	},

	// sets/ gets the GREEN component	
	green: function(g) {
		if(arguments.length == 1) this.g = this.parseComponent(arguments[0]);
		return this.g;
	},
	
	// sets/ gets the BLUE component
	blue: function(b) {
		if(arguments.length == 1) this.b = this.parseComponent(arguments[0]);
		return this.b;
	},

	// sets/ gets the HSL HUE value
	hue: function() {
		var hsv = this.toHSV();
		if(arguments.length == 1) {
			hsv[0] = arguments[0];
			this.fromHSV(hsv[0],hsv[1],hsv[2]);
		}
		return hsv[0];
	},
	
	// sets/ gets the HSL SATURATION value
	saturation: function() {
		var hsv = this.toHSV()
		if(arguments.length == 1) {
			hsv[1] = arguments[0]
			this.fromHSV(hsv[0],hsv[1],hsv[2])
		}
		return hsv[1]
	},
	
	// sets/ gets the hsv VALUE/ BRIGHTNESS value
	value: function() {
		var hsv = this.toHSV()
		if(arguments.length == 1) {
			hsv[2] = arguments[0]
			this.fromHSV(hsv[0],hsv[1],hsv[2])
		}
		return hsv[2]
	},
	
	// -- Convert To ... Methods -------------------------------------------------
	// Converts this Colour to HSL
	toHSL: function() {
		var _r = this.r / 255
		var _g = this.g / 255
		var _b = this.b / 255
		
	    var max = Math.max(_r, _g, _b), min = Math.min(_r, _g, _b)
	    var h, s, l = (max + min) / 2
	
	    if(max == min){
	        h = s = 0 // ach_romatic
	    }else{
	        var d = max - min
	        s = l > 0.5 ? d / (2 - max - min) : d / (max + min)
	        switch(max){
	            case _r: h = (_g - _b) / d + (_g < _b ? 6 : 0); break
	            case _g: h = (_b - _r) / d + 2; break
	            case _b: h = (_r - _g) / d + 4; break
	        }
	        h /= 6;
	    }
	
	    return [h, s, l];
	},
	
	// Converts this Colour to HSV
	toHSV: function() {
		var _r = this.r / 255
		var _g = this.g / 255
		var _b = this.b / 255
		
		var max = Math.max(_r, _g, _b), min = Math.min(_r, _g, _b)
		var h, s, v = max

		var d = max - min
		s = max == 0 ? 0 : d / max

		if(max == min) {
			h = 0; // ach_romatic
		} else {
			switch(max){
				case _r: h = (_g - _b) / d + (_g < _b ? 6 : 0); break
				case _g: h = (_b - _r) / d + 2; break
				case _b: h = (_r - _g) / d + 4; break
			}
			h /= 6;
		}

		return [h, s, v]
	},
	
	// returns this colour as a hexadecimal string
	toHex: function() {
		return fk.dec2hex(this.toInt());
	},
	
	// returns this colour as a single integer
	toInt: function() {
		return (this.r << 16) + (this.g << 8) + this.b;
	},
	
	// -- Convert From ... Methods -----------------------------------------------
	// Sets this Colour to the colour defined with the given HSL values.
	fromHSL: function(h, s, l) {
		if(s == 0) {
	        r = g = b = l // achromatic
	    } else {
	        function hue2rgb(p, q, t){
	            if(t < 0) t += 1
	            if(t > 1) t -= 1
	            if(t < 1/6) return p + (q - p) * 6 * t
	            if(t < 1/2) return q
	            if(t < 2/3) return p + (q - p) * (2/3 - t) * 6
	            return p;
	        }
	
	        var q = l < 0.5 ? l * (1 + s) : l + s - l * s
	        var p = 2 * l - q
	        r = hue2rgb(p, q, h + 1/3)
	        g = hue2rgb(p, q, h)
	        b = hue2rgb(p, q, h - 1/3)
	    }
		return this
	},
	
	// Sets this Colour to the colour defined with the given HSV values.
	fromHSV: function(h,s,v) {
		
		var i = Math.floor(h * 6);
	    var f = h * 6 - i;
	    var p = v * (1 - s);
	    var q = v * (1 - f * s);
	    var t = v * (1 - (1 - f) * s);
	
	    switch(i % 6){
	        case 0: this.r = v, this.g = t, this.b = p; break
	        case 1: this.r = q, this.g = v, this.b = p; break
	        case 2: this.r = p, this.g = v, this.b = t; break
	        case 3: this.r = p, this.g = q, this.b = v; break
	        case 4: this.r = t, this.g = p, this.b = v; break
	        case 5: this.r = v, this.g = p, this.b = q; break
	    };

		this.r *= 255;
		this.g *= 255;
		this.b *= 255;
		
		return this;
	},
	
	// Sets this Colour to the colour defined with the given hex-string
	fromHex: function(hex) {
		return this.fromInt(fk.hex2dec(hex));
	},
	
	// Sets this Colour to the colour defined with the given integer
	fromInt: function(i) {
		this.r = (i >> 16) & 0xFF;
		this.g = (i >> 8) & 0xFF;
		this.b = i & 0xFF;
		return this;
	},
	
	// -- Misc Utilities -------------------------------------------------------
	randomise: function() {
		this.red(Math.random());
		this.green(Math.random());
		this.blue(Math.random());
		return this;
	},
	
	slerp: function(target, delta) {
		this.r = this.r * (1 - delta) + target.r * delta;
		this.g = this.g * (1 - delta) + target.g * delta;
		this.b = this.b * (1 - delta) + target.b * delta;
		return this;
	},
	
	interpolate: function(target, delta) {
		this.r += (target.r - this.r) * delta;
		this.g += (target.g - this.g) * delta;
		this.b += (target.b - this.b) * delta;
		return this;  
	},
	
	clone: function() {
		return new fk.Colour(this);
	},
	
	toString: function() {
		return 'Colour['+ this.r +', '+ this.g +', '+ this.b + ']';
	},
	
	
	// -- Private Utilities ------------------------------------------------------
	parseComponent: function(arg) {
		if(typeof(arg) == 'number') {
			var n = arg
			// make sure n is not negative
			if(n < 0) n *= -1
			// when n is smaller than 1 assume it's a float [0,1] and scale it up
			if(n < 1.0) n *= 255
			// make sure n doesnt exceed the rgb range [0,255]
			if(n > 255) n = 255
			return n
			
		} else if(typeof(arg) == 'string') {
			return this.parseComponent(parseFloat(arg))
			
		} else {
			fk.info("fk.Colour.parseComponent: unhandled type "+ typeof(arg) +" "+ arg)
			return arg	
		}
	},
});