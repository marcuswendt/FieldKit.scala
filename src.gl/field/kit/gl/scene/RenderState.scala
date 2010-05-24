/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
