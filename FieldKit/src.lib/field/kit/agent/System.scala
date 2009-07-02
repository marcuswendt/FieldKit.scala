/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 15, 2009 */
package field.kit.agent

/**
 * Base class all of the <code>Agents</code> subsystems.
 */
abstract class System {
  def update(dt:Float)
}

class SensorSystem extends System {
  def update(dt:Float) {}
}

class ReasonSystem extends System {
  def update(dt:Float) {}
}

class MotorSystem extends System {
  def update(dt:Float) {}
}