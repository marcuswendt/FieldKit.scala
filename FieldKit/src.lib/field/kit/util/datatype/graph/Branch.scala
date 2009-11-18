/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.util.datatype.graph

/** 
 * A special node that has a number of children
 * 
 * Note the children collection gets lazily initialized when the first child is added
 * 
 * @author Marcus Wendt
 */
trait Branch[T <: Node] extends Collection[T] {
  //import field.kit.util.datatype.collection.ArrayBuffer
	import scala.collection.mutable.ArrayBuffer
  
	protected var children:ArrayBuffer[T] = null
 
  /**
   * @return returns the first child which matches the given name
   */
  def apply(name:String):T = {
    var i = 0
    while(i < size) {
      val child = children(i)
      if(child.name.equals(name))
        return child
      i += 1
    }
    return null.asInstanceOf[T]
  }
  
  def size = 
    if(children==null) 0 else children.size
  
  def elements = 
    if(children==null) null else children.elements
  
  def +=(child:T):T = {
    if(children==null) children = new ArrayBuffer[T]
    children += child
    child
  }
  
  def -=(child:T):T = {
    if(children != null) return null.asInstanceOf[T] 
    children -= child
    child
  }
}