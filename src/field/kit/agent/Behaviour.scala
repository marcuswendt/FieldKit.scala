/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.Logger

/* a single behaviour */
abstract class Behaviour(name:String) extends Group with Logger {
  import scala.reflect.Manifest
  
  logName = name +"Behaviour"
  
  /** the current context this behaviour acts on */
  var c:Context = null
  
  /** the delta time since the last call */
  var dt:Float = 0f
  
  def apply(c:Context, dt:Float):Boolean = {
    this.c = c
    this.dt = dt
    
    // when this behaviour is just a single action -> execute it and feedback
    if(behaviours.size == 0) {
      apply
    
    // when this behaviour has subactions -> execute subactions when this 
    // behaviour returns true
    } else {
      if(apply) update(c, dt)
      true
    }
  }
  
  def apply:Boolean
  
  def apply[T](name:String, default:T)(implicit m:Manifest[T]):T =
    c.apply(name, default).apply()
  
  def update[T](name:String, value:T)(implicit m:Manifest[T]) =
    c.apply(name, value).update(value)
  
  override def toString = name +"Behaviour"
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