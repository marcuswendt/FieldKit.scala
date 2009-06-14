/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent

import field.kit.util.datatype.graph._ 

/** 
 * companion object to class <code>Neuron</code> 
 * @author Marcus Wendt
 */
object Neuron {
  /** creates an anonymous <code>Neuron</code> with the given logic block */
  def apply(logic: => Boolean):Neuron = apply("anonymous", logic)
  
  /** creates a named <code>Neuron</code> with the given logic block */
  def apply(name:String, logic: => Boolean) = {
    new Neuron(name) {
      def apply = logic
    }
  }
}

/**
 * The <code>Neuron</code> is the basic logic building block of the agent system.
 * @author Marcus Wendt
 */
abstract class Neuron(name:String) 
extends Node(name) 
with Branch[Neuron] {
  
  // temporary variables, to be used by apply only!
  protected var agent:Agent = _
  protected var dt:Float = _
  
  /** updates this neuron and eventually all its children */
  def update(a:Agent, dt:Float):Unit = {
    this.agent = agent
    this.dt = dt
    
    if(apply) {
      var i = 0
      while(i < children.size) {
        children(i).update(a, dt)
        i += 1
      }
    }
  }
  
  /** applies this neuron to the given agent for one timestep */
  def apply:Boolean
  
  /** adds an anonymous child <code>Neuron</code>*/
  def +=(logic: => Boolean):Neuron = this += Neuron(logic)

  /** adds a named child <code>Neuron</code>*/
  def +=(name:String, logic: => Boolean):Neuron = this += Neuron(name, logic)
}