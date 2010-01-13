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
object Timer {
  /** converts nanoseconds to milliseconds */
  val NS2MS = 1.0/ 1000000000 * 1000.0
  
  // -- Timestamp --------------------------------------------------------------
  import java.text.SimpleDateFormat
  import java.util.Date

  var timestampFormat = new SimpleDateFormat("yy.MM.dd-H.mm.ss")
  
  /** returns a simple timestamp **/
  def apply() = {
    timestampFormat.format(new Date)
  }
}


/** 
 * a simple helper for making sketches time-invariant
 * @author Marcus Wendt
 */
class Timer {
	
  var elapsed = 0f
  var interval = 0f
  
  var start = time

  /** 
   * @return true if the set interval is up, otherwise false
   */
  def apply() = {
	update
	elapsed > interval
  }
  
  def time = System.nanoTime
  
  /** @return the time since the last update in ms */
  def sinceStart = ((time - start) * Timer.NS2MS).toFloat
  
  /** update delta time since last update */
  def update = {
    val now = time
    val dt = ((now - start) * Timer.NS2MS).toFloat
    elapsed += dt
    start = now
    dt
  }
  
  /** resets this timer*/
  def reset = {
    elapsed = 0
    start = time
  }
}
