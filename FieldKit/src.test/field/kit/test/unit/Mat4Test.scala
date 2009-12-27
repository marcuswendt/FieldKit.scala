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
   
    assertEquals(m1(0,0), 0f, 0)
    assertEquals(m1(0,1), 1f, 0)
    assertEquals(m1(0,2), 2f, 0)
    assertEquals(m1(0,3), 3f, 0)
   
    assertEquals(m1(1,0), 4f, 0)
    assertEquals(m1(1,1), 5f, 0)
    assertEquals(m1(1,2), 6f, 0)
    assertEquals(m1(1,3), 7f, 0)
   
    assertEquals(m1(2,0), 8f, 0)
    assertEquals(m1(2,1), 9f, 0)
    assertEquals(m1(2,2), 10f, 0)
    assertEquals(m1(2,3), 11f, 0)
   
    assertEquals(m1(3,0), 12f, 0)
    assertEquals(m1(3,1), 13f, 0)
    assertEquals(m1(3,2), 14f, 0)
    assertEquals(m1(3,3), 15f, 0)
  }
   
  def testSetFloats() = {
    var m1 = new Mat4
    m1 := (0,1,2,3, 
           4,5,6,7,
           8,9,10,11,
           12,13,14,15)
    
    assertEquals(m1.m00, 0f, 0)
    assertEquals(m1.m01, 1f, 0)
    assertEquals(m1.m02, 2f, 0)
    assertEquals(m1.m03, 3f, 0)
   
    assertEquals(m1.m10, 4f, 0)
    assertEquals(m1.m11, 5f, 0)
    assertEquals(m1.m12, 6f, 0)
    assertEquals(m1.m13, 7f, 0)
   
    assertEquals(m1.m20, 8f, 0)
    assertEquals(m1.m21, 9f, 0)
    assertEquals(m1.m22, 10f, 0)
    assertEquals(m1.m23, 11f, 0)
   
    assertEquals(m1.m30, 12f, 0)
    assertEquals(m1.m31, 13f, 0)
    assertEquals(m1.m32, 14f, 0)
    assertEquals(m1.m33, 15f, 0)
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
   
    assertEquals(m1(0,0), 0f, 0)
    assertEquals(m1(0,1), 1f, 0)
    assertEquals(m1(0,2), 2f, 0)
    assertEquals(m1(0,3), 3f, 0)
   
    assertEquals(m1(1,0), 4f, 0)
    assertEquals(m1(1,1), 5f, 0)
    assertEquals(m1(1,2), 6f, 0)
    assertEquals(m1(1,3), 7f, 0)
   
    assertEquals(m1(2,0), 8f, 0)
    assertEquals(m1(2,1), 9f, 0)
    assertEquals(m1(2,2), 10f, 0)
    assertEquals(m1(2,3), 11f, 0)
   
    assertEquals(m1(3,0), 12f, 0)
    assertEquals(m1(3,1), 13f, 0)
    assertEquals(m1(3,2), 14f, 0)
    assertEquals(m1(3,3), 15f, 0)
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
   
    assertEquals(m1.m00, 0f, 0)
    assertEquals(m1.m01, 2f, 0)
    assertEquals(m1.m02, 4f, 0)
    assertEquals(m1.m03, 6f, 0)
   
    assertEquals(m1.m10, 8f, 0)
    assertEquals(m1.m11, 10f, 0)
    assertEquals(m1.m12, 12f, 0)
    assertEquals(m1.m13, 14f, 0)
   
    assertEquals(m1.m20, 16f, 0)
    assertEquals(m1.m21, 18f, 0)
    assertEquals(m1.m22, 20f, 0)
    assertEquals(m1.m23, 22f, 0)
   
    assertEquals(m1.m30, 24f, 0)
    assertEquals(m1.m31, 26f, 0)
    assertEquals(m1.m32, 28f, 0)
    assertEquals(m1.m33, 30f, 0)
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
   
    assertEquals(m3.m00, 0f, 0)
    assertEquals(m3.m01, 2f, 0)
    assertEquals(m3.m02, 4f, 0)
    assertEquals(m3.m03, 6f, 0)
   
    assertEquals(m3.m10, 8f, 0)
    assertEquals(m3.m11, 10f, 0)
    assertEquals(m3.m12, 12f, 0)
    assertEquals(m3.m13, 14f, 0)
   
    assertEquals(m3.m20, 16f, 0)
    assertEquals(m3.m21, 18f, 0)
    assertEquals(m3.m22, 20f, 0)
    assertEquals(m3.m23, 22f, 0)
   
    assertEquals(m3.m30, 24f, 0)
    assertEquals(m3.m31, 26f, 0)
    assertEquals(m3.m32, 28f, 0)
    assertEquals(m3.m33, 30f, 0)
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
   
    assertEquals(m3.m00, 0f, 0)
    assertEquals(m3.m01, 0f, 0)
    assertEquals(m3.m02, 0f, 0)
    assertEquals(m3.m03, 0f, 0)
   
    assertEquals(m3.m10, 0f, 0)
    assertEquals(m3.m11, 0f, 0)
    assertEquals(m3.m12, 0f, 0)
    assertEquals(m3.m13, 0f, 0)
   
    assertEquals(m3.m20, 0f, 0)
    assertEquals(m3.m21, 0f, 0)
    assertEquals(m3.m22, 0f, 0)
    assertEquals(m3.m23, 0f, 0)
   
    assertEquals(m3.m30, 0f, 0)
    assertEquals(m3.m31, 0f, 0)
    assertEquals(m3.m32, 0f, 0)
    assertEquals(m3.m33, 0f, 0)
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
   
    assertEquals(m3.m00, 56f, 0)
    assertEquals(m3.m01, 62f, 0)
    assertEquals(m3.m02, 68f, 0)
    assertEquals(m3.m03, 74f, 0)
   
    assertEquals(m3.m10, 152.0f, 0)
    assertEquals(m3.m11, 174f, 0)
    assertEquals(m3.m12, 196f, 0)
    assertEquals(m3.m13, 218f, 0)
   
    assertEquals(m3.m20, 248f, 0)
    assertEquals(m3.m21, 286f, 0)
    assertEquals(m3.m22, 324f, 0)
    assertEquals(m3.m23, 362f, 0)
   
    assertEquals(m3.m30, 344f, 0)
    assertEquals(m3.m31, 398f, 0)
    assertEquals(m3.m32, 452f, 0)
    assertEquals(m3.m33, 506f, 0)
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
   
    assertEquals(m1.m00, 56f, 0)
    assertEquals(m1.m01, 62f, 0)
    assertEquals(m1.m02, 68f, 0)
    assertEquals(m1.m03, 74f, 0)
   
    assertEquals(m1.m10, 152.0f, 0)
    assertEquals(m1.m11, 174f, 0)
    assertEquals(m1.m12, 196f, 0)
    assertEquals(m1.m13, 218f, 0)
   
    assertEquals(m1.m20, 248f, 0)
    assertEquals(m1.m21, 286f, 0)
    assertEquals(m1.m22, 324f, 0)
    assertEquals(m1.m23, 362f, 0)
   
    assertEquals(m1.m30, 344f, 0)
    assertEquals(m1.m31, 398f, 0)
    assertEquals(m1.m32, 452f, 0)
    assertEquals(m1.m33, 506f, 0)
 }
}
