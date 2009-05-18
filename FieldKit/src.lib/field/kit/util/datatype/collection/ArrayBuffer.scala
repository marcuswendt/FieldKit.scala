/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 18, 2009 */
package field.kit.util.datatype.collection

/**
 * a performance and memory usage optimized version of the regular arraybuffer
 * thought to be used as a drop-in replacement, simply:
 * <code>import field.kit.util.datatype.collection.ArrayBuffer</code>
 * 
 * the general idea is to avoid iterators completely
 */
class ArrayBuffer[A] extends scala.collection.mutable.ArrayBuffer[A] {
  override def apply(i:Int) = array(i).asInstanceOf[A]
  
  override def foreach(func: A => Unit) {
    var i=0
    while(i < size0) {
      func( array(i).asInstanceOf[A] )
      i += 1
    }
  }
  
  /**
   * scala's own indexOf as defined in Seq leaks
   */
  override def indexOf[B >: A](elem: B):Int = {
    var index = -1
    var i=0
    while(i < size0) {
      if(elem.equals(array(i))) {
        index = i
        i = size0
      }
      i += 1
    }
    index
  }
  
  override def find(p: A => Boolean):Option[A] = {
    var result:Option[A] = None
    var i=0
    while(i < size0) {
      val elem = array(i).asInstanceOf[A]
      if(p(elem)) {
        result = Some(elem)
        i = size0
      }
      i += 1
    }
    result
  }
}
