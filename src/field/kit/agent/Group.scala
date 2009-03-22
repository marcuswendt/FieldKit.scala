/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 22, 2009 */
package field.kit.agent

/** defines a group of behaviours */
trait Group {
  import scala.collection.mutable.ArrayBuffer
  
  // TODO how do we set priorities here, simply by order of adding/ removing?
  // is ArrayBuffers positioning reliable?
  var behaviours = new ArrayBuffer[Behaviour]
  def +=(b:Behaviour) = behaviours += b
  def -=(b:Behaviour) = behaviours -= b
  
  /** executes all behaviours in the list as long they return true */
  def update(c:Context, dt:Float) {
    var continue = true
    val i = behaviours.elements
    while(i.hasNext && continue) {
      continue = i.next.apply(c, dt)
    }
  }
}
