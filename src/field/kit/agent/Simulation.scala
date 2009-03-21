/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent

import field.kit.agent.graph._

/** Main container structure of the simulation */
class Simulation extends Root {
  name = "Simulation"
  
//  import scala.collection.mutable.ArrayBuffer
//  val agents = new ArrayBuffer[Agent]
  
  var agents:Branch = this += "agents"
  
  import field.kit.agent.space._
  var space = new Space
  
  def +=(a:Agent) = {
    a.parent(this)
    agents += a
    a
  }
}




