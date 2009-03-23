/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.agent.graph.Branch

/** The simulated character that actually just is a parent context, wrapping a few subcontexts */
class Agent(name:String) extends Context(name) {
  logName = "Agent("+name+")"
  
  def update(dt:Float) {
    foreach { n => 
    	if(n.isInstanceOf[Context]) 
    		n.asInstanceOf[Context].update(this, dt)
    }
  }
  
  override def update(c:Context, dt:Float):Boolean = { 
    fatal("Not supposed to call this method")
    false
  }
  
  override def toString = "Agent("+ name +")"
}