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
import field.kit.colour.Colour

/** unit tests for the <code>Colour</code> class */
class ColourTest extends TestCase {
 
 def testStringConstructor() = {
   var c:Colour = null
   c = Colour("25")
   val n = 25/ 255f
   assertEquals(n, c.r)
   assertEquals(n, c.g)
   assertEquals(n, c.b)
   assertEquals(n, c.a)
    
   c = Colour("0.9 0.1")    
   assertEquals(c.r, 0.9f)
   assertEquals(c.g, 0.9f)
   assertEquals(c.b, 0.9f)
   assertEquals(c.a, 0.1f)
   
   c = Colour("0.1 0.2 0.3")    
   assertEquals(c.r, 0.1f)
   assertEquals(c.g, 0.2f)
   assertEquals(c.b, 0.3f)
   assertEquals(c.a, 1.0f)
   
   c = Colour("0.1 0.2 0.3 0.4")    
   assertEquals(c.r, 0.1f)
   assertEquals(c.g, 0.2f)
   assertEquals(c.b, 0.3f)
   assertEquals(c.a, 0.4f)
   
   c = Colour("R1.0 G0.1254902 B0.96862745 A1.0")
   assertEquals(1.0f, c.r)
   assertEquals(0.1254902f, c.g)
   assertEquals(0.96862745f, c.b)
   assertEquals(1.0f, c.a)
 }
}
