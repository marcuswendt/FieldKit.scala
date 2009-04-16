/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Drawable
import field.kit.structure.graph.Branch

/** basic scene-graph element that contains a group of spatials **/
class Group(name:String) extends Branch[Spatial](name) with Drawable {
  def draw = children foreach(_.render)
}