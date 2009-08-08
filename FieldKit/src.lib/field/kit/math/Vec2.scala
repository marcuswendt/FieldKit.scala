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
  import FMath._
  
  val ZERO = new Vec2(0, 0)
  val UNIT_X = new Vec2(1, 0)
  val UNIT_Y = new Vec2(0, 1)
  val UNIT_XY = new Vec2(1, 1)
  
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
class Vec2(var x:Float, var y:Float) extends VecF {
  import java.nio.FloatBuffer
  import FMath._
  
  def this() = this(0,0)
  def this(v:Vec2) = this(v.x,v.y)
  def this(s:Float) = this(s,s)
  def this(s:String) = { this(); :=(s); this }
  
  final def update(i:Int, value:Float) = 
    i match {
      case 0 => this.x = value
      case 1 => this.y = value
    }
  
  //def apply(x:Float, y:Float) = :=(x,y)
  //def apply(v:Vec2) = :=(v)
  
  // -- Setters ----------------------------------------------------------------
  /** 
   * Sets this Vectors components to the given Vec2
   * @return itself
   */
  final def :=(v:Vec2) = {this.x=v.x; this.y=v.y; this}
  
  /**
   * Sets this Vectors components to the given Floats
   * @return itself
   */
  final def :=(x:Float, y:Float) = { this.x=x; this.y=y; this }
  
  /**
   * Sets all components of this Vector to the given Float
   * @return itself
   */
  final def :=(s:Float) = { this.x=s; this.y=s; this }
  
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
        // set xy to a scalar
        case 1 =>
          val f = next
          this.x = f 
          this.y = f
        // set xy independently
        case 2 =>
          this.x = next 
          this.y = next    
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
  final def +(s:Float):Vec2 = this + (s,s,null)
  
  /**
   * Subtracts the given float from this vector
   * @return result as new vector 
   */
  final def -(s:Float):Vec2 = this - (s,s,null)
  
  /** 
   * Multiplies the given float with this vector
   * @return result as new vector 
   */
  final def *(s:Float):Vec2 = this * (s,s,null)
  
  /** 
   * Divides this vector through the given float
   * @return result as new vector 
   */
  final def /(s:Float):Vec2 = this / (s,s,null)
  
  
  // -- Float Operations -------------------------------------------------------
  // TODO could clean this up in Scala 2.8 when default arguments are implemented

  /** 
   * Subtracts the given <code>Vec2</code> from this <code>Vec2</code>
   * @return result as new vector 
   */
  final def -(x:Float, y:Float):Vec2 = this - (x,y, null)
  
  /** 
   * Adds the given <code>Vec2</code> to this <code>Vec2</code>
   * @return result as new vector 
   */
  final def +(x:Float, y:Float):Vec2 = this - (x,y, null)
  
  /** 
   * Multiplies the given <code>Vec2</code> with this <code>Vec2</code>
   * @return result as new vector 
   */
  final def *(x:Float, y:Float):Vec2 = this - (x,y, null)
  
  /** 
   * Divides this <code>Vec2</code> through the given <code>Vec2</code>
   * @return result as new vector 
   */
  final def /(x:Float, y:Float):Vec2 = this - (x,y, null)
  
  /** 
   * Subtracts the given <code>Vec2</code> from this <code>Vec2</code>
   * @return result
   */
  final def -(x:Float, y:Float, result:Vec2) = {
    val r = if(result == null) new Vec2 else result 
    r.x = this.x - x
    r.y = this.y - y
    r
  }
  
  /** 
   * Adds the given <code>Vec2</code> to this <code>Vec2</code>
   * @return result
   */
  final def +(x:Float, y:Float, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x + x
    r.y = this.y + y
    r
  }
  
  /** 
   * Multiplies the given <code>Vec2</code> with this <code>Vec2</code>
   * @return result
   */
  final def *(x:Float, y:Float, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x * x
    r.y = this.y * y
    r
  }
  
  /** 
   * Divides this <code>Vec3</code> through the given <code>Vec2</code>
   * @return result 
   */
  final def /(x:Float, y:Float, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x / x
    r.y = this.y / y
    r
  }
  
  // -- Vec2 Operations --------------------------------------------------------
  /** 
   * Subtracts the given <code>Vec2</code> from this <code>Vec2</code>
   * @return result as new vector 
   */
  final def -(v:Vec2):Vec2 = this - (v, null)
  
  /** 
   * Adds the given <code>Vec2</code> to this <code>Vec2</code>
   * @return result as new vector 
   */
  final def +(v:Vec2):Vec2 = this - (v, null)
  
  /** 
   * Multiplies the given <code>Vec2</code> with this <code>Vec2</code>
   * @return result as new vector 
   */
  final def *(v:Vec2):Vec2 = this - (v, null)
  
  /** 
   * Divides this <code>Vec2</code> through the given <code>Vec2</code>
   * @return result as new vector 
   */
  final def /(v:Vec2):Vec2 = this - (v, null)
  
  /** 
   * Subtracts the given <code>Vec2</code> from this <code>Vec2</code>
   * @return result
   */
  final def -(v:Vec2, result:Vec2) = {
    val r = if(result == null) new Vec2 else result 
    r.x = this.x - v.x
    r.y = this.y - v.y
    r
  }
  
  /** 
   * Adds the given <code>Vec2</code> to this <code>Vec2</code>
   * @return result
   */
  final def +(v:Vec2, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x + v.x
    r.y = this.y + v.y
    r
  }
  
  /** 
   * Multiplies the given <code>Vec2</code> with this <code>Vec2</code>
   * @return result
   */
  final def *(v:Vec2, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x * v.x
    r.y = this.y * v.y
    r
  }
  
  /** 
   * Divides this <code>Vec3</code> through the given <code>Vec2</code>
   * @return result 
   */
  final def /(v:Vec2, result:Vec2) = {
    val r = if(result == null) new Vec2 else result
    r.x = this.x / v.x
    r.y = this.y / v.y
    r
  }
  
  // -- Local Operations -------------------------------------------------------
  final def +=(s:Float) = { x+=s; y+=s; this }
  final def -=(s:Float) = { x-=s; y-=s; this }
  final def *=(s:Float) = { x*=s; y*=s; this }
  final def /=(s:Float) = { x/=s; y/=s; this }
  
  final def +=(x:Float, y:Float) = { this.x+=x; this.y+=y; this }
  final def -=(x:Float, y:Float) = { this.x-=x; this.y-=y; this }
  final def *=(x:Float, y:Float) = { this.x*=x; this.y*=y; this }
  final def /=(x:Float, y:Float) = { this.x/=x; this.y/=y; this }
  
  final def +=(v:Vec2) = { x+=v.x; y+=v.y; this }
  final def -=(v:Vec2) = { x-=v.x; y-=v.y; this }
  final def *=(v:Vec2) = { x*=v.x; y*=v.y; this }
  final def /=(v:Vec2) = { x/=v.x; y/=v.y; this }
  
  final def lengthSquared = x * x + y * y
  
  final def distance(v:Vec2) = Math.sqrt(distanceSquared(v.x, v.y)).asInstanceOf[Float]
  final def distance(x:Float, y:Float):Float = Math.sqrt(distanceSquared(x,y)).asInstanceOf[Float]
  
  final def distanceSquared(v:Vec2):Float = distanceSquared(v.x, v.y)
  final def distanceSquared(x:Float, y:Float) = {
    val dx = this.x - x
    val dy = this.y - y
    dx * dx + dy * dy
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
   * interpolates towards the given target Vector
   * @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
   */
  final def slerp(target:Vec2, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
    this
  }
  
  /** checks wether one or several components of this vector are Not a Number or Infinite */
  final def isNaNOrInfinite = { 
    import java.lang.Float
    var error = 0
    if (Float.isInfinite(x)) error -= 1
    if (Float.isInfinite(y)) error -= 1
    if (Float.isNaN(x)) error -= 1
    if (Float.isNaN(y)) error -= 1
    error == 0
  }
  
  final def elements = new Iterator[Float] {
    var i=0
    def next = {
      i match {
        case 0 => x
        case 1 => y
        case _ => 0
      }
    }
    def hasNext = i==2
  }
  
  // helpers
  override def toString = "Vec2["+ toLabel +"]"
  
  def toLabel = "X"+ x +" Y"+ y
}
