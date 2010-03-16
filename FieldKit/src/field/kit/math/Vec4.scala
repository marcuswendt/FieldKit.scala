/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 08, 2009 */
package field.kit.math

object Vec4 {
  def apply() = new Vec4(0,0,0,0)
  def apply(s:Float) = new Vec4(s,s,s,s)
  def apply(s:Float, w:Float) = new Vec4(s,s,s,w)
}

/**
 * 4 Dimensional Float Vector
 * @author Marcus Wendt
 */
class Vec4(var x:Float, var y:Float, var z:Float, var w:Float) extends Vec {
  import java.nio.FloatBuffer
  import Common._
  
  def this() = this(0,0,0,0)
  
  // -- Setters ----------------------------------------------------------------
  final def :=(v:Vec4) = { this.x=v.x; this.y=v.y; this.z=v.z; this.w=v.z; this }

  def :=(s:Float) = { this.x=s; this.y=s; this.z=s; this.w=s; this }
  
  // -- Immutable Operations ---------------------------------------------------
  /**
   * Adds the given Float to this vector
   * @return result as new vector 
   */
  final def +(s:Float) = new Vec4(x+s, y+s, z+s, w+s)
  
  /**
   * Subtracts the given float from this vector
   * @return result as new vector 
   */
  final def -(s:Float) = new Vec4(x-s, y-s, z-s, w-s)
  
  /** 
   * Multiplies the given float with this vector
   * @return result as new vector 
   */
  final def *(s:Float) = new Vec4(x*s, y*s, z*s, w*s)
  
  /** 
   * Divides this vector through the given float
   * @return result as new vector 
   */
  final def /(s:Float) = new Vec4(x/s, y/s, z/s, w/s)
  
  // -- Mutable Operations -----------------------------------------------------
  final def +=(s:Float) = { x+=s; y+=s; z+=s; w+=s; this }
  final def -=(s:Float) = { x-=s; y-=s; z-=s; w-=s; this }
  final def *=(s:Float) = { x*=s; y*=s; z*=s; w*=s; this }
  final def /=(s:Float) = { x/=s; y/=s; z/=s; w/=s; this }
  
  final def +=(v:Vec4) = { x+=v.x; y+=v.y; z+=v.z; w+=v.w; this }
  final def -=(v:Vec4) = { x-=v.x; y-=v.y; z-=v.z; w-=v.w; this }
  final def *=(v:Vec4) = { x*=v.x; y*=v.y; z*=v.z; w*=v.w; this }
  final def /=(v:Vec4) = { x/=v.x; y/=v.y; z/=v.z; w/=v.w; this }
  
  // -- Geometric Operations ---------------------------------------------------
  final def length = sqrt(lengthSquared).toFloat
  
  final def lengthSquared = x*x + y*y + z*z + w*w
  
  /** makes sure this vector does not exceed a certain length */
  final def clamp(max:Float) = {
    val l = length
    if(l > max) {
      this /= l
      this *= max
    }
    this
  }  
  
  // -- Other Operations -------------------------------------------------------
  /** Resets this vectors components all to zero */
  final def zero = { x=0; y=0; z=0; w=0 }
  
  /**
   * Normalizes this vector.
   * @return itself
   */
  final def normalize = {
    val l = length
    if(l != 0)
      this /= l
    else
      this /= 1
    this
  }
  
  /**
   * interpolates towards the given target Vector
   * @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
   */
  final def slerp(target:Vec4, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
    this.z = z * (1 - delta) + target.z * delta
    this.w = w * (1 - delta) + target.w * delta
    this
  }
}
