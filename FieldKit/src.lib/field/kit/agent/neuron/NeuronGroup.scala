/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.agent.neuron

class NeuronGroup(name:String) extends Neuron(name) {
  def apply = true
}

class SensorSystem extends NeuronGroup("sensor") {}

class ReasonSystem extends NeuronGroup("reason") {}

class MotorSystem extends NeuronGroup("reason") {}