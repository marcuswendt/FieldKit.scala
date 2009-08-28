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

/** unit tests for the <code>Mat4</code>  class */
class Mat4Test extends TestCase {
  import field.kit.math.Mat4 
  
   def testGetXY() = {
    var m1 = new Mat4
    m1 :=(0,1,2,3, 
   		  4,5,6,7,
   		  8,9,10,11,
   		  12,13,14,15)
   
    assertEquals(m1(0,0), 0f)
    assertEquals(m1(0,1), 1f)
    assertEquals(m1(0,2), 2f)
    assertEquals(m1(0,3), 3f)
   
    assertEquals(m1(1,0), 4f)
    assertEquals(m1(1,1), 5f)
    assertEquals(m1(1,2), 6f)
    assertEquals(m1(1,3), 7f)
   
    assertEquals(m1(2,0), 8f)
    assertEquals(m1(2,1), 9f)
    assertEquals(m1(2,2), 10f)
    assertEquals(m1(2,3), 11f)
   
    assertEquals(m1(3,0), 12f)
    assertEquals(m1(3,1), 13f)
    assertEquals(m1(3,2), 14f)
    assertEquals(m1(3,3), 15f)
  }
   
  def testSetFloats() = {
    var m1 = new Mat4
    m1 := (0,1,2,3, 
           4,5,6,7,
           8,9,10,11,
           12,13,14,15)
    
    assertEquals(m1.m00, 0f)
    assertEquals(m1.m01, 1f)
    assertEquals(m1.m02, 2f)
    assertEquals(m1.m03, 3f)
   
    assertEquals(m1.m10, 4f)
    assertEquals(m1.m11, 5f)
    assertEquals(m1.m12, 6f)
    assertEquals(m1.m13, 7f)
   
    assertEquals(m1.m20, 8f)
    assertEquals(m1.m21, 9f)
    assertEquals(m1.m22, 10f)
    assertEquals(m1.m23, 11f)
   
    assertEquals(m1.m30, 12f)
    assertEquals(m1.m31, 13f)
    assertEquals(m1.m32, 14f)
    assertEquals(m1.m33, 15f)
  }
  
  def testSetXY() = {
    var m1 = new Mat4
    m1(0,0) = 0 
    m1(0,1) = 1 
    m1(0,2) = 2 
    m1(0,3) = 3 
    
    m1(1,0) = 4 
    m1(1,1) = 5 
    m1(1,2) = 6 
    m1(1,3) = 7
    
    m1(2,0) = 8 
    m1(2,1) = 9 
    m1(2,2) = 10 
    m1(2,3) = 11
    
    m1(3,0) = 12
    m1(3,1) = 13
    m1(3,2) = 14
    m1(3,3) = 15
   
    assertEquals(m1(0,0), 0f)
    assertEquals(m1(0,1), 1f)
    assertEquals(m1(0,2), 2f)
    assertEquals(m1(0,3), 3f)
   
    assertEquals(m1(1,0), 4f)
    assertEquals(m1(1,1), 5f)
    assertEquals(m1(1,2), 6f)
    assertEquals(m1(1,3), 7f)
   
    assertEquals(m1(2,0), 8f)
    assertEquals(m1(2,1), 9f)
    assertEquals(m1(2,2), 10f)
    assertEquals(m1(2,3), 11f)
   
    assertEquals(m1(3,0), 12f)
    assertEquals(m1(3,1), 13f)
    assertEquals(m1(3,2), 14f)
    assertEquals(m1(3,3), 15f)
 }
  
 def testAddLocalMatrix() = {
   var m1 = new Mat4
   m1 :=(0,1,2,3, 
   		 4,5,6,7,
   		 8,9,10,11,
   		 12,13,14,15)
   
   var m2 = new Mat4
   m2 :=(0,1,2,3, 
   		 4,5,6,7,
   		 8,9,10,11,
   		 12,13,14,15)
   
   m1 += m2
   
    assertEquals(m1.m00, 0f)
    assertEquals(m1.m01, 2f)
    assertEquals(m1.m02, 4f)
    assertEquals(m1.m03, 6f)
   
    assertEquals(m1.m10, 8f)
    assertEquals(m1.m11, 10f)
    assertEquals(m1.m12, 12f)
    assertEquals(m1.m13, 14f)
   
    assertEquals(m1.m20, 16f)
    assertEquals(m1.m21, 18f)
    assertEquals(m1.m22, 20f)
    assertEquals(m1.m23, 22f)
   
    assertEquals(m1.m30, 24f)
    assertEquals(m1.m31, 26f)
    assertEquals(m1.m32, 28f)
    assertEquals(m1.m33, 30f)
 }
 
 def testAddMatrix() = {
   var m1 = new Mat4
   m1 := (0,1,2,3, 
   		  4,5,6,7,
   		  8,9,10,11,
   		  12,13,14,15)
   
   var m2 = new Mat4
   m2 := (0,1,2,3, 
   		  4,5,6,7,
   		  8,9,10,11,
   		  12,13,14,15)
   
   val m3 = m1 + m2
   
    assertEquals(m3.m00, 0f)
    assertEquals(m3.m01, 2f)
    assertEquals(m3.m02, 4f)
    assertEquals(m3.m03, 6f)
   
    assertEquals(m3.m10, 8f)
    assertEquals(m3.m11, 10f)
    assertEquals(m3.m12, 12f)
    assertEquals(m3.m13, 14f)
   
    assertEquals(m3.m20, 16f)
    assertEquals(m3.m21, 18f)
    assertEquals(m3.m22, 20f)
    assertEquals(m3.m23, 22f)
   
    assertEquals(m3.m30, 24f)
    assertEquals(m3.m31, 26f)
    assertEquals(m3.m32, 28f)
    assertEquals(m3.m33, 30f)
 }
 
  def testSubMatrix() = {
   var m1 = new Mat4
   m1 := (0,1,2,3, 
   		  4,5,6,7,
   		  8,9,10,11,
   		  12,13,14,15)
   
   var m2 = new Mat4
   m2 := (0,1,2,3, 
   		  4,5,6,7,
   		  8,9,10,11,
   		  12,13,14,15)
   
   val m3 = m1 - m2
   
    assertEquals(m3.m00, 0f)
    assertEquals(m3.m01, 0f)
    assertEquals(m3.m02, 0f)
    assertEquals(m3.m03, 0f)
   
    assertEquals(m3.m10, 0f)
    assertEquals(m3.m11, 0f)
    assertEquals(m3.m12, 0f)
    assertEquals(m3.m13, 0f)
   
    assertEquals(m3.m20, 0f)
    assertEquals(m3.m21, 0f)
    assertEquals(m3.m22, 0f)
    assertEquals(m3.m23, 0f)
   
    assertEquals(m3.m30, 0f)
    assertEquals(m3.m31, 0f)
    assertEquals(m3.m32, 0f)
    assertEquals(m3.m33, 0f)
 }
  
  def testMulMatrix() = {
    var m1 = new Mat4
    m1 := (0,1,2,3, 
   	 	   4,5,6,7,
   		   8,9,10,11,
   		   12,13,14,15)
    
    var m2 = new Mat4
    m2 := m1
    
    val m3 = m1 * m2
   
    assertEquals(m3.m00, 56f)
    assertEquals(m3.m01, 62f)
    assertEquals(m3.m02, 68f)
    assertEquals(m3.m03, 74f)
   
    assertEquals(m3.m10, 152.0f)
    assertEquals(m3.m11, 174f)
    assertEquals(m3.m12, 196f)
    assertEquals(m3.m13, 218f)
   
    assertEquals(m3.m20, 248f)
    assertEquals(m3.m21, 286f)
    assertEquals(m3.m22, 324f)
    assertEquals(m3.m23, 362f)
   
    assertEquals(m3.m30, 344f)
    assertEquals(m3.m31, 398f)
    assertEquals(m3.m32, 452f)
    assertEquals(m3.m33, 506f)
  }
  
  def testMulLocal() = {
    var m1 = new Mat4
    m1 := (0,1,2,3, 
   	 	   4,5,6,7,
   		   8,9,10,11,
   		   12,13,14,15)
    
    var m2 = new Mat4
    m2 := m1
    m1 *= m2 
   
    assertEquals(m1.m00, 56f)
    assertEquals(m1.m01, 62f)
    assertEquals(m1.m02, 68f)
    assertEquals(m1.m03, 74f)
   
    assertEquals(m1.m10, 152.0f)
    assertEquals(m1.m11, 174f)
    assertEquals(m1.m12, 196f)
    assertEquals(m1.m13, 218f)
   
    assertEquals(m1.m20, 248f)
    assertEquals(m1.m21, 286f)
    assertEquals(m1.m22, 324f)
    assertEquals(m1.m23, 362f)
   
    assertEquals(m1.m30, 344f)
    assertEquals(m1.m31, 398f)
    assertEquals(m1.m32, 452f)
    assertEquals(m1.m33, 506f)
 }
}
