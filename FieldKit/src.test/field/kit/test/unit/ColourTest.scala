/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 06, 2009 */
package field.kit.test.unit

import junit.framework._
import org.junit.Assert._

/** unit tests for the <code>Colour</code> class */
class ColourTest extends TestCase {
  override def setUp() = {}

 override def tearDown() = {}
 
 def testStringConstructor() = {
   var c:Colour = null
   c = new Colour("25")
   val n = 25/ 255f
   assertEquals(n, c.r)
   assertEquals(n, c.g)
   assertEquals(n, c.b)
   assertEquals(n, c.a)
    
   c = new Colour("0.9 0.1")    
   assertEquals(c.r, 0.9f)
   assertEquals(c.g, 0.9f)
   assertEquals(c.b, 0.9f)
   assertEquals(c.a, 0.1f)
   
   c = new Colour("0.1 0.2 0.3")    
   assertEquals(c.r, 0.1f)
   assertEquals(c.g, 0.2f)
   assertEquals(c.b, 0.3f)
   assertEquals(c.a, 1.0f)
   
   c = new Colour("0.1 0.2 0.3 0.4")    
   assertEquals(c.r, 0.1f)
   assertEquals(c.g, 0.2f)
   assertEquals(c.b, 0.3f)
   assertEquals(c.a, 0.4f)
 }
}
