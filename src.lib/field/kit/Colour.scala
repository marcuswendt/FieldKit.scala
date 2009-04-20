/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit // note NOT .colour

class Colour(
  var r:Float,
  var g:Float,
  var b:Float,
  var a:Float
) extends Logger {
  import field.kit.math._
  
  def this() = this(0f,0f,0f,1f)
  def this(r:Float,g:Float,b:Float) = this(r,g,b,1f)
  def this(r:Int,g:Int,b:Int) = this(r/255f,g/255f,b/255f,1f)
  def this(r:Int,g:Int,b:Int,a:Int) = this(r/255f,g/255f,b/255f,a/255f)
  
  def this(i:Int) = { this(); set(i) }
  def this(c:Colour) = { this(); set(c) }
  def this(grey:Float) = this(grey,grey,grey,1f)
  def this(grey:Float, a:Float) = this(grey,grey,grey,a)
  
  // -- setters ----------------------------------------------------------------
  def set(c:Colour) = { this.r = c.r; this.g=c.g; this.b=c.b; this.a=c.a; this }
  def set(grey:Float) = {this.r=grey; this.g=grey; this.b=grey; this}
  def set(grey:Float,a:Float) = {this.r=grey; this.g=grey; this.b=grey; this.a=a; this}
  def set(r:Float,g:Float,b:Float) = { this.r = r; this.g=g; this.b=b; this }
  def set(r:Float,g:Float,b:Float,a:Float) = { this.r = r; this.g=g; this.b=b; this.a=a; this }
  
  def set(i:Int):Colour = {
    // i is within [0, 256] range -> i is a greyscale value
    if(i > 0 && i < 256) { 
      set(i/ 256f)
    // i is a hex-value
    } else {
      fromRGB(i)
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
  
  // -- helpers ----------------------------------------------------------------
  def toInt = toARGB
  override def toString = "Colour("+ r +", "+ g +", "+ b +", "+ a +")"
}

object Colour {
//  def apply() = new Colour(0f,0f,0f,1f)
//  def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
//  def apply(grey:Float) = new Colour(grey,grey,grey,1f)
//  def apply(grey:Float, a:Float) = new Colour(grey,grey,grey,a)
//  
  val BLACK = new Colour(0)
  val WHITE = new Colour(1)
  
  val RED = new Colour(1f, 0f, 0f)
  val GREEN = new Colour(0f, 1f, 0f)
  val BLUE = new Colour(0f, 0f, 1f)
}