/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.agent.graph.Branch

/** the simulated character is just another context wrapping several subcontexts */
class Agent(name:String) extends Branch(name) {
  logName = "Agent("+name+")"
  
  def update(dt:Float) {
    foreach { n => 
    	if(n.isInstanceOf[Context]) 
    		n.asInstanceOf[Context].update(dt)
    }
  }
  
  override def toString = "Agent("+ name +")"
}