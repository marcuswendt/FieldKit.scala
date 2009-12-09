/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 09, 2009 */
package field.kit.colour

import field.kit.math.Vec4

object HSVA {
  def apply() = new HSVA()
}

/**
 * Utility class representing a Colour in Hue, Saturation, Value + Alpha form
 */
class HSVA(_h:Float, _s:Float, _v:Float, _a:Float) extends Vec4(_h, _s, _v, _a) {
  def this() = this(0,0,0,0)
  
  def h = x
  def h_=(f:Float) = x = f
  
  def s = y
  def s_=(f:Float) = y = f
  
  def v = z
  def v_=(f:Float) = z = f
  
  def a = w
  def a_=(f:Float) = w = f
  
  override def toString = "HSVA(H"+ h +" S"+ s +" V"+ v +" A"+ a +")"
}
