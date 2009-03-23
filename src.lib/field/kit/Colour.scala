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
  
  /** @return this colour as argb packed integer */
  def argb = {
    (((a * 255).asInstanceOf[Int] & 0xFF) << 24) | 
    (((r * 255).asInstanceOf[Int] & 0xFF) << 16) | 
    (((g * 255).asInstanceOf[Int] & 0xFF) << 8) | 
    (((b * 255).asInstanceOf[Int] & 0xFF))
  }
  
  /** @return this colour as rgba packed integer */
  def rgba = {
    (((r * 255).asInstanceOf[Int] & 0xFF) << 24) | 
    (((g * 255).asInstanceOf[Int] & 0xFF) << 16) | 
    (((b * 255).asInstanceOf[Int] & 0xFF) << 8) | 
    (((a * 255).asInstanceOf[Int] & 0xFF))
  }
  
  def toInt = argb
  
  override def toString = "Colour(r:"+ r +" g:"+ g +" b:"+ b +" a:"+ a +")"
}

object Colour {
  def apply() = new Colour(0f,0f,0f,1f)
  def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
  def apply(grey:Float) = new Colour(grey,grey,grey,1f)
  def apply(grey:Float, a:Float) = new Colour(grey,grey,grey,a)
  
  val BLACK = apply(0)
  val WHITE = apply(1)
}