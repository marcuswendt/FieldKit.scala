/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent

/** companion object to class <code>Simulation</code> */
object Simulation {
  /** tells the simulation to update all its agents */
  case class UpdateDynamic()
	
  /** tells an agent to update its neuron network for one frame */
  case class Update(dt:Float)
	
  /** returned from the agent when it finished the update */
  case class Finished()  
}

import actors.Actor
import field.kit.util.datatype.graph._

/**
 * <code>Simulation</code>
 */
class Simulation(name:String) extends Node(name) with Branch[Agent] with Actor {
  
  var timeStep = 60f
  
  override def start = {
    super.start
    this ! Simulation.UpdateDynamic
    this
  }
  
  def act {
    import field.kit.util.Timer
    
    var working = 0
    var timer = new Timer
    
    // start all registered agents
    children foreach(_.start)
    
    while(true) {
      receive {
        case Simulation.UpdateDynamic =>
          working = size
          val dt = timer.update
          var i=0
          while(i < size) {
            children(i) ! Simulation.Update(dt)
            i += 1
          }
        
        case Simulation.Finished =>
          working -= 1
          if(working <= 0) {
            Thread.sleep((1000.0/ timeStep).asInstanceOf[Long])
            this ! Simulation.UpdateDynamic
          }
      }
    }
  }
}
