/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.Logger

/** a single behaviour */
abstract class Behaviour(name:String) extends Context(name) {
  import scala.reflect.Manifest
  import field.kit.agent.graph.Branch
  
  logName = name +"Behaviour"
  
  var initialized = false
  
  /** the current branch this behaviour acts on */
  var current:Branch = null
  
  override def attach(parent:Branch) {
    this.current = parent
    this.parent = parent.parent
  }
  
  /** the delta time since the last call */
  var dt:Float = 0f
  
  override def update(dt:Float):Boolean = {
    if(!initialized) { 
      init
      initialized = true
    }
    
    this.dt = dt
    
    // when this behaviour is just a single action -> execute it and feedback
    if(behaviours.size == 0) {
      apply
    
    // when this behaviour has subactions -> execute subactions when this 
    // behaviour returns true
    } else {
      if(apply) super.update(dt)
      true
    }
  }
  
  // overrideables
  def init {}  
  def apply:Boolean
  
  // helpers
  override def toString = "Behaviour("+ name +")"
}