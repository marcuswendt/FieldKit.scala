/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

abstract class Behaviour(name:String) extends Context(name) {
  import scala.reflect.Manifest
  import field.kit.agent.graph.Leaf
  
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
  
  //
  // Helpers
  //
  def local[T](name:String)(implicit m:Manifest[T]):Leaf[T] = {
    c.children find(_.name == name) match {
      case Some(l:Leaf[_]) => l.asInstanceOf[Leaf[T]]
      case _ => c += (name, default(m))
    }
  }
  
  /** returns the default value for a given object */
  def default[T](m:Manifest[T]):T = {
    val d = m.toString match {
      case "boolean" => true
      case "byte" => 0x0
      case "char" => ' '
      case "double" => 0.0
      case "float" => 0f
      case "int" => 0
      case "long" => 0L
      case "String" => ""
      case _ => null
    }
    d.asInstanceOf[T]
  }
  
  override def toString = name +"Behaviour"
}

/** companion object to behaviour */
object Behaviour {
  def apply(name:String, body: => Boolean) = {
    new Behaviour(name) {
      def apply() = body
    }
  }
}