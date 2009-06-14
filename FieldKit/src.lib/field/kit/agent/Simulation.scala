/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent

/** 
 * companion object to class <code>Simulation</code>
 * @author Marcus Wendt
 */
object Simulation {
  /** tells the simulation to update all its agents */
  case class UpdateDynamic()
	
  /** tells an agent to update its neuron network for one frame */
  case class Update(dt:Float)
	
  /** returned from the agent when it finished the update */
  case class Finished()  
}

import scala.actors.Actor
import scala.actors.Actor._

import field.kit.util.datatype.graph._

/**
 * <code>Simulation</code>
 * @author Marcus Wendt
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
    
    loop {
      react {
        case Simulation.UpdateDynamic =>
          working = size
          
          // try to run at the given timeStep frequency
          val dtReal = timer.update
          val dtTarget = 1000f/ timeStep
          var dt = dtReal
          if(dtReal < dtTarget) {
            val timeOut = (dtTarget - dtReal)
            //info("dtReal", dtReal, "dtTarget", dtTarget, "timeOut", timeOut)
            Thread.sleep(timeOut.asInstanceOf[Long])
            dt = dtTarget
          }
          
          var i=0
          val message = Simulation.Update(dt)
          while(i < size) {
            children(i) ! message
            i += 1
          }
        
        case Simulation.Finished =>
          working -= 1
          if(working <= 0) this ! Simulation.UpdateDynamic
      }
    }
  }
}
