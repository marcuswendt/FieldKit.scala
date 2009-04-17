/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 07, 2009 */
package field.kit.math

// —————————————————————————————————————————————————————————————————————————————
// Base classes
// —————————————————————————————————————————————————————————————————————————————
/**
 * Base class for all Vectors 
 */
abstract class Vector[T](val size:Int) extends Collection[T] {
}

/**
 * Base class for all Float Vector Types
 */
abstract class VecF(size:Int) extends Vector[Float](size) {
  def +=(s:Float)
  def -=(s:Float)
  def *=(s:Float)
  def /=(s:Float)
  
  def zero
  def negate = this *= -1
  
  def length = Math.sqrt(lengthSquared).asInstanceOf[Float]
  def lengthSquared:Float
  
  def normalize = {
    val l = length
    if(l != 0)
      this /= l
    else
      this /= 1
    this
  }
}


// —————————————————————————————————————————————————————————————————————————————
/**
 * 2 Dimensional Float Vector
 */
object Vec2 {
  val ZERO = new Vec2(0, 0)
  val UNIT_X = new Vec2(1, 0)
  val UNIT_Y = new Vec2(0, 1)
  val UNIT_XY = new Vec2(1, 1)
  
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

class Vec2(var x:Float, var y:Float) extends VecF(2) {
  import java.nio.FloatBuffer
  
  def this() = this(0,0)
  def this(v:Vec2) = this(v.x,v.y)
  
  def apply(x:Float, y:Float) = set(x,y)
  def apply(v:Vec2) = set(v)
  
  def set(x:Float, y:Float) = { this.x=x; this.y=y; this }
  def set(v:Vec2) = {this.x=v.x; this.y=v.y; this}
  def set(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    this.x = buffer get i
    this.y = buffer get i+1
    this
  }

  def put(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    buffer.put(i, x)
    buffer.put(i + 1, y)
    this    
  }
  def put(buffer:FloatBuffer) = {
    buffer put x
    buffer put y
    this    
  }
  
  def zero = set(0,0)
  
  def +=(s:Float) = { x+=s; y+=s; this }
  def -=(s:Float) = { x-=s; y-=s; this }
  def *=(s:Float) = { x*=s; y*=s; this }
  def /=(s:Float) = { x/=s; y/=s; this }
  
  def +=(v:Vec2) = { x+=v.x; y+=v.y; this }
  def -=(v:Vec2) = { x-=v.x; y-=v.y; this }
  def *=(v:Vec2) = { x*=v.x; y*=v.y; this }
  def /=(v:Vec2) = { x/=v.x; y/=v.y; this }
  
  def lengthSquared = x * x + y * y
  
  def distance(v:Vec2) = Math.sqrt(distanceSquared(v.x, v.y)).asInstanceOf[Float]
  def distance(x:Float, y:Float):Float = Math.sqrt(distanceSquared(x,y)).asInstanceOf[Float]
  
  def distanceSquared(v:Vec2):Float = distanceSquared(v.x, v.y)
  def distanceSquared(x:Float, y:Float) = {
    val dx = this.x - x
    val dy = this.y - y
    dx * dx + dy * dy
  }
  
  def perpendiculate = {
    val tmp = x
    this.x = y
    this.y = -tmp
    this
  }
  
  /** checks wether one or several components of this vector are Not a Number or Infinite */
  def isNaNOrInfinite = { 
    import java.lang.Float
    var error = 0
    if (Float.isInfinite(x)) error -= 1
    if (Float.isInfinite(y)) error -= 1
    if (Float.isNaN(x)) error -= 1
    if (Float.isNaN(y)) error -= 1
    error == 0
  }
  
  def elements = new Iterator[Float] {
    var i=0
    def next = {
      i match {
        case 0 => x
        case 1 => y
        case _ => 0
      }
    }
    def hasNext = i==size
  }
  
  override def toString = "Vec2("+ x +","+ y+")"
}


// —————————————————————————————————————————————————————————————————————————————
/**
 * 3 Dimensional Float Vector
 */
object Vec3 {
  val ZERO = new Vec3(0, 0, 0)
  val UNIT_X = new Vec3(1, 0, 0)
  val UNIT_Y = new Vec3(0, 1, 0)
  val UNIT_Z = new Vec3(0, 0, 1)
  val UNIT_XYZ = new Vec3(1, 1, 1)
  
  def apply() = new Vec3(0,0,0)
  def apply(x:Float, y:Float, z:Float) = new Vec3(x,y,z)
}

class Vec3(var x:Float, var y:Float, var z:Float) extends VecF(3) {
  import java.nio.FloatBuffer
  
  def this() = this(0,0,0)
  def this(v:Vec2) = this(v.x, v.y, 0)
  def this(v:Vec3) = this(v.x, v.y, v.z)
  
  def apply(x:Float, y:Float, z:Float) = set(x,y,z)
  def apply(v:Vec3) = set(v)
  
  def set(x:Float, y:Float, z:Float) = { this.x=x; this.y=y; this.z=z; this }
  def set(v:Vec3) = { this.x=v.x; this.y=v.y; this.z=v.z; this }
  
  def set(buffer:FloatBuffer, index:Int) = {
    val i = index * 3
    this.x = buffer get i
    this.y = buffer get i + 1
    this.z = buffer get i + 2
    this
  }

  def put(buffer:FloatBuffer, index:Int) = {
    val i = index * 2
    buffer put (i, x)
    buffer put (i+1, y)
    buffer put (i+2, z)
    this    
  }
  def put(buffer:FloatBuffer) = {
    buffer put x
    buffer put y
    buffer put z
    this    
  }
  
  def zero = set(0,0,0)
  
  def +=(s:Float) = { x+=s; y+=s; z+=s; this }
  def -=(s:Float) = { x-=s; y-=s; z-=s; this }
  def *=(s:Float) = { x*=s; y*=s; z*=s; this }
  def /=(s:Float) = { x/=s; y/=s; z/=s; this }
  
  def +=(v:Vec3) = { x+=v.x; y+=v.y; z+=v.z; this }
  def -=(v:Vec3) = { x-=v.x; y-=v.y; z-=v.z; this }
  def *=(v:Vec3) = { x*=v.x; y*=v.y; z*=v.z; this }
  def /=(v:Vec3) = { x/=v.x; y/=v.y; z/=v.z; this }
  
  def +=(x:Float, y:Float, z:Float) = { this.x+=x; this.y+=y; this.z+=z; this }
  def -=(x:Float, y:Float, z:Float) = { this.x-=x; this.y-=y; this.z-=z; this }
  def *=(x:Float, y:Float, z:Float) = { this.x*=x; this.y*=y; this.z*=z; this }
  def /=(x:Float, y:Float, z:Float) = { this.x/=x; this.y/=y; this.z/=z; this }
  
  def dot(v:Vec3) = x * v.x + y * v.y + z * v.z
  
  def cross(v:Vec3) = {
    val tempx = (y * v.z) - (z * v.y)
    val tempy = (z * v.x) - (x * v.z)
    z = (x * v.y) - (y * v.x)
    x = tempx
    y = tempy
    this
  }

  def lengthSquared = x * x + y * y + z * z
  
  def distance(v:Vec3) = Math.sqrt(distanceSquared(v)).asInstanceOf[Float]
  def distanceSquared(v:Vec3) = {
    val dx = x - v.x
    val dy = y - v.y
    val dz = z - v.z
    dx * dx + dy * dy + dz * dz
  }

  /** <code>angleBetween</code> returns (in radians) the angle between two vectors.
   *  It is assumed that both this vector and the given vector are unit vectors (iow, normalized). */
  def angleBetween(v:Vec3) {
    val dotProduct = dot(v)
    Math.acos(dotProduct).asInstanceOf[Float]
  }

  /** makes sure this vector does not exceed a certain length */
  def clamp(max:Float) = {
    val l = length
    if(l > max) {
      this /= l
      this *= max
    }
    this
  }
  
  /** checks wether one or several components of this vector are Not a Number or Infinite */
  def isNaNOrInfinite = { 
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
  
  def elements = new Iterator[Float] {
    var i=0
    def next = {
      i match {
        case 0 => x
        case 1 => y
        case 2 => z
        case _ => 0
      }
    }
    def hasNext = i==size
  }
  
  override def toString = "Vec3("+ x +", "+ y +", "+ z +")"
}