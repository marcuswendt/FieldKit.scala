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

/** unit tests  for the <code>Mat3</code> class  */
class Mat3Test extends TestCase {
  import field.kit.math.Mat3
  
   def testGetXY() = { 
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
   
    assertEquals(m1(0,0), 0f, 0)
    assertEquals(m1(0,1), 1f, 0)
    assertEquals(m1(0,2), 2f, 0)
   
    assertEquals(m1(1,0), 3f, 0)
    assertEquals(m1(1,1), 4f, 0)
    assertEquals(m1(1,2), 5f, 0)
   
    assertEquals(m1(2,0), 6f, 0)
    assertEquals(m1(2,1), 7f, 0)
    assertEquals(m1(2,2), 8f, 0)
  }
   
  def testSetFloats() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    assertEquals(m1.m00, 0f, 0)
    assertEquals(m1.m01, 1f, 0)
    assertEquals(m1.m02, 2f, 0)
   
    assertEquals(m1.m10, 3f, 0)
    assertEquals(m1.m11, 4f, 0)
    assertEquals(m1.m12, 5f, 0)
   
    assertEquals(m1.m20, 6f, 0)
    assertEquals(m1.m21, 7f, 0)
    assertEquals(m1.m22, 8f, 0)
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
   
    assertEquals(m1(0,0), 0f, 0)
    assertEquals(m1(0,1), 1f, 0)
    assertEquals(m1(0,2), 2f, 0)
   
    assertEquals(m1(1,0), 4f, 0)
    assertEquals(m1(1,1), 5f, 0)
    assertEquals(m1(1,2), 6f, 0)
   
    assertEquals(m1(2,0), 8f, 0)
    assertEquals(m1(2,1), 9f, 0)
    assertEquals(m1(2,2), 10f, 0)
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
   
    assertEquals(m1.m00, 0f, 0)
    assertEquals(m1.m01, 2f, 0)
    assertEquals(m1.m02, 4f, 0)
   
    assertEquals(m1.m10, 6f, 0)
    assertEquals(m1.m11, 8f, 0)
    assertEquals(m1.m12, 10f, 0)
   
    assertEquals(m1.m20, 12f, 0)
    assertEquals(m1.m21, 14f, 0)
    assertEquals(m1.m22, 16f, 0)
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
   
    assertEquals(m3.m00, 0f, 0)
    assertEquals(m3.m01, 2f, 0)
    assertEquals(m3.m02, 4f, 0)
   
    assertEquals(m3.m10, 6f, 0)
    assertEquals(m3.m11, 8f, 0)
    assertEquals(m3.m12, 10f, 0)
   
    assertEquals(m3.m20, 12f, 0)
    assertEquals(m3.m21, 14f, 0)
    assertEquals(m3.m22, 16f, 0)
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
   
    assertEquals(m3.m00, 0f, 0)
    assertEquals(m3.m01, 0f, 0)
    assertEquals(m3.m02, 0f, 0)
   
    assertEquals(m3.m10, 0f, 0)
    assertEquals(m3.m11, 0f, 0)
    assertEquals(m3.m12, 0f, 0)
   
    assertEquals(m3.m20, 0f, 0)
    assertEquals(m3.m21, 0f, 0)
    assertEquals(m3.m22, 0f, 0)
 }
  
  def testMulMatrix() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    var m2 = new Mat3
    m2.set(m1)
    
    val m3 = m1 * m2
   
    assertEquals(m3.m00, 15f, 0)
    assertEquals(m3.m01, 18f, 0)
    assertEquals(m3.m02, 21f, 0)
   
    assertEquals(m3.m10, 42f, 0)
    assertEquals(m3.m11, 54f, 0)
    assertEquals(m3.m12, 66f, 0)
   
    assertEquals(m3.m20, 69f, 0)
    assertEquals(m3.m21, 90f, 0)
    assertEquals(m3.m22, 111f, 0)
  }
  
  def testMulLocal() = {
    var m1 = new Mat3
    m1.set(0,1,2,
           3,4,5,
           6,7,8)
    
    var m2 = new Mat3
    m2.set(m1)
    
    m1 *= m2  
   
    assertEquals(m1.m00, 15f, 0)
    assertEquals(m1.m01, 18f, 0)
    assertEquals(m1.m02, 21f, 0)
   
    assertEquals(m1.m10, 42f, 0)
    assertEquals(m1.m11, 54f, 0)
    assertEquals(m1.m12, 66f, 0)
   
    assertEquals(m1.m20, 69f, 0)
    assertEquals(m1.m21, 90f, 0)
    assertEquals(m1.m22, 111f, 0)
 }
}
