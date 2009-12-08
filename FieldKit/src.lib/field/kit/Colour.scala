/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 18, 2009 */
package field.kit

import field.kit.math._

/**
 * Companion object to class <code>Colour</code>
 */
object Colour {
  def apply() = new Colour(1f,1f,1f,1f)
  def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
  def apply(grey:Float) = new Colour(grey,grey,grey,1f)
  def apply(grey:Float, alpha:Float) = new Colour(grey,grey,grey,alpha)
  
  val BLACK = new Colour(0f)
  val WHITE = new Colour(1f)
  
  val RED = new Colour(1f, 0f, 0f)
  val GREEN = new Colour(0f, 1f, 0f)
  val BLUE = new Colour(0f, 0f, 1f)
  
  val YELLOW = new Colour(1f, 1f, 0f)
  val CYAN = new Colour(0f, 1f, 1f)
  val MAGENTA = new Colour(1f, 0f, 1f)
}


/**
 * A versatile RGBA Colour utility, used as datatype and for conversion 
 * @author Marcus Wendt
 */
class Colour(_r:Float, _g:Float, _b:Float, _a:Float) extends Vec4(_r,_g,_b,_a) with Logger {  
  import math._
  import math.Common._
  
  def this() = this(0f,0f,0f,1f)
  def this(r:Float,g:Float,b:Float) = this(r,g,b,1f)
  def this(r:Int,g:Int,b:Int) = this(r/255f,g/255f,b/255f,1f)
  def this(r:Int,g:Int,b:Int,a:Int) = this(r/255f,g/255f,b/255f,a/255f)
  
  def this(i:Int) = { this(); :=(i) }
  def this(c:Colour) = { this(); :=(c) }
  def this(s:String) = { this(); :=(s) }
  def this(grey:Float) = this(grey,grey,grey,1f)
  def this(grey:Float, a:Float) = this(grey,grey,grey,a)
  
  // -- RGBA Accessors ---------------------------------------------------------
  def r = x
  def r_=(s:Float) = x = s
  
  def g = y
  def g_=(s:Float) = y = s
  
  def b = z
  def b_=(s:Float) = z = s
  
  def a = w
  def a_=(s:Float) = w = s
  
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

  /** @return this colour as rgb packed integer */ 
  def toRGB = {
    (((r * 255).asInstanceOf[Int] & 0xFF) << 16) | 
    (((g * 255).asInstanceOf[Int] & 0xFF) << 8) | 
    (((b * 255).asInstanceOf[Int] & 0xFF))
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
  
  def luminance = r * 0.299f + g * 0.587f + b * 0.114f
  
  // -- Colour Operations ------------------------------------------------------  
  def randomize = {
    r = random
    g = random
    b = random
    a = random
    this
  }
  
  def inverse {
    this.r = 1f - r
    this.g = 1f - g
    this.b = 1f - b
  }
  
  def inverseWithAlpha {
    this.r = 1f - r
    this.g = 1f - g
    this.b = 1f - b
    this.a = 1f - a
  }
  
  // -- Helpers ----------------------------------------------------------------
  def toInt = toARGB
  override def toString = "Colour("+ toLabel +")"
  def toLabel = "R"+ r +" G"+ g +" B"+ b +" A"+ a
}