/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Renderable

/** a node is a basic scene graph element that can have children **/
class Node[T <: Renderable](name:String) extends Spatial(name) {
  import scala.collection.mutable.ArrayBuffer
  var children = new ArrayBuffer[T]()
  
  def render {
    if(isVisible) {
      renderPre
      children foreach(_.render)
      renderPost
    }
  }
  
  def +=(child:T) = children += child
  def -=(child:T) = children -= child
}
