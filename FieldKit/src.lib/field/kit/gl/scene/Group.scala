/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Drawable
import field.kit.util.datatype.graph.Branch

/** basic scene-graph element that contains a group of spatials **/
class Group(name:String) extends Spatial(name) with Branch[Spatial] {
  def draw = {
    // children foreach(_.render)
    
    var i = 0
    while(i < children.size) {
      children(i).render
      i += 1
    }
  }
}