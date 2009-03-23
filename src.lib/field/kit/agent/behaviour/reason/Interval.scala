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
  
  override def init = set("interval", Math.random.asInstanceOf[Float] * 1000f)
  
  def apply = {
    time += dt
    
    val trigger = time > get[Float]("interval")
    if(trigger) time = 0
    trigger  
  }
}