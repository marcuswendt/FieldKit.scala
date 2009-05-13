/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.util

/** 
 * a simple helper for making sketches time-invariant
 * @author Marcus Wendt
 */
class Timer {
  def time = System.nanoTime
  val resolution = 1000000000f
  val time2millisec = 1f/ resolution * 1000f
  
  var elapsed = 0f
  var last = time
  
  private var dt = 0f
  
  reset
  
  /** update delta time since last update */
  def update = {
    val now = time
    dt = (now - last) * time2millisec
    elapsed += dt
    last = now
    dt
  }
  
  /** resets this timer*/
  def reset = {
    elapsed = 0
    last = time
  }
}
