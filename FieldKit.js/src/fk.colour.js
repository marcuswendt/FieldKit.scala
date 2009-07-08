/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: July 08, 2009
 */

// =============================================================================
// COLOUR CLASS
// =============================================================================
fk.Colour = function() {
	var r = g = b = 0

	// -- Getters & Setters ------------------------------------------------------
	// Sets the R,G,B values of this Colour object
	// Can take either another Colour object, an RGB(r,g,b) String or three seperate R,G,B values as arguments
	this.set = function() {
		if(arguments.length == 1) {
			var arg = arguments[0]
			if(typeof(arg) == 'string') {
				var parts = arg.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/)
				this.r( parts[1] )
				this.g( parts[2] )
				this.b( parts[3] )
			} else {			
				r = arg.r()
				g = arg.g()
				b = arg.b()
			}
		} else if(arguments.length == 3) {
			this.r( arguments[0] )
			this.g( arguments[1] )
			this.b( arguments[2] )
		}
	}
	
	// sets/ gets the RED component
	this.r = function() {
		if(arguments.length == 1) r = parseComponent(arguments[0])
		return r
	}

	// sets/ gets the GREEN component	
	this.g = function() {
		if(arguments.length == 1) g = parseComponent(arguments[0])
		return g
	}
	
	// sets/ gets the BLUE component
	this.b = function() {
		if(arguments.length == 1) b = parseComponent(arguments[0])
		return b
	}

	// sets/ gets the HSL HUE value
	this.h = function() {
		var hsv = this.toHSV()
		if(arguments.length == 1) {
			hsv[0] = arguments[0]
			this.fromHSV(hsv[0],hsv[1],hsv[2])
		}
		return hsv[0]
	}
	
	// sets/ gets the HSL SATURATION value
	this.s = function() {
		var hsv = this.toHSV()
		if(arguments.length == 1) {
			hsv[1] = arguments[0]
			this.fromHSV(hsv[0],hsv[1],hsv[2])
		}
		return hsv[1]
	}
	
	// sets/ gets the hsv VALUE/ BRIGHTNESS value
	this.v = function() {
		var hsv = this.toHSV()
		if(arguments.length == 1) {
			hsv[2] = arguments[0]
			this.fromHSV(hsv[0],hsv[1],hsv[2])
		}
		return hsv[2]
	}
	
	// -- Convert To ... Methods -------------------------------------------------
	// Converts this Colour to HSL
	// http://en.wikipedia.org/wiki/HSL_and_HSV
	//
	// Code from the web developer Michael Jackson
	// http://www.mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
	this.toHSL = function() {
		var _r = r / 255
		var _g = g / 255
		var _b = b / 255
		
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
	}
	
	// Converts this Colour to HSV
	// http://en.wikipedia.org/wiki/HSL_and_HSV
	this.toHSV = function() {
		var _r = r / 255
		var _g = g / 255
		var _b = b / 255
		
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
	}
	
	this.toHEX = function() {
		
	}
	
	// -- Convert From ... Methods -----------------------------------------------
	// Sets this Colour to the colour defined with the given HSL values.
	this.fromHSL = function(h, s, l) {
		
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
	}
	
	// Sets this Colour to the colour defined with the given HSV values.
	this.fromHSV = function(h,s,v) {
		
		var i = Math.floor(h * 6)
    var f = h * 6 - i
    var p = v * (1 - s)
    var q = v * (1 - f * s)
    var t = v * (1 - (1 - f) * s)

    switch(i % 6){
        case 0: r = v, g = t, b = p; break
        case 1: r = q, g = v, b = p; break
        case 2: r = p, g = v, b = t; break
        case 3: r = p, g = q, b = v; break
        case 4: r = t, g = p, b = v; break
        case 5: r = v, g = p, b = q; break
    }

		r *= 255
		g *= 255
		b *= 255
		
		return this
	}
	
	
	
	// -- Misc Utilities ---------------------------------------------------------
	this.randomize = function() {
		this.r(Math.random())
		this.g(Math.random())
		this.b(Math.random())
		return this
	}
	
	this.clone = function() {
		var c = new Colour()
		c.set(this)
		return c
	}
	
	this.toString = function() {
		return 'Colour ['+ r +', '+ g +', '+ b + ']'
	}
	// -- Private Utilities ------------------------------------------------------
	var parseComponent = function(arg) {
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
			return parseComponent(parseFloat(arg))
			
		} else {
			fk.info("fk.Colour.parseComponent: unhandled type "+ typeof(arg) +" "+ arg)
			return arg	
		}
	}

	// -- Init -------------------------------------------------------------------
	// parse arguments
	if(arguments.length == 3) {
		this.set(arguments[0], arguments[1], arguments[2])
		
	} else if(arguments.length == 1) {
		this.set(arguments[0])
	}
}