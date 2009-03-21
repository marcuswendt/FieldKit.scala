/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent

import field.kit.agent.graph.Node
import field.kit.Logger

/** defines a singule action-block within the context-tree */
abstract class Behaviour(name:String) extends Node(name) with Logger {
  logName = name +"Behaviour"
  
  def apply(c:Context, dt:Float):Unit = if(trigger(c)) update(c,dt)
  def trigger(c:Context):Boolean
  def update(c:Context, dt:Float)
  
  override def toString = name +"Behaviour"
}