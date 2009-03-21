/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.agent.graph.Node

/** The simulated character that actually just is a parent context, wrapping a few subcontexts */
class Agent(name:String) extends Context(name) {
  logName = "Agent("+name+")"
  
  import scala.collection.mutable.ArrayBuffer
  var contexts = new ArrayBuffer[Context]
  
  override def update(dt:Float) = {
    super.update(dt)
    contexts.foreach(_.update(dt))
  }
  
  def +=(c:Context):Context = {
    super.+=(c)
    contexts += c
    c
  }
  
  override def toString = "Agent("+ name +")"
}