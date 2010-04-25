/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.math

/**
 * Contains all math Package methods; deprecated since moving to 2.8 in favour of the
 * new Package object. Instead of import field.kit.Math.Common._ use import field.kit._
 * @deprecated
 */
object Common extends Package

/**
 * Extensive Maths package for 2D/3D graphics and simulations.
 * 
 * Provides trigonometry, interpolation and other helper methods.
 * 
 * @author Marcus Wendt
 */
trait Package 
extends Trigonometry
with Interpolation
with Intersection 
with Randomness {
	import java.lang.Math
	
	final val EPSILON = 1e-6f
	
	// Implicit conversions
	implicit def doubleTofloat(d: Double) = d.toFloat

	implicit def tuple2fToVec2(xy: (Float, Float)) = new Vec2(xy._1, xy._2)

	implicit def tuple3fToVec3(xyz: (Float, Float, Float)) = new Vec3(xyz._1, xyz._2, xyz._3)

	/** regular expression to detect a number within a string with optional minus and fractional part */
	final val DECIMAL = """(-)?(\d+)(\.\d*)?""".r

	// -- Utilities --------------------------------------------------------------
	final def abs(n:Int) = if(n < 0) -n else n
	final def abs(n:Float) = if(n < 0) -n else n

	final def sq(n:Float) = n*n
	final def sqrt(a:Float) = Math.sqrt(a).toFloat
	final def log(a:Float) = Math.log(a).toFloat
	final def exp(a:Float) = Math.exp(a).toFloat
	final def pow(a:Float, b:Float) = Math.pow(a,b).toFloat

	final def max(a:Int, b:Int) = if(a > b) a else b
	final def max(a:Float, b:Float) = if(a > b) a else b
	final def max(a:Float, b:Float, c:Float) = if(a > b) if(a > c) a else c else if(b > c) b else c 

	final def min(a:Int, b:Int) = if(a > b) b else a
	final def min(a:Float, b:Float) = if(a > b) b else a
	final def min(a:Float, b:Float, c:Float) = if(a < b) if(a < c) a else c else if(b < c) b else c

	final def floor(n:Float) = Math.floor(n).toFloat
	final def ceil(n:Float) = Math.ceil(n).toFloat

	/** @return Returns the signum function of the argument; zero if the argument is zero, 1.0f if the argument is greater than zero, -1.0f if the argument is less than zero.*/
	final def signum(value:Float) = if(value > 1f) 1f else -1f

	/** @return returns true when a and b are both positive or negative number */
	final def same(a:Float, b:Float) = (a * b) >= 0

	final def round(value:Float, precision:Int):Float = {
		val _exp = Math.pow(10, precision).toFloat
		Math.round(value * _exp) / _exp
	}

	final def clamp(value:Float):Float = clamp(value, 0f, 1f)

	final def clamp(value:Float, min:Float, max:Float) = {
		var result = value
		if(result > max) result = max
		if(result < min) result = min
		if(result > max) result = max
		result
	}
}
