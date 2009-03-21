/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 07, 2009 */
package field.kit.math

/**
 * Base Class for all Vectors 
 */
abstract class Vector[T](val size:Int) extends Collection[T] {
}

/**
 * Base Class for all Float Vector Types
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
  }
}

/**
 * 2 Dimensional Float Vector
 */
object Vec2 {
  val ZERO = new Vec2(0, 0)
  val UNIT_X = new Vec2(1, 0)
  val UNIT_Y = new Vec2(0, 1)
  val UNIT_XY = new Vec2(1, 1)  
}

class Vec2(var x:Float, var y:Float) extends VecF(2) {
  def this() = this(0,0) 
  
  def set(x:Float, y:Float) = { this.x=x; this.y=y; this }
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
  
  def distance(v:Vec2) = Math.sqrt(distanceSquared(v)).asInstanceOf[Float]
  def distanceSquared(v:Vec2) = {
    val dx = x - v.x
    val dy = y - v.y
    dx * dx + dy * dy
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

/**
 * 3 Dimensional Float Vector
 */
object Vec3 {
  val ZERO = new Vec3(0, 0, 0)
  val UNIT_X = new Vec3(1, 0, 0)
  val UNIT_Y = new Vec3(0, 1, 0)
  val UNIT_Z = new Vec3(0, 0, 1)
  val UNIT_XYZ = new Vec3(1, 1, 1)
}

class Vec3(var x:Float, var y:Float, var z:Float) extends VecF(3) {
  def this() = this(0,0,0) 
  
  def set(x:Float, y:Float, z:Float) = { this.x=x; this.y=y; this.z=z }
  def zero = set(0,0,0)
  
  def +=(s:Float) = { x+=s; y+=s; z+=s; this }
  def -=(s:Float) = { x-=s; y-=s; z-=s; this }
  def *=(s:Float) = { x*=s; y*=s; z*=s; this }
  def /=(s:Float) = { x/=s; y/=s; z/=s; this }
  
  def +=(v:Vec3) = { x+=v.x; y+=v.y; z+=v.z; this }
  def -=(v:Vec3) = { x-=v.x; y-=v.y; z-=v.z; this }
  def *=(v:Vec3) = { x*=v.x; y*=v.y; z*=v.z; this }
  def /=(v:Vec3) = { x/=v.x; y/=v.y; z/=v.z; this }
  
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
  
  override def toString = "Vec3("+ x +","+ y +","+ z +")"
}