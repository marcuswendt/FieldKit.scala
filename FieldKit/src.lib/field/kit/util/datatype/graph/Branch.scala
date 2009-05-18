/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.util.datatype.graph

/** 
 * a special node that has a number of children
 * @author Marcus Wendt
 */
trait Branch[T <: Node] extends Collection[T] {
  import field.kit.util.datatype.collection.ArrayBuffer
  var children = new ArrayBuffer[T]
  def size = children.size
  def elements = children.elements
  
  def +=(child:T) = children += child
  def -=(child:T) = children -= child
}