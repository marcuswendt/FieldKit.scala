/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created February 11, 2009 */
package field.kit.test.unit

import junit.framework._
import org.junit.Assert._

import field.kit.math.Vec3
import field.kit.math.geometry.Rect

/** unit test for the <code>Rect</code> class */
class RectTest extends TestCase {
	
	def testIntersection { 
		val r1 = new Rect(0, 0, 200, 100)
		val r2 = new Rect(-100, -50, 200, 100)
		assertEquals(r1.intersects(r2), true)
	}
	
	def testIntersectionSameHeight {
		val r1 = new Rect(0, 0, 200, 100)
		val r2 = new Rect(100, 0, 200, 100)
		assertEquals(r1.intersects(r2), true)
	}
	
	def testIntersectionTouching {
		val r1 = new Rect(0, 0, 100, 100)
		val r2 = new Rect(100, 0, 100, 100)
		assertEquals(r1.intersects(r2), false)
	}
	
	def testIntersectionContainment { 
		val r1 = new Rect(-100, -100, 200, 200)
		val r2 = new Rect(-50, -50, 100, 100)
		assertEquals(r1.intersects(r2), true)
		assertEquals(r2.intersects(r1), true)
	}
	
	// -- Containment ----------------------------------------------------------
	
	def testContainment { 
		val r1 = new Rect(-100, -100, 200, 200)
		val r2 = new Rect(-50, -50, 100, 100)
		assertEquals(r1.contains(r2), true)
		assertEquals(r2.contains(r1), false)
	}
}
