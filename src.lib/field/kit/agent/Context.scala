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
class Context(name:String) extends Branch(name) with Group {
    
  /* the parent context of the current update cycle */
  var parent:Context = null
  
  override def update(c:Context, dt:Float) = {
    this.parent = c
    super.update(this, dt)
  }
  
  override def toString = "Context("+ name +")"
}
