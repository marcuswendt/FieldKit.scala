/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.Logger

/** a single behaviour 
 * TODO evtl. add a verify/init method where the behaviour can check wether
 * its context is in the right shape
 */
//abstract class Behaviour(var name:String) extends Group with Logger {
abstract class Behaviour(name:String) extends Context(name) {
  import scala.reflect.Manifest
  
  logName = name +"Behaviour"
  
//  /** the current context this behaviour acts on */
//  var c:Context = null
  
  /** the delta time since the last call */
  var dt:Float = 0f
  
  override def update(parent:Context, dt:Float):Boolean = {
    // check if we're still operating in the same context
    if(this.parent != parent) {
      this.parent = parent
      switch
    }
    
    this.dt = dt
    
    // when this behaviour is just a single action -> execute it and feedback
    if(behaviours.size == 0) {
      apply
    
    // when this behaviour has subactions -> execute subactions when this 
    // behaviour returns true
    } else {
      if(apply) super.update(parent, dt)
      true
    }
  }
  
  // overrideables
  /** called when the behaviour context is switched */
  def switch {}
  
  def apply:Boolean
  
//  // tree-node accessors
//  def get[T](name:String)(implicit m:Manifest[T]):T = c.get(name)
//  
//  def get[T](name:String, default:T)(implicit m:Manifest[T]):T = c.get(name, default)
//  
//  def set[T](name:String, value:T)(implicit m:Manifest[T]) = c.set(name, value)
//  
//  def parent = c.parent
  
  // helpers
  override def toString = "Behaviour("+ name +")"
}

/*
/** companion object to behaviour */
object Behaviour {
  def apply(name:String, body: () => Boolean) = {
    val b = new Behaviour(name) 
    b.apply = body
    b
  }
}
*/