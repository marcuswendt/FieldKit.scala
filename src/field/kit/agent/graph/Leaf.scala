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
class Leaf[T](parent:Branch, name:String, private var value:T)(implicit val clazz: Manifest[T]) 
extends Node(parent, name) {
  
  def apply():T = value
  
  // TODO consider implementing a subscriber-event mechanism
  def update(value:T):Leaf[T] = {
    this.value = value
    this
  }
    
  override def toString = "Leaf("+name+") => "+ value +" type: "+ clazz
}