/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit // note NOT .colour

/**
 * A versatile RGBA Colour utility, used as datatype and for conversion 
 * @author Marcus Wendt
 */
class Colour(
  var r:Float,
  var g:Float,
  var b:Float,
  var a:Float
) extends Logger {  
  import math._
  import math.FMath._
  
  def this() = this(0f,0f,0f,1f)
  def this(r:Float,g:Float,b:Float) = this(r,g,b,1f)
  def this(r:Int,g:Int,b:Int) = this(r/255f,g/255f,b/255f,1f)
  def this(r:Int,g:Int,b:Int,a:Int) = this(r/255f,g/255f,b/255f,a/255f)
  
  def this(i:Int) = { this(); :=(i) }
  def this(c:Colour) = { this(); :=(c) }
  def this(s:String) = { this(); :=(s) }
  def this(grey:Float) = this(grey,grey,grey,1f)
  def this(grey:Float, a:Float) = this(grey,grey,grey,a)
  
  // -- Setters ----------------------------------------------------------------
  /**
   * Sets this Colour components to the given Colour
   * @return itself
   */
  final def :=(c:Colour) = {
    if(c != null) {
      this.r = c.r
      this.g = c.g
      this.b = c.b
      this.a = c.a
    }
    this
  }
  
  /**
   * Sets all components of this Colour to the given Float
   * @return itself
   */
  final def :=(grey:Float) = {this.r=grey; this.g=grey; this.b=grey; this}
  
  /**
   * Sets the r,g,b components of this Colour to the given Float and the alpha seperately
   * @return itself
   */
  final def :=(grey:Float,a:Float) = {this.r=grey; this.g=grey; this.b=grey; this.a=a; this}
  
  /**
   * Sets only the rgb components of this Colour
   * @return itself
   */
  final def :=(r:Float,g:Float,b:Float) = { this.r = r; this.g=g; this.b=b; this }
  
  /**
   * Sets all components of this Colour individually
   * @return itself
   */
  final def :=(r:Float,g:Float,b:Float,a:Float) = { this.r = r; this.g=g; this.b=b; this.a=a; this }
  
  /**
   * Sets this Colour using an Integer which is interpreted as:
   * a) as single [0,256] grey value
   * b) as packed integer given as hex number
   * @return itself
   */ 
  final def :=(i:Int):Colour = {
    // i is within [0, 256] range -> i is a greyscale value
    if(i > 0 && i < 256) { 
      :=(i/ 256f)
    // i is a hex-value
    } else {
      fromRGB(i)
    }
    this
  }
  
  /**
   * Attempts to interpret this String to set this Colours components
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
        // normalize values above 1.0
        if(f > 1.0f) f /= 255f
        f
      }
      
      list.size match {
        // set all 
        case 1 =>
          val f = next
          this.r = f
          this.g = f
          this.b = f
          this.a = f
        // set rgb and alpha
        case 2 =>
          val f = next
          this.r = f
          this.g = f
          this.b = f
          this.a = next
        // set the rgb values
        case 3 =>
          this.r = next 
          this.g = next
          this.b = next
        // set rgba independently
        case 4 =>
          this.r = next 
          this.g = next
          this.b = next
          this.a = next
        case _ => throw new Exception("Couldnt parse String '"+ s +"' (matches:"+ list.size +")")
      }
    }
    this
  }
  
  // -- to and from packed integer conversion ----------------------------------
  /** @return this colour as argb packed integer */
  def toARGB = {
    (((a * 255).asInstanceOf[Int] & 0xFF) << 24) | 
    (((r * 255).asInstanceOf[Int] & 0xFF) << 16) | 
    (((g * 255).asInstanceOf[Int] & 0xFF) << 8) | 
    (((b * 255).asInstanceOf[Int] & 0xFF))
  }
  
  /** @return this colour as rgba packed integer */
  def toRGBA = {
    (((r * 255).asInstanceOf[Int] & 0xFF) << 24) | 
    (((g * 255).asInstanceOf[Int] & 0xFF) << 16) | 
    (((b * 255).asInstanceOf[Int] & 0xFF) << 8) | 
    (((a * 255).asInstanceOf[Int] & 0xFF))
  }
 
  def fromRGB(i:Int) {
    r = (i >> 16 & 0xFF) / 255f
    g = (i >> 8 & 0xFF) / 255f
    b = (i & 0xFF) / 255f
  }
  
  def fromRGBA(i:Int) {
    r = (i >> 24 & 0xFF) / 255f
    g = (i >> 16 & 0xFF) / 255f
    b = (i >> 8 & 0xFF) / 255f
    a = (i & 0xFF) / 255f
  }
  
  def fromARGB(i:Int) {
    a = (i >> 24 & 0xFF) / 255f
    r = (i >> 16 & 0xFF) / 255f
    g = (i >> 8 & 0xFF) / 255f
    b = (i & 0xFF) / 255f
  }
  
  def randomize = {
    r = Random()
    g = Random()
    b = Random()
    a = Random()
    this
  }
  
  // -- operations -------------------------------------------------------------
  def inverse {
    this.r = 1f - r
    this.g = 1f - g
    this.b = 1f - b
  }
  
  def inverseAll {
    this.r = 1f - r
    this.g = 1f - g
    this.b = 1f - b
    this.a = 1f - a
  }
  
  
  // -- helpers ----------------------------------------------------------------
  def toInt = toARGB
  override def toString = "Colour("+ toLabel +")"
  def toLabel = "R"+ r +" G"+ g +" B"+ b +" A"+ a
}

object Colour {
//  def apply() = new Colour(0f,0f,0f,1f)
//  def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
//  def apply(grey:Float) = new Colour(grey,grey,grey,1f)
//  def apply(grey:Float, a:Float) = new Colour(grey,grey,grey,a)
//  
  val BLACK = new Colour(0f)
  val WHITE = new Colour(1f)
  
  val RED = new Colour(1f, 0f, 0f)
  val GREEN = new Colour(0f, 1f, 0f)
  val BLUE = new Colour(0f, 0f, 1f)
}