/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 04, 2009 */
package field.kit.test.unit

import junit.framework._
import org.junit.Assert._

/** unit tests for the <code>SphereTest</code> class */
class SphereTest extends TestCase {
  import kit.math.Vec3
  import kit.math.geometry.Sphere
  
  def testContainsOrigin {
    var s = new Sphere(100f)
    val p1 = new Vec3(125f, 65f, 0f)    
    assertEquals( s contains p1, false)
    
    val p2 = new Vec3(25f, 15f, 0f)
    assertEquals( s contains p2, true)
    
    val p3 = new Vec3(-85f, 55f, 0f)
    assertEquals( s contains p3, true)
  }
}

object SphereTest {
  def main(args:Array[String]) = org.junit.runner.JUnitCore.main( classOf[SphereTest].getName );
}