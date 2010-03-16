/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.util.datatype.graph.Branch

/** basic scene-graph element that contains a group of spatials **/
class Group(name:String) extends Spatial(name) with Branch[Spatial] {
  def draw = {
    // children foreach(_.render)
    var i = 0
    while(i < size) {
      children(i).render
      i += 1
    }
  }
}