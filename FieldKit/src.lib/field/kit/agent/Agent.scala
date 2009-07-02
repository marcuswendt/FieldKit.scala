/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent

import scala.actors.Actor
import scala.actors.Actor._

/**
 * <code>Agent</code> represents a single <code>Actor</code> within a <code>Simulation</code> 
 * and is also the root of a <code>Neuron</code> tree, which defines its behaviour.
 * @author Marcus Wendt
 */
class Agent(name:String) extends Actor {
  def act = {}
  
  /*
  /** system containing sensory neurons; retrieving information about the environment */
  var sensor = new SensorSystem
  
  /** reasoning system, makes a decision about the best possible reaction to the 
   * sensor information and issues commands to the motor system */
  var reason = new ReasonSystem

  /** tries to fullfill the reason system's tasks by applying them to the agents body */
  var motor = new MotorSystem
  
  def act {
    loop {
      react {
      	case Simulation.Update(dt:Float) => 
      	  sensor.update(dt)
      	  reason.update(dt)
      	  motor.update(dt)
      	  sender ! Simulation.Finished
      }
    }
  }
  */
}