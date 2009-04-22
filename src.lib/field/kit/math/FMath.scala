/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.math

/**
 * same as <code>trait FMath</code> but singletonized
 */
object FMath extends FMath {}

/**
 * interpolation, trigonometry and other helper methods for mathematical problems
 */
trait FMath {
  // --------------------------------------------------------------------------
  // implements various interpolation and easing functions
  // @see http://en.wikipedia.org/wiki/Interpolation
  // --------------------------------------------------------------------------
  /** 
   * spherical linear interpolation
   * @see http://en.wikipedia.org/wiki/Slerp
   */
  def slerp(current:Float, target:Float, delta:Float):Float = 
    current * (1 - delta) + target * delta
  
  def slerp(current:Double, target:Double, delta:Double):Double = 
    current * (1.0 - delta) + target * delta
  
  def linear(current:Float, target:Float, delta:Float):Float =
    (target - current) * delta + current  
  
  def linear(current:Double, target:Double, delta:Double):Double =
    (target - current) * delta + current  

  def slerpAngle(cur:Float, to:Float, delta:Float) = {
    var result = cur
    if(cur < 0 && to > 0) {
      if(abs(cur) > HALF_PI && abs(to) > HALF_PI) result += TWO_PI
    } else if (cur > 0 && to < 0) {
      if (abs(cur) > HALF_PI && abs(to) > HALF_PI) result -= TWO_PI
    }
    result * (1 - delta) + to * delta
  }
  
  // --------------------------------------------------------------------------
  // helpers from PApplet
  // --------------------------------------------------------------------------
  def abs(n:Int) = if(n < 0) -n else n
  def abs(n:Float) = if(n < 0) -n else n
 
  def sq(n:Float) = n*n
  def sqrt(a:Float) = Math.sqrt(a).asInstanceOf[Float]
  def log(a:Float) = Math.log(a).asInstanceOf[Float]
  def exp(a:Float) = Math.exp(a).asInstanceOf[Float]
  def pow(a:Float, b:Float) = Math.pow(a,b).asInstanceOf[Float]
  
  def max(a:Int, b:Int) = if(a > b) a else b
  def max(a:Float, b:Float) = if(a > b) a else b
  def min(a:Int, b:Int) = if(a > b) b else a
  def min(a:Float, b:Float) = if(a > b) b else a

  def floor(n:Float) = Math.floor(n).asInstanceOf[Float]
  def ceil(n:Float) = Math.ceil(n).asInstanceOf[Float]
  
  // --------------------------------------------------------------------------
  // trigonomentry
  // --------------------------------------------------------------------------
  val PI = (java.lang.Math.PI).asInstanceOf[Float]
  val HALF_PI = PI / 2f
  val THIRD_PI = PI / 3f
  val QUARTER_PI = PI / 4f
  val TWO_PI = PI * 2f
  
  val DEG_TO_RAD = PI / 180f
  val RAD_TO_DEG = 180f / PI
  
  def sin(f:Float) = Math.sin(f).asInstanceOf[Float]
  def cos(f:Float) = Math.cos(f).asInstanceOf[Float]
  def acos(f:Float) = Math.acos(f).asInstanceOf[Float]
  def tan(f:Float) = Math.tan(f).asInstanceOf[Float]
  def atan(f:Float) = Math.atan(f).asInstanceOf[Float]
  def atan2(x:Float, y:Float) = Math.atan2(x,y).asInstanceOf[Float]
  
  def sin(f:Double) = Math.sin(f)
  def cos(f:Double) = Math.cos(f)
  def acos(f:Double) = Math.acos(f)
  def tan(f:Double) = Math.tan(f)
  def atan(f:Double) = Math.atan(f)
  def atan2(x:Double, y:Double) = Math.atan2(x,y)
  
  // --------------------------------------------------------------------------
  // misc
  // --------------------------------------------------------------------------
  /** @return Returns the signum function of the argument; zero if the argument is zero, 1.0f if the argument is greater than zero, -1.0f if the argument is less than zero.*/
  def sign(value:Float) = if(value > 1f) 1f else -1f
  
  /** @return returns true when a and b are both positive or negative number */
  def same(a:Float, b:Float) = (a * b) >= 0
  
  def round(value:Float, precision:Int):Float = {
    val exp = Math.pow(10, precision).asInstanceOf[Float]
    Math.round(value * exp) / exp
  }
  
  def round(value:Double, precision:Int):Double = {
    val exp = Math.pow(10, precision)
    Math.round(value * exp) / exp
  }
  
  def clamp(value:Float):Float = clamp(value, 0f, 1f)
  
  def clamp(value:Float, min:Float, max:Float) = {
    var result = value
    if(result > max) result = max
    if(result < min) result = min
    result
  }
  
  /**
   * Finds the intersection point between two lines
   * (Original code by Guillaume Stagnaro)
   * 
   * @see <a href="http://www.ecole-art-aix.fr/IMG/pde/Intersect.pde">original Javacode</a>
   * 
   * @result a Vector that stores the intersection point
   * @return returns true when the two lines intersect, otherwise false
   */
  def intersects(x1:Float, y1:Float, x2:Float, y2:Float, x3:Float, y3:Float, x4:Float, y4:Float, result:VecF) = {
	// Compute a1, b1, c1, where line joining points 1 and 2
	// is "a1 x + b1 y + c1 = 0".
	val a1 = y2 - y1
	val b1 = x1 - x2
	val c1 = (x2 * y1) - (x1 * y2)

	// Compute r3 and r4.
	val r3 = ((a1 * x3) + (b1 * y3) + c1)
	val r4 = ((a1 * x4) + (b1 * y4) + c1)

	// Check signs of r3 and r4. If both point 3 and point 4 lie on
	// same side of line 1, the line segments do not intersect.
	if ((r3 != 0) && (r4 != 0) && same(r3, r4)) {
	  false 
	} else {
	  // Compute a2, b2, c2
	  val a2 = y4 - y3
	  val b2 = x3 - x4
	  val c2 = (x4 * y3) - (x3 * y4)

	  // Compute r1 and r2
	  val r1 = (a2 * x1) + (b2 * y1) + c2
	  val r2 = (a2 * x2) + (b2 * y2) + c2
   
	  // Check signs of r1 and r2. If both point 1 and point 2 lie
	  // on same side of second line segment, the line segments do
      // not intersect.
      if((r1 != 0) && (r2 != 0) && (same(r1, r2))) {
        false
      } else {
        // Line segments intersect: compute intersection point.
  	  	val denom = (a1 * b2) - (a2 * b1)
     
  	  	if (denom == 0) {
  	  	  // lines are collinear
  	  	  false
  	  	} else {
  	  	  var offset = denom / 2
  	  	  if(denom < 0) offset = -offset
          
  	  	  // The denom/2 is to get rounding instead of truncating. It
		  // is added or subtracted to the numerator, depending upon the
		  // sign of the numerator.
		  var num = (b1 * c2) - (b2 * c1)
		  if (num < 0) {
		    result(0) = (num - offset) / denom
		  } else {
		    result(0) = (num + offset) / denom
		  }
	   
		  num = (a2 * c1) - (a1 * c2)
		  if (num < 0) {
		    result(1) = (num - offset) / denom
		  } else {
		    result(1) = (num + offset) / denom
		  }
		  true
  	  	}
  	  }
  	}
  }
}
