/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.util

/** 
 * convenience class for uniformly formatted timestamps
 * @author Marcus Wendt
 */
object Timestamp {
  import java.text.SimpleDateFormat
  import java.util.Date
  
  var format = new SimpleDateFormat("yy.MM.dd-H.mm.ss")
  
  def apply() = {
    val now = new Date
    format.format(now)
  }
}
