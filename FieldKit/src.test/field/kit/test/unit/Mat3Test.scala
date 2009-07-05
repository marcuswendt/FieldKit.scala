/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.test.unit

import junit.framework._
import org.junit.Assert._

/** unit tests for the <code>Mat3</code> class */
class Mat3Test extends TestCase {
  import field.kit.math.Mat3
  
   def testGetXY() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
   
    assertEquals(m1.get(0,0), 0f)
    assertEquals(m1.get(0,1), 1f)
    assertEquals(m1.get(0,2), 2f)
   
    assertEquals(m1.get(1,0), 3f)
    assertEquals(m1.get(1,1), 4f)
    assertEquals(m1.get(1,2), 5f)
   
    assertEquals(m1.get(2,0), 6f)
    assertEquals(m1.get(2,1), 7f)
    assertEquals(m1.get(2,2), 8f)
  }
   
  def testSetFloats() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    assertEquals(m1.m00, 0f)
    assertEquals(m1.m01, 1f)
    assertEquals(m1.m02, 2f)
   
    assertEquals(m1.m10, 3f)
    assertEquals(m1.m11, 4f)
    assertEquals(m1.m12, 5f)
   
    assertEquals(m1.m20, 6f)
    assertEquals(m1.m21, 7f)
    assertEquals(m1.m22, 8f)
  }
  
  def testSetXY() = {
    var m1 = new Mat3
    m1(0,0) = 0 
    m1(0,1) = 1 
    m1(0,2) = 2 
    
    m1(1,0) = 4 
    m1(1,1) = 5 
    m1(1,2) = 6 
    
    m1(2,0) = 8 
    m1(2,1) = 9 
    m1(2,2) = 10 
   
    assertEquals(m1.get(0,0), 0f)
    assertEquals(m1.get(0,1), 1f)
    assertEquals(m1.get(0,2), 2f)
   
    assertEquals(m1.get(1,0), 4f)
    assertEquals(m1.get(1,1), 5f)
    assertEquals(m1.get(1,2), 6f)
   
    assertEquals(m1.get(2,0), 8f)
    assertEquals(m1.get(2,1), 9f)
    assertEquals(m1.get(2,2), 10f)
 }
  
 def testAddLocalMatrix() = {
   var m1 = new Mat3
   m1.set(0,1,2,
           3,4,5,
           6,7,8)
   
   var m2 = new Mat3
   m2.set(0,1,2,
           3,4,5,
           6,7,8)
   
   m1 += m2
   
    assertEquals(m1.m00, 0f)
    assertEquals(m1.m01, 2f)
    assertEquals(m1.m02, 4f)
   
    assertEquals(m1.m10, 6f)
    assertEquals(m1.m11, 8f)
    assertEquals(m1.m12, 10f)
   
    assertEquals(m1.m20, 12f)
    assertEquals(m1.m21, 14f)
    assertEquals(m1.m22, 16f)
 }
 
 def testAddMatrix() = {
   var m1 = new Mat3
   m1.set(0,1,2,
          3,4,5,
          6,7,8)
   
   var m2 = new Mat3
   m2.set(0,1,2,
          3,4,5,
          6,7,8)
   
   val m3 = m1 + m2
   
    assertEquals(m3.m00, 0f)
    assertEquals(m3.m01, 2f)
    assertEquals(m3.m02, 4f)
   
    assertEquals(m3.m10, 6f)
    assertEquals(m3.m11, 8f)
    assertEquals(m3.m12, 10f)
   
    assertEquals(m3.m20, 12f)
    assertEquals(m3.m21, 14f)
    assertEquals(m3.m22, 16f)
 }
 
  def testSubMatrix() = {
   var m1 = new Mat3
   m1.set(0,1,2,
          3,4,5,
          6,7,8)
   
   var m2 = new Mat3
   m2.set(0,1,2,
           3,4,5,
           6,7,8)
   
   val m3 = m1 - m2
   
    assertEquals(m3.m00, 0f)
    assertEquals(m3.m01, 0f)
    assertEquals(m3.m02, 0f)
   
    assertEquals(m3.m10, 0f)
    assertEquals(m3.m11, 0f)
    assertEquals(m3.m12, 0f)
   
    assertEquals(m3.m20, 0f)
    assertEquals(m3.m21, 0f)
    assertEquals(m3.m22, 0f)
 }
  
  def testMulMatrix() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    var m2 = new Mat3
    m2.set(m1)
    
    val m3 = m1 * m2
   
    assertEquals(m3.m00, 15f)
    assertEquals(m3.m01, 18f)
    assertEquals(m3.m02, 21f)
   
    assertEquals(m3.m10, 42f)
    assertEquals(m3.m11, 54f)
    assertEquals(m3.m12, 66f)
   
    assertEquals(m3.m20, 69f)
    assertEquals(m3.m21, 90f)
    assertEquals(m3.m22, 111f)
  }
  
  def testMulLocal() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    var m2 = new Mat3
    m2.set(m1)
    
    m1 *= m2 
   
    assertEquals(m1.m00, 15f)
    assertEquals(m1.m01, 18f)
    assertEquals(m1.m02, 21f)
   
    assertEquals(m1.m10, 42f)
    assertEquals(m1.m11, 54f)
    assertEquals(m1.m12, 66f)
   
    assertEquals(m1.m20, 69f)
    assertEquals(m1.m21, 90f)
    assertEquals(m1.m22, 111f)
 }
}
