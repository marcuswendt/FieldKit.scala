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
class Context(name:String) extends Branch(name) {
  logName = "Context("+ name +")"
    
  import scala.collection.mutable.ArrayBuffer
  // TODO how do we set priorities here, simply by order of adding/ removing?
  // is ArrayBuffers positioning reliable?
  var behaviours = new ArrayBuffer[Behaviour]
  
  def +=(b:Behaviour):Behaviour = {
    super.+=(b)
    behaviours += b
    b
  }
  
  def +=(name:String, 
         t: (Context) => Boolean, 
         u: (Context, Float) => Unit):Behaviour = {
    this += new Behaviour(name) {
      def trigger(c:Context):Boolean = t(c)
      def update(c:Context, dt:Float):Unit = u(c,dt)
    }
  }
  
  def update(dt:Float) = behaviours.foreach(_.apply(this, dt))
  
  def simulation = root.asInstanceOf[Simulation]
  
  override def toString = "Context("+ name +")"
}
