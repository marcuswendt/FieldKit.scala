/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.math

// —————————————————————————————————————————————————————————————————————————————
// Base classes
// —————————————————————————————————————————————————————————————————————————————
/**
 * Base class for all Matrizes
 */
abstract class Matrix[T](val size:Int) {
  /** @return Returns the value at the given position in the matrix. */
  def get(i:Int, j:Int):T
  
  /** Sets all of the values in this matrix to zero. */
  def zero
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity
}

/**
 * Base class for all Float Matrizes
 */
abstract class MatrixF(size:Int) extends Matrix[Float](size) {
  
}

// —————————————————————————————————————————————————————————————————————————————
// 3x3 Float Matrix
// —————————————————————————————————————————————————————————————————————————————
class Mat3 extends MatrixF(3) {
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
  
  def set(m00:Float, m01:Float, m02:Float, m03:Float, 
		  m10:Float, m11:Float, m12:Float, m13:Float, 
		  m20:Float, m21:Float, m22:Float, m23:Float) {
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
  }
  
  /** @return Returns the value at the given position in the matrix. */
  def get(i:Int, j:Int) = {
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
    }
  }
  
  /** Sets all of the values in this matrix to zero. */
  def zero = set(0,0,0,0, 0,0,0,0, 0,0,0,0)
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity = set(1,0,0,0, 0,1,0,0, 0,0,1,0)
}


// —————————————————————————————————————————————————————————————————————————————
// 4x4 Float Matrix
// —————————————————————————————————————————————————————————————————————————————
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
  
  def set(m00:Float, m01:Float, m02:Float, m03:Float, 
  		  m10:Float, m11:Float, m12:Float, m13:Float, 
  		  m20:Float, m21:Float, m22:Float, m23:Float, 
  		  m30:Float, m31:Float, m32:Float, m33:Float) {
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
  }
  
  /** @return Returns the value at the given position in the matrix. */
  def get(i:Int, j:Int) = {
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
  
  /** Sets all of the values in this matrix to zero. */
  def zero = set(0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0)
  
  /** Sets this matrix to the identity matrix, namely all zeros with ones along the diagonal. */
  def identity = set(1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1)
}