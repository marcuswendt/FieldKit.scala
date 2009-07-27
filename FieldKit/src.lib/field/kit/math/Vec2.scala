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

/**
 * 2 Dimensional Float Vector
 * @author Marcus Wendt
 */
class Vec2(var x:Float, var y:Float) extends VecF(2) {
  import java.nio.FloatBuffer
  
  def this() = this(0,0)
  def this(v:Vec2) = this(v.x,v.y)
  def this(s:String) = { this(); set(s); this }
  
  def update(i:Int, value:Float) = 
    i match {
      case 0 => this.x = value
      case 1 => this.y = value
    }
  
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
  
  def zero = set(0,0)
  
  def +=(s:Float) = { x+=s; y+=s; this }
  def -=(s:Float) = { x-=s; y-=s; this }
  def *=(s:Float) = { x*=s; y*=s; this }
  def /=(s:Float) = { x/=s; y/=s; this }
  
  def +=(x:Float, y:Float) = { this.x+=x; this.y+=y; this }
  def -=(x:Float, y:Float) = { this.x-=x; this.y-=y; this }
  def *=(x:Float, y:Float) = { this.x*=x; this.y*=y; this }
  def /=(x:Float, y:Float) = { this.x/=x; this.y/=y; this }
  
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
  
  /**
   * interpolates towards the given target Vector
   * @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
   */
  def slerp(target:Vec2, delta:Float) = {
    this.x = x * (1 - delta) + target.x * delta
    this.y = y * (1 - delta) + target.y * delta
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
  
  // helpers
  override def toString = "Vec2["+ toLabel +"]"
  
  def toLabel = "X"+ x +" Y"+ y
}
