/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.util

/** 
 * various utility methods to deal with cross-platform issues
 * @author Marcus Wendt
 */
object OSUtil {
  /** the current operating system name in lowercase */
  val osName = System.getProperty("os.name").toLowerCase
  
  /** @return returns true if the system runs on a macintosh, false otherwise */
  def isMac = osName.startsWith("mac")
  
  /** @return returns true if the system runs on windows, false otherwise */
  def isWindows = osName.startsWith("windows")
}
