/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.Renderable

/** 
 * base class for all render states
 * @author Marcus Wendt
 */
abstract class RenderState extends Renderable with field.kit.Logger {
  var isEnabled = true
  def enable
  def disable
  def destroy
  final override def render {}
}
