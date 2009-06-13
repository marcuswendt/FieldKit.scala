/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent

import actors.Actor

import field.kit.agent.neuron._

/**
 * <code>Agent</code> represents a single <code>Actor</code> within a <code>Simulation</code> 
 * and is also the root of a <code>Neuron</code> tree, which defines its behaviour. 
 */
class Agent(name:String) extends NeuronGroup(name) with Actor {
  
  /** system containing sensory neurons; retrieving information about the environment */
  var sensor = new SensorSystem
  this += sensor
  
  /** reasoning system, makes a decision about the best possible reaction to the 
   * sensor information and issues commands to the motor system */
  var reason = new ReasonSystem
  this += reason

  /** tries to fullfill the reason system's tasks by applying them to the agents body */
  var motor = new MotorSystem
  this += motor
  
  def act {
    while(true) {
      receive {
      	case Simulation.Update(dt:Float) => 
      	  update(this, dt)
      	  sender ! Simulation.Finished
      }
    }
  }
}