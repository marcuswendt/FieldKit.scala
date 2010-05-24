/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
