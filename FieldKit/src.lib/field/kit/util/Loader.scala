/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 15, 2009 */
package field.kit.util

/**
 * Helpers to resolve and load Files
 * @author Marcus Wendt
 */
object Loader extends field.kit.Logger {
  import java.net.URL
  import java.io.File
  
  /**
   * Tries to resolve the given String to an URL in various ways
   */
  def get(file:String) = {
    import java.net.MalformedURLException
    
    var url:URL = null
    
    if(file != null) {
      // 1. via URL
      try {
        url = new URL(file)
      } catch {
        case e:MalformedURLException =>
      }
      
      // 2. via ClassLoader
      if(url == null) url = ClassLoader.getSystemResource(file)
      
      // 3. via local File path
      if(url == null) url = new File(file).toURL
    }
    
    url
  }
}
