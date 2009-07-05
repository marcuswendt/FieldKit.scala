/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.math

/**
 * 3x3 Float Matrix
 * @author Marcus Wendt
 */
class Mat3 extends MatrixF(3) {
  var m00 = 0f
  var m01 = 0f
  var m02 = 0f

  var m10 = 0f
  var m11 = 0f
  var m12 = 0f

  var m20 = 0f
  var m21 = 0f
  var m22 = 0f
  
  def set(m:Mat3) = {
    m00 = m.m00
    m01 = m.m01
    m02 = m.m02
    
    m10 = m.m10
    m11 = m.m11
    m12 = m.m12
    
    m20 = m.m20
    m21 = m.m21
    m22 = m.m22
    
    this
  }
  
  def set(m00:Float, m01:Float, m02:Float, 
		  m10:Float, m11:Float, m12:Float,
		  m20:Float, m21:Float, m22:Float) = {
	this.m00 = m00
	this.m01 = m01
	this.m02 = m02
  
 	this.m10 = m10
	this.m11 = m11
	this.m12 = m12
  
 	this.m20 = m20
	this.m21 = m21
	this.m22 = m22
  
 	this
  }
  
  /** @return Returns the value at the given position in the matrix. */
  def apply(i:Int, j:Int) = {
    i match {
      case 0 =>
        j match {
	      case 0 => m00 
	      case 1 => m01
	      case 2 => m02
        }
      case 1 =>
        j match {
	      case 0 => m10 
	      case 1 => m11
	      case 2 => m12
        }
      case 2 =>
        j match {
	      case 0 => m20 
	      case 1 => m21
	      case 2 => m22
        }
    }
  }
  
  /** Sets the value at position i,j in the matrix. */
  def update(i:Int, j:Int, s:Float) = {
    i match {
      case 0 =>
        j match {
	      case 0 => m00 = s
	      case 1 => m01 = s
	      case 2 => m02 = s
        }
      case 1 =>
        j match {
	      case 0 => m10 = s
	      case 1 => m11 = s
	      case 2 => m12 = s
        }
      case 2 =>
        j match {
	      case 0 => m20 = s
	      case 1 => m21 = s
	      case 2 => m22 = s
        }
    }
    
    this
  }
  
  /** Sets all of the values in this matrix to zero. */
  def zero = set(0,0,0, 0,0,0, 0,0,0)
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity = set(1,0,0, 0,1,0, 0,0,1)
  
  // -- Matrix Operations ------------------------------------------------------
  def +=(m:Mat3) = this + (m, this)
  def -=(m:Mat3) = this - (m, this)
  def *=(m:Mat3) = this * (m, this)
  
  def +(m:Mat3):Mat3 = this + (m, null)
  def -(m:Mat3):Mat3 = this - (m, null)
  def *(m:Mat3):Mat3 = this * (m, null)
  
  def +(m:Mat3, product:Mat3) = {
    val p = if(product == null) new Mat3 else product
    p.m00 = this.m00 + m.m00
    p.m01 = this.m01 + m.m01
    p.m02 = this.m02 + m.m02
    
    p.m10 = this.m10 + m.m10
    p.m11 = this.m11 + m.m11
    p.m12 = this.m12 + m.m12
    
    p.m20 = this.m20 + m.m20
    p.m21 = this.m21 + m.m21
    p.m22 = this.m22 + m.m22
    
    p
  }
  
  def -(m:Mat3, product:Mat3) = {
    val p = if(product == null) new Mat3 else product
    p.m00 = this.m00 - m.m00
    p.m01 = this.m01 - m.m01
    p.m02 = this.m02 - m.m02
    
    p.m10 = this.m10 - m.m10
    p.m11 = this.m11 - m.m11
    p.m12 = this.m12 - m.m12
    
    p.m20 = this.m20 - m.m20
    p.m21 = this.m21 - m.m21
    p.m22 = this.m22 - m.m22
    
    p
  } 

  def *(m:Mat3, product:Mat3) = {
    val p = if(product == null) new Mat3 else product
    
    val t00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20
    val t01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21
    val t02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22

    val t10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20
    val t11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 
    val t12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22
    
    val t20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20
    val t21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21
    val t22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22
    
    p.m00 = t00
    p.m01 = t01
    p.m02 = t02
    
    p.m10 = t10
    p.m11 = t11
    p.m12 = t12
    
    p.m20 = t20
    p.m21 = t21
    p.m22 = t22
    
    p
  }
  
  // -- Scalar Local Operations ------------------------------------------------
  def +=(s:Float) = {
    m00 += s
	m01 += s
	m02 += s
 
	m10 += s
	m11 += s
	m12 += s
 
	m20 += s
	m21 += s
	m22 += s
 
	this
  }
  
  def -=(s:Float) = {
    m00 -= s
	m01 -= s
	m02 -= s
 
	m10 -= s
	m11 -= s
	m12 -= s
 
	m20 -= s
	m21 -= s
	m22 -= s
 
	this
  }
  
  def *=(s:Float) = {
    m00 *= s
	m01 *= s
	m02 *= s
 
	m10 *= s
	m11 *= s
	m12 *= s
 
	m20 *= s
	m21 *= s
	m22 *= s
 
	this
  }
  
  // -- Scalar Operations ------------------------------------------------------
  def +(s:Float):Mat3 = this + (s, null)
  def -(s:Float):Mat3 = this - (s, null)
  def *(s:Float):Mat3 = this * (s, null)
  
  def +(s:Float, product:Mat3):Mat3 = {
    val p = if(product == null) new Mat3 else product
    p.set(this)
    p += s
    p
  }
  
  def -(s:Float, product:Mat3):Mat3 = {
    val p = if(product == null) new Mat3 else product
    p.set(this)
    p -= s
    p
  }
  
  def *(s:Float, product:Mat3):Mat3 = {
    val p = if(product == null) new Mat3 else product
    p.set(this)
    p *= s
    p
  } 
  
  // -- Utilities -----------------------------------------------------------------
  override def toString = 
    "Mat3 [\n"+
    " "+ m00 +" "+ m01 +" "+ m02 +"\n" +
    " "+ m10 +" "+ m11 +" "+ m12 +"\n" +
    " "+ m20 +" "+ m21 +" "+ m22 +"\n" +
    "] \n"
}
