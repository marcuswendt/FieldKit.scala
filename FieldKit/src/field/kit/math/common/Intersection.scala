/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 31, 2009 */
package field.kit.math

/**
 * Methods for calculating intersections for math.Common
 */
trait Intersection {
  import Common._
  
  /**
   * Finds the intersection point between two lines
   * (Original code by Guillaume Stagnaro)
   * 
   * @see <a href="http://www.ecole-art-aix.fr/IMG/pde/Intersect.pde">original Javacode</a>
   * 
   * @result a Vector that stores the intersection point
   * @return returns true when the two lines intersect, otherwise false
   */
  final def intersects(x1:Float, y1:Float, x2:Float, y2:Float, x3:Float, y3:Float, x4:Float, y4:Float, result:Vec2) = {
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
		    result.x = (num - offset) / denom
		  } else {
		    result.x = (num + offset) / denom
		  }
	   
		  num = (a2 * c1) - (a1 * c2)
		  if (num < 0) {
		    result.y = (num - offset) / denom
		  } else {
		    result.y = (num + offset) / denom
		  }
		  true
  	  	}
  	  }
  	}
  }
}
