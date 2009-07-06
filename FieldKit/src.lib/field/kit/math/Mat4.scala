/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.math

/**
 * 4x4 Float Matrix
 * @author Marcus Wendt
 */
class Mat4 extends MatrixF(4) {
  var m00 = 0f
  var m01 = 0f
  var m02 = 0f
  var m03 = 0f

  var m10 = 0f
  var m11 = 0f
  var m12 = 0f
  var m13 = 0f

  var m20 = 0f
  var m21 = 0f
  var m22 = 0f
  var m23 = 0f

  var m30 = 0f
  var m31 = 0f
  var m32 = 0f
  var m33 = 0f
  
  def set(m:Mat4) = {
    m00 = m.m00
    m01 = m.m01
    m02 = m.m02
    m03 = m.m03
    
    m10 = m.m10
    m11 = m.m11
    m12 = m.m12
    m13 = m.m13
    
    m20 = m.m20
    m21 = m.m21
    m22 = m.m22
    m23 = m.m23
    
    m30 = m.m30
    m31 = m.m31
    m32 = m.m32
    m33 = m.m33
    
    this
  }
  
  def set(m00:Float, m01:Float, m02:Float, m03:Float, 
  		  m10:Float, m11:Float, m12:Float, m13:Float, 
  		  m20:Float, m21:Float, m22:Float, m23:Float, 
  		  m30:Float, m31:Float, m32:Float, m33:Float) = {
	this.m00 = m00
	this.m01 = m01
	this.m02 = m02
 	this.m03 = m03
  
 	this.m10 = m10
	this.m11 = m11
	this.m12 = m12
 	this.m13 = m13
  
 	this.m20 = m20
	this.m21 = m21
	this.m22 = m22
 	this.m23 = m23
  
 	this.m30 = m30
	this.m31 = m31
	this.m32 = m32
 	this.m33 = m33
  
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
	      case 3 => m03
        }
      case 1 =>
        j match {
	      case 0 => m10 
	      case 1 => m11
	      case 2 => m12
	      case 3 => m13
        }
      case 2 =>
        j match {
	      case 0 => m20 
	      case 1 => m21
	      case 2 => m22
	      case 3 => m23
        }
      case 3 =>
        j match {
	      case 0 => m30 
	      case 1 => m31
	      case 2 => m32
	      case 3 => m33
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
	      case 3 => m03 = s
        }
      case 1 =>
        j match {
	      case 0 => m10 = s
	      case 1 => m11 = s
	      case 2 => m12 = s
	      case 3 => m13 = s
        }
      case 2 =>
        j match {
	      case 0 => m20 = s
	      case 1 => m21 = s
	      case 2 => m22 = s
	      case 3 => m23 = s
        }
      case 3 =>
        j match {
	      case 0 => m30 = s 
	      case 1 => m31 = s
	      case 2 => m32 = s
	      case 3 => m33 = s
        }
    }
    
    this
  }
  
  /** Sets all of the values in this matrix to zero. */
  def zero = set(0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0)
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity = set(1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1)
  
  // -- Matrix Operations ------------------------------------------------------
  def +=(m:Mat4) = this + (m, this)
  def -=(m:Mat4) = this - (m, this)
  def *=(m:Mat4) = this * (m, this)
  
  def +(m:Mat4):Mat4 = this + (m, null)
  def -(m:Mat4):Mat4 = this - (m, null)
  def *(m:Mat4):Mat4 = this * (m, null)
  
  def +(m:Mat4, product:Mat4) = {
    val p = if(product == null) new Mat4 else product
    p.m00 = this.m00 + m.m00
    p.m01 = this.m01 + m.m01
    p.m02 = this.m02 + m.m02
    p.m03 = this.m03 + m.m03
    
    p.m10 = this.m10 + m.m10
    p.m11 = this.m11 + m.m11
    p.m12 = this.m12 + m.m12
    p.m13 = this.m13 + m.m13
    
    p.m20 = this.m20 + m.m20
    p.m21 = this.m21 + m.m21
    p.m22 = this.m22 + m.m22
    p.m23 = this.m23 + m.m23
    
    p.m30 = this.m30 + m.m30
    p.m31 = this.m31 + m.m31
    p.m32 = this.m32 + m.m32
    p.m33 = this.m33 + m.m33
    
    p
  }
  
  def -(m:Mat4, product:Mat4) = {
    val p = if(product == null) new Mat4 else product
    p.m00 = this.m00 - m.m00
    p.m01 = this.m01 - m.m01
    p.m02 = this.m02 - m.m02
    p.m03 = this.m03 - m.m03
    
    p.m10 = this.m10 - m.m10
    p.m11 = this.m11 - m.m11
    p.m12 = this.m12 - m.m12
    p.m13 = this.m13 - m.m13
    
    p.m20 = this.m20 - m.m20
    p.m21 = this.m21 - m.m21
    p.m22 = this.m22 - m.m22
    p.m23 = this.m23 - m.m23
    
    p.m30 = this.m30 - m.m30
    p.m31 = this.m31 - m.m31
    p.m32 = this.m32 - m.m32
    p.m33 = this.m33 - m.m33
    
    p
  } 

  def *(m:Mat4, product:Mat4) = {
    val p = if(product == null) new Mat4 else product
    
    val t00 = m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30
    val t01 = m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31
    val t02 = m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32
    val t03 = m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33

    val t10 = m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30
    val t11 = m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31
    val t12 = m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32
    val t13 = m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33
    
    val t20 = m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30
    val t21 = m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31
    val t22 = m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32
    val t23 = m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33
    
    val t30 = m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30
    val t31 = m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31
    val t32 = m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32
    val t33 = m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33
    
    p.m00 = t00
    p.m01 = t01
    p.m02 = t02
    p.m03 = t03
    
    p.m10 = t10
    p.m11 = t11
    p.m12 = t12
    p.m13 = t13
    
    p.m20 = t20
    p.m21 = t21
    p.m22 = t22
    p.m23 = t23
    
    p.m30 = t30
    p.m31 = t31
    p.m32 = t32
    p.m33 = t33
    
    p
  }
  
  // -- Scalar Local Operations ------------------------------------------------
  def +=(s:Float) = {
    m00 += s
	m01 += s
	m02 += s
	m03 += s
 
	m10 += s
	m11 += s
	m12 += s
	m13 += s
 
	m20 += s
	m21 += s
	m22 += s
	m23 += s
 
	m30 += s
	m31 += s
	m32 += s
	m33 += s
 
	this
  }
  
  def -=(s:Float) = {
    m00 -= s
	m01 -= s
	m02 -= s
	m03 -= s
 
	m10 -= s
	m11 -= s
	m12 -= s
	m13 -= s
 
	m20 -= s
	m21 -= s
	m22 -= s
	m23 -= s
 
	m30 -= s
	m31 -= s
	m32 -= s
	m33 -= s
 
	this
  }
  
  def *=(s:Float) = {
    m00 *= s
	m01 *= s
	m02 *= s
	m03 *= s
 
	m10 *= s
	m11 *= s
	m12 *= s
	m13 *= s
 
	m20 *= s
	m21 *= s
	m22 *= s
	m23 *= s
 
	m30 *= s
	m31 *= s
	m32 *= s
	m33 *= s
 
	this
  }
  
  // -- Scalar Operations ------------------------------------------------------
  def +(s:Float):Mat4 = this + (s, null)
  def -(s:Float):Mat4 = this - (s, null)
  def *(s:Float):Mat4 = this * (s, null)
  
  def +(s:Float, product:Mat4):Mat4 = {
    val p = if(product == null) new Mat4 else product
    p.set(this)
    p += s
    p
  }
  
  def -(s:Float, product:Mat4):Mat4 = {
    val p = if(product == null) new Mat4 else product
    p.set(this)
    p -= s
    p
  }
  
  def *(s:Float, product:Mat4):Mat4 = {
    val p = if(product == null) new Mat4 else product
    p.set(this)
    p *= s
    p
  }
  
  // -- Transform Operations ---------------------------------------------------
  def translate(tx:Float, ty:Float):Mat4 = translate(tx, ty, 0)
  
  def translate(tx:Float, ty:Float, tz:Float) = {
    m03 += tx*m00 + ty*m01 + tz*m02
    m13 += tx*m10 + ty*m11 + tz*m12
    m23 += tx*m20 + ty*m21 + tz*m22
    m33 += tx*m30 + ty*m31 + tz*m32
    this
  }
  
  // -- Utilities --------------------------------------------------------------
  override def toString = 
    "Mat4 [\n"+
    " "+ m00 +" "+ m01 +" "+ m02 +" "+ m03 +"\n" +
    " "+ m10 +" "+ m11 +" "+ m12 +" "+ m13 +"\n" +
    " "+ m20 +" "+ m21 +" "+ m22 +" "+ m23 +"\n" +
    " "+ m30 +" "+ m31 +" "+ m32 +" "+ m33 +"\n" +
    "] \n"
  
}