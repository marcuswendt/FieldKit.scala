/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.colour

import field.kit._

/**
* Companion object to class <code>Colour</code>
* 
* Colour conversion code ported from toxiclibs 
* <a href="http://code.google.com/p/toxiclibs/source/browse/trunk/toxiclibs/src.color/toxi/color/TColor.java">TColour</a> class
* 
* @see http://code.google.com/p/toxiclibs
*/
object Colour {
	val BLACK = Colour(0f)
	val WHITE = Colour(1f)

	val RED = Colour(1f, 0f, 0f)
	val GREEN = Colour(0f, 1f, 0f)
	val BLUE = Colour(0f, 0f, 1f)

	val YELLOW = Colour(1f, 1f, 0f)
	val CYAN = Colour(0f, 1f, 1f)
	val MAGENTA = Colour(1f, 0f, 1f)

	val INV60DEGREES = 60.0f / 360f

	def apply() = new Colour(1f,1f,1f,1f)
	def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
	def apply(grey:Float) = new Colour(grey,grey,grey,1f)
	def apply(grey:Float, alpha:Float) = new Colour(grey,grey,grey,alpha)

	def apply(r:Int,g:Int,b:Int) = new Colour(r/255f,g/255f,b/255f,1f)
	def apply(r:Int,g:Int,b:Int,a:Int) = new Colour(r/255f,g/255f,b/255f,a/255f)

	def apply(i:Int) = { val c = new Colour(); c := i; c }
	def apply(s:String) = { val c = new Colour(); c := s; c }
	def apply(c:Colour) = { val cNew = new Colour(); cNew := c; cNew }

	// -- Conversion -------------------------------------------------------------
	/** Converts a RGB colour to a HSV triplet */
	final def rgbToHSV(r:Float, g:Float, b:Float) = {
		var h=0f
		var s=0f
		var v = max(r, g, b)
		var d = v - min(r, g, b)

		if(v != 0.0) {
			s = d / v
		}

		if(s != 0.0) {
			if(java.lang.Float.compare(r, v) == 0) {
				h = (g - b) / d
			} else if(java.lang.Float.compare(g, v) == 0) {
				h = 2 + (b - r) / d
			} else {
				h = 4 + (r - g) / d
			}
		}

		h *= INV60DEGREES

		if (h < 0)
		h += 1.0f

		(h,s,v)
	}

		/** Converts a HSV colour to a RGB triplet */
	final def hsvToRGB(_h:Float, s:Float, v:Float) = {
		if(java.lang.Float.compare(s, 0.0f) == 0) {
			(v,v,v)

		} else {
			val h = _h / INV60DEGREES
			val i = h.toInt
			val f = h - i
			val p = v * (1 - s)
			val q = v * (1 - s * f)
			val t = v * (1 - s * (1 - f))

		i match {
			case 0 => (v, t, p)
			case 1 => (q, v, p)
			case 2 => (p, v, t)
			case 3 => (t, p, v)
			case _ => (v, p, q)
			}
		}
	}
}


/**
* A versatile RGBA Colour utility, used as datatype and for conversion 
* @author Marcus Wendt
*/
class Colour(var r:Float, var g:Float, var b:Float, var a:Float) extends Logger {  
	protected var h = 0f
	protected var s = 0f
	protected var v = 0f

	// make sure HSV values are up to date
	setRGB(r,g,b)

	final def red = r
	final def red_=(s:Float) = setRGB(s,g,b) 

	final def green = g
	final def green_=(s:Float) = setRGB(r,s,b)

	final def blue = b
	final def blue_=(s:Float) = setRGB(r,g,s)

	final def alpha = a
	final def alpha_=(s:Float) = this.a = clamp(s, 0, 1)

	final def hue = h
	final def hue_=(theta:Float) = setHSV(theta,s,v)

	final def saturation = s
	final def saturation_=(theta:Float) = setHSV(h,theta,v)

	final def value = v
	final def value_=(theta:Float) = setHSV(h,s,theta)

	final def luminance = r * 0.299f + g * 0.587f + b * 0.114f

	// -- Auxilliary Constructors ------------------------------------------------
	def this() = this(0f,0f,0f,1f)

	// -- Setters ----------------------------------------------------------------
	final def setHSV(h:Float, s:Float, v:Float) = {
		this.h = h
		this.s = s
		this.v = v

		if(this.h < 0f) this.h *= -1f
		if(this.h > 1f) this.h %= 1f

		if(this.s < 0f) this.s *= -1f
		if(this.s > 1f) this.s %= 1f

		if(this.v < 0f) this.v *= -1
		if(this.v > 1f) this.v %= 1f


		val rgb = Colour.hsvToRGB(h,s,v)
		this.r = rgb._1
		this.g = rgb._2
		this.b = rgb._3

		this
	}

	/** Sets the rgb values and internally updates the hsv as well */
	final def setRGB(r:Float, g:Float, b:Float) = {
		this.r = r
		this.g = g
		this.b = b

		val hsv = Colour.rgbToHSV(r,g,b)
		this.h = hsv._1
		this.s = hsv._2
		this.v = hsv._3

		this
	}

	/**
	* Sets this Colour components to the given Colour
	* @return itself
*/
	final def :=(c:Colour):Colour = {
		if(c == null) return c

		this.r = c.r
		this.g = c.g
		this.b = c.b
		this.a = c.a

		this.h = c.h
		this.s = c.s
		this.v = c.v

		this
	}

	/**
	* Sets the r,g,b components of this Colour to the given Float and the alpha seperately
	* @return itself
*/
	final def :=(grey:Float) = {
		setRGB(grey, grey, grey)
		this.a = 1f
		this
	}

	/**
	* Sets the r,g,b components of this Colour to the given Float and the alpha seperately
	* @return itself
*/
	final def :=(grey:Float,a:Float) = {
		setRGB(grey, grey, grey) 
		this.a=a
		this
	}

	/**
	* Sets only the rgb components of this Colour
	* @return itself
*/
	final def :=(r:Float,g:Float,b:Float) = setRGB(r,g,b)

	/**
	* Sets all components of this Colour individually
	* @return itself
*/
	final def :=(r:Float,g:Float,b:Float,a:Float) = { 
		setRGB(r,g,b)
		this.a=a
		this
	}

	/**
	* Sets this Colour using an Integer which is interpreted as:
	* a) as single [0,256] grey value
	* b) as packed integer given as hex number
	* @return itself
*/
	final def :=(i:Int):Colour = {
		// i is within [0, 256] range -> i is a greyscale value
		if(i > 0 && i < 256) { 
			:=(i/ 256f)
		// i is a hex-value
		} else {
			fromRGB(i)
		}
		this
	}

	/**
	* Attempts to interpret this String to set this Colours components
	* @return itself
*/
	final def :=(s:String) = {
		if(s != null) {
		val iter = DECIMAL findAllIn s
		val list = iter.toList

		var index = 0
		def next = { 
		var f = abs(list(index).toFloat)
		index += 1
		// normalize values above 1.0
		if(f > 1.0f) f /= 255f
		f
		}

		list.size match {
		// set all 
		case 1 =>
		val f = next
		this.r = f
		this.g = f
		this.b = f
		this.a = f
		// set rgb and alpha
		case 2 =>
		val f = next
		this.r = f
		this.g = f
		this.b = f
		this.a = next
		// set the rgb values
		case 3 =>
		this.r = next 
		this.g = next
		this.b = next
		// set rgba independently
		case 4 =>
		this.r = next 
		this.g = next
		this.b = next
		this.a = next
		case _ => throw new Exception("Couldnt parse String '"+ s +"' (matches:"+ list.size +")")
		}

		setRGB(r,g,b)
		}
		this
	}

	// -- to and from packed integer conversion ----------------------------------
	/** @return this colour as argb packed integer */
	final def toARGB = {
		(((a * 255).asInstanceOf[Int] & 0xFF) << 24) | 
		(((r * 255).asInstanceOf[Int] & 0xFF) << 16) | 
		(((g * 255).asInstanceOf[Int] & 0xFF) << 8) | 
		(((b * 255).asInstanceOf[Int] & 0xFF))
	}

	/** @return this colour as rgba packed integer */
	final def toRGBA = {
		(((r * 255).asInstanceOf[Int] & 0xFF) << 24) | 
		(((g * 255).asInstanceOf[Int] & 0xFF) << 16) | 
		(((b * 255).asInstanceOf[Int] & 0xFF) << 8) | 
		(((a * 255).asInstanceOf[Int] & 0xFF))
	}

	/** @return this colour as rgb packed integer */ 
	final def toRGB = {
		(((r * 255).asInstanceOf[Int] & 0xFF) << 16) | 
		(((g * 255).asInstanceOf[Int] & 0xFF) << 8) | 
		(((b * 255).asInstanceOf[Int] & 0xFF))
	}

	final def toHSVA = new HSVA(h,s,v,a)

	final def fromRGB(i:Int) = {
		r = (i >> 16 & 0xFF) / 255f
		g = (i >> 8 & 0xFF) / 255f
		b = (i & 0xFF) / 255f
		setRGB(r,g,b)
	}

	final def fromRGBA(i:Int) = {
		r = (i >> 24 & 0xFF) / 255f
		g = (i >> 16 & 0xFF) / 255f
		b = (i >> 8 & 0xFF) / 255f
		a = (i & 0xFF) / 255f
		setRGB(r,g,b)
	}

	final def fromARGB(i:Int) = {
		a = (i >> 24 & 0xFF) / 255f
		r = (i >> 16 & 0xFF) / 255f
		g = (i >> 8 & 0xFF) / 255f
		b = (i & 0xFF) / 255f
		setRGB(r,g,b)
	}

	// -- Colour Operations ------------------------------------------------------  
	final def randomise = {
		setRGB(random, random, random)
		a = random
		this
	}

	final def invert = setRGB(1 - r, 1 - g, 1 - b)

	final def interpolate(target:Colour, delta:Float) = {
		val d = delta
	    val i = 1f - d
	    this.r = this.r * d + target.r * i
	    this.g = this.g * d + target.g * i
	    this.b = this.b * d + target.b * i
		this
	}
	
	// -- Arithmetic Operations --------------------------------------------------
	def +=(r:Float, g:Float, b:Float, a:Float) = {
		setRGB(this.r + r, this.g + g, this.b + b)
		this.a += a
		this
	}

	def -=(r:Float, g:Float, b:Float, a:Float) = {
		setRGB(this.r - r, this.g - g, this.b - b)
		this.a -= a
		this
	}

	def *=(r:Float, g:Float, b:Float, a:Float) = {
		setRGB(this.r * r, this.g * g, this.b * b)
		this.a *= a
		this
	}

	def /=(r:Float, g:Float, b:Float, a:Float) = {
		setRGB(this.r / r, this.g / g, this.b / b)
		this.a /= a
		this
	}

	// -- HSV Operations ---------------------------------------------------------
	final def darken(amount:Float) = setHSV(h, s, v - clamp(amount, 0, 1))

	final def brighten(amount:Float) = setHSV(h, s, v + clamp(amount, 0, 1))

	final def desaturate(amount:Float) = setHSV(h, clamp(s - amount, 0, 1), v)

	final def saturate(amount:Float) = setHSV(h, clamp(s + amount, 0, 1), v)

	final def shiftHue(amount:Float) = setHSV(h + amount, s, v)

	final def shiftSaturation(amount:Float) = setHSV(h, s + amount, v)

	final def shiftValue(amount:Float) = setHSV(h, s, v + amount)


	// -- Helpers ----------------------------------------------------------------
	final def toInt = toARGB

	override def toString = "Colour("+ toLabel +")"

	def toLabel = "R"+ r +" G"+ g +" B"+ b +" A"+ a
}