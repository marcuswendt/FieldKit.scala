/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.colour

import field.kit.Logger

class Colour(
  var r:Float,
  var g:Float,
  var b:Float,
  var a:Float
) extends Logger {
}

object Color {
  def apply() = new Colour(0f,0f,0f,1f)
  def apply(r:Float,g:Float,b:Float) = new Colour(r,g,b,1f)
  def apply(grey:Float) = new Colour(grey,grey,grey,1f)
  def apply(grey:Float, a:Float) = new Colour(grey,grey,grey,a)
  
  val BLACK = apply(0)
  val WHITE = apply(1)
}