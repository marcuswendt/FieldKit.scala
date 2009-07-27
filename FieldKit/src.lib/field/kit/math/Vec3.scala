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
  
  def apply() = new Vec3(0,0,0)
  def apply(x:Float, y:Float, z:Float) = new Vec3(x,y,z)
}

/**
 * 3 Dimensional Float Vector
 * @author Marcus Wendt
 */
class Vec3(var x:Float, var y:Float, var z:Float) extends VecF(3) {
  import java.nio.FloatBuffer
  
  def this() = this(0,0,0)
  def this(v:Vec2) = this(v.x, v.y, 0)
  def this(v:Vec3) = this(v.x, v.y, v.z)
  def this(s:String) = { this(); set(s); this }
  
  def update(i:Int, value:Float) = 
    i match {
      case 0 => this.x = value
      case 1 => this.y = value
      case 2 => this.y = value
    }
  
  def apply(x:Float, y:Float, z:Float) = set(x,y,z)
  
  def apply(v:Vec3) = set(v)
  
  /** sets xyz to a single scalar */
  def set(s:Float) = { this.x=s; this.y=s; this.z=s; this }
  
  /** sets the xyz components individually */
  def set(x:Float, y:Float, z:Float) = { this.x=x; this.y=y; this.z=z; this }
  
  /** sets the xyz components to the given vectors xyz components */
  def set(v:Vec3) = { this.x=v.x; this.y=v.y; this.z=v.z; this }
  
  /** sets the xyz components to the data from a given buffer at a given index */
  def set(buffer:FloatBuffer, index:Int) = {
    val i = index * 3
    this.x = buffer get i
    this.y = buffer get i + 1
    this.z = buffer get i + 2
    this
  }
  
  /** will parse the given string and attempts to set the xyz components accordingly */
  def set(s:String) = {
    if(s != null) {
      val iter = FMath.DECIMAL findAllIn s
      val list = iter.toList
      
      var index = 0
      def next = { 
        var f = FMath.abs(list(index).toFloat)
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
  
//  TODO implement non-local operations 
//  def -(s:Float) = {
//    
//  }
  
  def +=(s:Float) = { x+=s; y+=s; z+=s; this }
  def -=(s:Float) = { x-=s; y-=s; z-=s; this }
  def *=(s:Float) = { x*=s; y*=s; z*=s; this }
  def /=(s:Float) = { x/=s; y/=s; z/=s; this }
  
  def +=(x:Float, y:Float, z:Float) = { this.x+=x; this.y+=y; this.z+=z; this }
  def -=(x:Float, y:Float, z:Float) = { this.x-=x; this.y-=y; this.z-=z; this }
  def *=(x:Float, y:Float, z:Float) = { this.x*=x; this.y*=y; this.z*=z; this }
  def /=(x:Float, y:Float, z:Float) = { this.x/=x; this.y/=y; this.z/=z; this }
  
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

  /** makes sure this vector does not exceed a certain length */
  def clamp(max:Float) = {
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
  def slerp(target:Vec3, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
    this.z = z * (1 - delta) + target.z * delta
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
  
  override def toString = "Vec3["+ toLabel +"]"
  def toLabel = "X"+ x +" Y"+ y +" Z"+ z
}