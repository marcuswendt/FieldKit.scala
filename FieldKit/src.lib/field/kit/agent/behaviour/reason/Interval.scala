/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 23, 2009 */
package field.kit.agent.behaviour.reason

/** triggers (returning true/ false in the update-cycle) in regular intervals
 * this allows to time
 */
class Interval extends Behaviour("interval") {
  var time = 0f
  
  def apply = {
    time += dt
    val trigger = time > get[Float]("interval", 1000f)
    if(trigger) time = 0
    trigger  
  }
}