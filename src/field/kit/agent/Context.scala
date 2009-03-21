/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.agent.graph._

/** Holds information about behaviours, subcontexts and memory of a certain part of the agents brain */
class Context(name:String) extends Branch(name) {
  logName = name +"Context"
  
  /** constructor */
  def this(parent:Branch, name:String) {
    this(name)
    this.parent(parent)
  }
  
  import scala.collection.mutable.ArrayBuffer
  // TODO how do we set priorities here, simply by order of adding/ removing?
  // is ArrayBuffers positioning reliable?
  var behaviours = new ArrayBuffer[Behaviour]

  def update(dt:Float) {}
  
  def simulation = root.asInstanceOf[Simulation]
  
  override def toString = "Context("+name+")"
}
