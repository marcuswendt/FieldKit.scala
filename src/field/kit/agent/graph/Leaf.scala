/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent.graph

import scala.reflect.Manifest

/**
 * a special node that stores a value 
 */
class Leaf[T](parent:Node, name:String, var value:T)(implicit val clazz: Manifest[T]) 
extends Node(parent, name) {
  
  def apply():T = value
  
  // TODO consider implementing a subscriber-event mechanism
  def update(value:T) = this.value = value
    
  override def toString = "Leaf("+name+") => "+ value +" type: "+ clazz
}