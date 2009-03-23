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
class Simulation(name:String) extends Root(name) {
  import field.kit.agent.space._
  var space = new Space
  var agents:Branch = this += new Branch("agents")  
  def update(dt:Float) = agents.foreach(_.asInstanceOf[Agent].update(dt))
}




