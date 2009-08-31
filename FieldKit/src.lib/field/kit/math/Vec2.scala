/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.math

/**
 * Companion object to <code>Vec2</code>
 * @author Marcus Wendt
 */
object Vec2 {
  import Common._
  
  val ZERO = new Vec2(0, 0)
  val UNIT_X = new Vec2(1, 0)
  val UNIT_Y = new Vec2(0, 1)
  val UNIT_XY = new Vec2(1, 1)
  
  def apply() = new Vec2(0, 0)
  def apply(s:Float) = new Vec2(s,s)
//  def apply(x:Float, y:Float) = new Vec2(x, y)

  def apply(v:Vec2) = new Vec2(v.x,v.y)
  def apply(s:String) = { val v = new Vec2(0,0); v := s; v }
  
  /**
   * Creates a new random unit vector
   * @return a new random normalized unit vector.
   */
  def random = new Vec2(randomNormal, randomNormal)
  
  /** computes the intersection point between two rays */
  def rayIntersectionPoint(origin1:Vec2, direction1:Vec2, origin2:Vec2, direction2:Vec2, result:Vec2) = {
    val v3bx = origin2.x - origin1.x
    val v3by = origin2.y - origin1.y

    val perP1 = v3bx * direction2.y - v3by * direction2.x
    val perP2 = direction1.x * direction2.y - direction1.y * direction2.x

    val t = perP1 / perP2;
    
    result.x = origin1.x + direction1.x * t
    result.y = origin1.y + direction1.y * t
  }
}

/**
 * 2 Dimensional Float Vector
 * @author Marcus Wendt
 */
case class Vec2(var x:Float, var y:Float) {
  import java.nio.FloatBuffer
  import Common._
  
  def this() = this(0,0)
  
  // -- Setters ----------------------------------------------------------------
  /** 
   * Sets this Vectors components to the given Vec2
   * @return itself
   */
  final def :=(v:Vec2) = {this.x=v.x; this.y=v.y; this}
  
  /** 
   * Sets this Vectors components to the given Vec2
   * @return itself
   */
  final def set(v:Vec2) = :=(v)
  
  /**
   * Sets this Vectors components to the given Floats
   * @return itself
   */
  final def :=(x:Float, y:Float) = { this.x=x; this.y=y; this }
  
  /**
   * Sets this Vectors components to the given Floats
   * @return itself
   */
  final def set(x:Float, y:Float) = :=(x, y)
  
  /**
   * Sets all components of this Vector to the given Float
   * @return itself
   */
  final def :=(s:Float) = { this.x=s; this.y=s; this }
  
  /**
   * Sets all components of this Vector to the given Float
   * @return itself
   */
  final def set(s:Float) = :=(s)
  
  /** 
   * Sets the xyz components to the data from a given buffer at a given index
   * @return itself
   */
  final def :=(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    this.x = buffer get i
    this.y = buffer get i+1
    this
  }
  
  /** 
   * Sets the xyz components to the data from a given buffer at a given index
   * @return itself
   */
  final def set(buffer:FloatBuffer, index:Int) = :=(buffer,index)
  
  /** 
   * Attempts to parse the given String to set this Vectors components
   * @return itself
   */
  final def :=(s:String) = {
    // TODO reimplement this method
    // if(s != null) {
    //       val iter = DECIMAL.findAllIn(s)
    //       val list = iter.toList
    //       
    //       var index = 0
    //       def next = { 
    //         var f = abs(list(index).toFloat)
    //         index += 1
    //         f
    //       }
    //       
    //       list.size match {
    //         // set xy to a scalar
    //         case 1 =>
    //           val f = next
    //           this.x = f 
    //           this.y = f
    //         // set xy independently
    //         case 2 =>
    //           this.x = next 
    //           this.y = next    
    //         case _ => throw new Exception("Couldnt parse String '"+ s +"'")
    //       } 
    //     }
    this
  }
  
  /** 
   * Attempts to parse the given String to set this Vectors components
   * @return itself
   */
  final def set(s:String) = :=(s)
  
  
  // -- Immutable Operations ---------------------------------------------------
   /**
    * Adds the given Float to this vector
    * @return result as new vector 
    */
   final def +(s:Float) = new Vec2(x + s, y + s)

   /**
    * Subtracts the given float from this vector
    * @return result as new vector 
    */
   final def -(s:Float):Vec2 = new Vec2(x - s, y - s)

   /** 
    * Multiplies the given float with this vector
    * @return result as new vector 
    */
   final def *(s:Float):Vec2 = new Vec2(x * s, y * s)

   /** 
    * Divides this vector through the given float
    * @return result as new vector 
    */
   final def /(s:Float):Vec2 = new Vec2(x / s, y / s)

   /** 
    * Subtracts the given <code>Vec2</code> from this <code>Vec2</code> and returns the result
    * @return result
    */
   final def -(v:Vec2) = new Vec2(this.x - v.x, this.y - v.y)

   /** 
    * Adds the given <code>Vec2</code> to this <code>Vec2</code> and returns the result 
    * @return result
    */
   final def +(v:Vec2) = new Vec2(this.x + v.x, this.y + v.y)

   /** 
    * Multiplies the given <code>Vec2</code> with this <code>Vec2</code> and returns the result
    * @return result
    */
   final def *(v:Vec2) = new Vec2(this.x * v.x, this.y * v.y)

   /** 
    * Divides this <code>Vec2</code> through the given <code>Vec2</code> and returns the result
    * @return result
    */
   final def /(v:Vec2) = new Vec2(this.x / v.x, this.y / v.y)

   // -- Mutable Operations ----------------------------------------------------
   final def +=(s:Float) = { x+=s; y+=s; this }
   final def -=(s:Float) = { x-=s; y-=s; this }
   final def *=(s:Float) = { x*=s; y*=s; this }
   final def /=(s:Float) = { x/=s; y/=s; this }

   final def +=(v:Vec2) = { x+=v.x; y+=v.y; this }
   final def -=(v:Vec2) = { x-=v.x; y-=v.y; this }
   final def *=(v:Vec2) = { x*=v.x; y*=v.y; this }
   final def /=(v:Vec2) = { x/=v.x; y/=v.y; this }
  
  final def length = Math.sqrt(lengthSquared).toFloat
  final def lengthSquared = x * x + y * y
  
  final def distance(v:Vec2) = Math.sqrt(distanceSquared(v.x, v.y)).toFloat
  final def distance(x:Float, y:Float):Float = Math.sqrt(distanceSquared(x,y)).toFloat
  
  final def distanceSquared(v:Vec2):Float = distanceSquared(v.x, v.y)
  final def distanceSquared(x:Float, y:Float) = {
    val dx = this.x - x
    val dy = this.y - y
    dx * dx + dy * dy
  }
  
  /**
   * It is assumed that both this vector and the given vector are unit vectors (iow, normalized).
   * @return the angle (in radians) between two vectors.
   */
  final def angleBetween(v:Vec2) = acos(dot(v))
  
  /**
   * Calculates the dot product of this vector with a provided vector.
   * @return the resultant dot product of this vector and a given vector.
   */
  final def dot(v:Vec2) = x * v.x + y * v.y
  
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
  
  final def perpendiculate = {
    val tmp = x
    this.x = y
    this.y = -tmp
    this
  }
  
  // -- Other Operations -------------------------------------------------------
  final def zero = :=(0,0)
  
  final def put(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    buffer.put(i, x)
    buffer.put(i + 1, y)
    this    
  }
  
  final def put(buffer:FloatBuffer) = {
    buffer put x
    buffer put y
    this    
  }
  
  /**
   * Interpolates towards the given target vector
   * @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
   */
  final def slerp(target:Vec2, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
    this
  }
  
  /**
   * Converts the current vector into polar coordinates. After the conversion
   * the x component of the vector contains the radius (magnitude) and y the
   * rotation angle.
   * 
   * @return itself as polar vector
   */
  def toPolar = {
    val r = sqrt(x * x + y * y)
    y = atan2(y, x)
    x = r
    this
  }
  
  /**
   * Converts the vector from polar to Cartesian space. Assumes this order:
   * x=radius, y=theta
   * 
   * @return itself as Cartesian vector
   */
  def toCartesian {
    val tmp = x * cos(y)
    y = x * sin(y)
    x = tmp
    this
  }
  
  /** checks wether one or several components of this vector are Not a Number or Infinite */
  final def isValid:Boolean = { 
    import java.lang.Float
    if (Float.isInfinite(x)) return false
    if (Float.isInfinite(y)) return false
    if (Float.isNaN(x)) return false
    if (Float.isNaN(y)) return false
    true
  }
  
  // helpers
  override def clone = new Vec2(x,y)
  
  override def toString = "Vec2["+ toLabel +"]"
  
  def toLabel = "X"+ x +" Y"+ y
}
