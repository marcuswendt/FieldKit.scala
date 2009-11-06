/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 23, 2009 */
package field.kit.gl.render

import field.kit.Logger

object GLObject {
  val UNDEFINED = -1
}

/**
 * Base class for all types of OpenGL objects.
 * @author Marcus Wendt
 */
abstract class GLObject extends Renderable with Logger {
  var id:Int = GLObject.UNDEFINED
  
  def create
  def destroy
  def bind
  def unbind
  
  final def render {}
}