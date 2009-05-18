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
 */
class ArrayBuffer[A] extends scala.collection.mutable.ArrayBuffer[A] {
  override def apply(i:Int) = array(i).asInstanceOf[A]
}
