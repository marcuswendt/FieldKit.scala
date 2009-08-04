/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.math

/**
 * Companion object to <code>Vec3</code>
 * @author Marcus Wendt
 */
object Vec3 {
  val ZERO = new Vec3(0, 0, 0)
  val UNIT_X = new Vec3(1, 0, 0)
  val UNIT_Y = new Vec3(0, 1, 0)
  val UNIT_Z = new Vec3(0, 0, 1)
  val UNIT_XYZ = new Vec3(1, 1, 1)
  
  /**
   * Creates a new random unit vector
   * @return a new random normalized unit vector.
   */
  def random = new Vec3(Random(), Random(), Random())
  
//  def apply() = new Vec3(0,0,0)
//  def apply(x:Float, y:Float, z:Float) = new Vec3(x,y,z)
}

/**
 * 3 Dimensional Float Vector
 * @author Marcus Wendt
 */
class Vec3(var x:Float, var y:Float, var z:Float) extends VecF {
  import java.nio.FloatBuffer
  import FMath._
  
  def this() = this(0,0,0)
  def this(v:Vec2) = this(v.x, v.y, 0)
  def this(v:Vec3) = this(v.x, v.y, v.z)
  def this(s:Float) = this(s,s,s)
  def this(s:String) = { this(); :=(s); this }
  
  final def update(i:Int, value:Float) = 
    i match {
      case 0 => this.x = value
      case 1 => this.y = value
      case 2 => this.y = value
    }
  
  //def apply(x:Float, y:Float, z:Float) = set(x,y,z)
  //def apply(v:Vec3) = set(v)
  
  // -- Setters ----------------------------------------------------------------
  /** 
   * Sets this Vectors components to the given Vec3
   * @return itself
   */
  final def :=(v:Vec3) = { this.x=v.x; this.y=v.y; this.z=v.z; this }
  
  /**
   * Sets this Vectors components to the given Floats
   * @return itself
   */
  final def :=(x:Float, y:Float, z:Float) = { this.x = x; this.y = y; this.z = z; this }
  
  /**
   * Sets all components of this Vector to the given Float
   * @return itself
   */
  final def :=(s:Float) = { this.x = s; this.y = s; this.z = s; this }
  
  /** 
   * Sets the xyz components to the data from a given buffer at a given index
   * @return itself
   */
  final def :=(buffer:FloatBuffer, index:Int) = {
    val i = index * 3
    this.x = buffer get i
    this.y = buffer get i + 1
    this.z = buffer get i + 2
    this
  }
  
  /** 
   * Attempts to parse the given String to set this Vectors components
   * @return itself
   */
  final def :=(s:String) = {
    if(s != null) {
      val iter = DECIMAL findAllIn s
      val list = iter.toList
      
      var index = 0
      def next = { 
        var f = abs(list(index).toFloat)
        index += 1
        f
      }
      
      list.size match {
        // set xyz to a scalar
        case 1 =>
          val f = next
          this.x = f 
          this.y = f
          this.z = f
        // set xyz independently
        case 3 =>
          this.x = next 
          this.y = next
          this.z = next
        case _ => throw new Exception("Couldnt parse String '"+ s +"'")
      } 
    }
    this
  }
  
  // -- Scalar Operations ------------------------------------------------------
  /**
   * Adds the given Float to this vector
   * @return result as new vector 
   */
  final def +(s:Float):Vec3 = this + (s,s,s,null)
  
  /**
   * Subtracts the given float from this vector
   * @return result as new vector 
   */
  final def -(s:Float):Vec3 = this - (s,s,s,null)
  
  /** 
   * Multiplies the given float with this vector
   * @return result as new vector 
   */
  final def *(s:Float):Vec3 = this * (s,s,s,null)
  
  /** 
   * Divides this vector through the given float
   * @return result as new vector 
   */
  final def /(s:Float):Vec3 = this / (s,s,s,null)
  
  
  
  // -- Float Operations -------------------------------------------------------
  // TODO could clean this up in Scala 2.8 when default arguments are implemented
    
  /**
   * Subtracts the given floats from this vector
   * @return result as new vector 
   */
  final def -(x:Float, y:Float, z:Float):Vec3 = this - (x,y,z, null)
  
  /** 
   * Adds the given floats to this vector
   * @return result as new vector 
   */
  final def +(x:Float, y:Float, z:Float):Vec3 = this - (x,y,z, null)
  
  /** 
   * Multiplies the given floats with this vector
   * @return result as new vector
   */
  final def *(x:Float, y:Float, z:Float):Vec3 = this - (x,y,z, null)
  
  /** 
   * Divides this vector through the given floats
   * @return result as new vector  
   */
  final def /(x:Float, y:Float, z:Float):Vec3 = this - (x,y,z, null)
  
  /** 
   * Subtracts the given floats from this vector and returns the result
   * @return result
   */
  final def -(x:Float, y:Float, z:Float, result:Vec3) = {
    val r = if(result == null) new Vec3 else result 
    r.x = this.x - x
    r.y = this.y - y
    r.z = this.z - z
    r
  }
  
  /** 
   * Adds the given floats to this vector
   * @return result
   */
  final def +(x:Float, y:Float, z:Float, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x + x
    r.y = this.y + y
    r.z = this.z + z
    r
  }
  
  /** 
   * Multiplies the given floats with this vector
   * @return result
   */
  final def *(x:Float, y:Float, z:Float, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x * x
    r.y = this.y * y
    r.z = this.z * z
    r
  }
  
  /** 
   * Divides this vector through the given floats
   * @return result
   */
  final def /(x:Float, y:Float, z:Float, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x / x
    r.y = this.y / y
    r.z = this.z / z
    r
  }
  
  
  // -- Vec3 Operations --------------------------------------------------------
  /** 
   * Subtracts the given <code>Vec3</code> from this <code>Vec3</code>
   * @return result as new vector
   */
  final def -(v:Vec3):Vec3 = this - (v, null)
  
  /** 
   * Adds the given <code>Vec3</code> to this <code>Vec3</code>
   * @return result as new vector
   */
  final def +(v:Vec3):Vec3 = this - (v, null)
  
  /** 
   * Multiplies the given <code>Vec3</code> with this <code>Vec3</code>
   * @return result as new vector 
   */
  final def *(v:Vec3):Vec3 = this - (v, null)
  
  /** 
   * Divides this <code>Vec3</code> through the given <code>Vec3</code>
   * @return result as new vector 
   */
  final def /(v:Vec3):Vec3 = this - (v, null)
  
  /** 
   * Subtracts the given <code>Vec3</code> from this <code>Vec3</code> and returns the result
   * @return result
   */
  final def -(v:Vec3, result:Vec3) = {
    val r = if(result == null) new Vec3 else result 
    r.x = this.x - v.x
    r.y = this.y - v.y
    r.z = this.z - v.z
    r
  }
  
  /** 
   * Adds the given <code>Vec3</code> to this <code>Vec3</code> and returns the result 
   * @return result
   */
  final def +(v:Vec3, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x + v.x
    r.y = this.y + v.y
    r.z = this.z + v.z
    r
  }
  
  /** 
   * Multiplies the given <code>Vec3</code> with this <code>Vec3</code> and returns the result
   * @return result
   */
  final def *(v:Vec3, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x * v.x
    r.y = this.y * v.y
    r.z = this.z * v.z
    r
  }
  
  /** 
   * Divides this <code>Vec3</code> through the given <code>Vec3</code> and returns the result
   * @return result
   */
  final def /(v:Vec3, result:Vec3) = {
    val r = if(result == null) new Vec3 else result
    r.x = this.x / v.x
    r.y = this.y / v.y
    r.z = this.z / v.z
    r
  }
  
  // -- Local Operations -------------------------------------------------------
  final def +=(s:Float):Vec3 = { x+=s; y+=s; z+=s; this }
  final def -=(s:Float):Vec3 = { x-=s; y-=s; z-=s; this }
  final def *=(s:Float):Vec3 = { x*=s; y*=s; z*=s; this }
  final def /=(s:Float):Vec3 = { x/=s; y/=s; z/=s; this }
  
  final def +=(x:Float, y:Float, z:Float) = { this.x+=x; this.y+=y; this.z+=z; this }
  final def -=(x:Float, y:Float, z:Float) = { this.x-=x; this.y-=y; this.z-=z; this }
  final def *=(x:Float, y:Float, z:Float) = { this.x*=x; this.y*=y; this.z*=z; this }
  final def /=(x:Float, y:Float, z:Float) = { this.x/=x; this.y/=y; this.z/=z; this }
  
  final def +=(v:Vec3):Vec3 = { x+=v.x; y+=v.y; z+=v.z; this }
  final def -=(v:Vec3):Vec3 = { x-=v.x; y-=v.y; z-=v.z; this }
  final def *=(v:Vec3):Vec3 = { x*=v.x; y*=v.y; z*=v.z; this }
  final def /=(v:Vec3):Vec3 = { x/=v.x; y/=v.y; z/=v.z; this }
  
  // -- Other Operations -------------------------------------------------------
  final def put(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    buffer put (i, x)
    buffer put (i+1, y)
    buffer put (i+2, z)
    this    
  }
  
  final def put(buffer:FloatBuffer) = {
    buffer put x
    buffer put y
    buffer put z
    this    
  }
  
  final def zero = :=(0,0,0)
  
  final def dot(v:Vec3) = x * v.x + y * v.y + z * v.z
  
  final def cross(v:Vec3) = {
    val tempx = (y * v.z) - (z * v.y)
    val tempy = (z * v.x) - (x * v.z)
    z = (x * v.y) - (y * v.x)
    x = tempx
    y = tempy
    this
  }

  final def lengthSquared = x * x + y * y + z * z
  
  final def distance(v:Vec3) = Math.sqrt(distanceSquared(v)).asInstanceOf[Float]
  
  final def distanceSquared(v:Vec3) = {
    val dx = x - v.x
    val dy = y - v.y
    val dz = z - v.z
    dx * dx + dy * dy + dz * dz
  }

  /** <code>angleBetween</code> returns (in radians) the angle between two vectors.
   *  It is assumed that both this vector and the given vector are unit vectors (iow, normalized). */
  final def angleBetween(v:Vec3) {
    val dotProduct = dot(v)
    Math.acos(dotProduct).asInstanceOf[Float]
  }

  /** makes sure this vector does not exceed a certain length */
  final def clamp(max:Float) = {
    val l = length
    if(l > max) {
      this /= l
      this *= max
    }
    this
  }
  
  /**
   * interpolates towards the given target Vector
   * @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
   */
  final def slerp(target:Vec3, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
    this.z = z * (1 - delta) + target.z * delta
    this
  }
  
  /** checks wether one or several components of this vector are Not a Number or Infinite */
  final def isNaNOrInfinite = { 
    import java.lang.Float
    var error = 0
    if (Float.isInfinite(x)) error -= 1
    if (Float.isInfinite(y)) error -= 1
    if (Float.isInfinite(z)) error -= 1
    if (Float.isNaN(x)) error -= 1
    if (Float.isNaN(y)) error -= 1
    if (Float.isNaN(z)) error -= 1
    error == 0
  }
  
  final def elements = new Iterator[Float] {
    var i=0
    def next = {
      i match {
        case 0 => x
        case 1 => y
        case 2 => z
        case _ => 0
      }
    }
    def hasNext = i==3
  }
  
  override def toString = "Vec3["+ toLabel +"]"
  
  def toLabel = "X"+ x +" Y"+ y +" Z"+ z
}