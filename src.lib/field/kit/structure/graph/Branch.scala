/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.structure.graph

/** 
 * a special node that has a number of children
 */
class Branch[T <: Node](name:String) extends Node(name) {
  import scala.collection.mutable.ArrayBuffer
  var children = new ArrayBuffer[T]
}