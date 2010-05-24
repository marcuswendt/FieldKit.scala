/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
 * A simple helper to set up alarms and measure time between frames
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
