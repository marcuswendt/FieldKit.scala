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
   * Tries to resolve the given <code>String</code> to an <code>URL</code> in various ways
   */
  def resolveToURL(_file:String) = {
    import java.net.MalformedURLException
    
    var url:URL = null
    var file = _file
    
    if(file != null) {
      file = file.replaceAll(" ","%20")

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
  
  /**
   * Tries to read the file from the given <code>URL</code> into a <code>String</code>
   * @return 
   */
  def readFile(url:URL) = {
    import java.io.BufferedReader
    import java.io.InputStreamReader

    try {
      var buffer = ""
      val reader = new BufferedReader(new InputStreamReader(url.openStream))
      var line = ""
      while (line != null) {
        line = reader.readLine
        buffer += line + "\n"
      }
      buffer
    } catch {
      case(e:Exception) =>
        warn(e)
        null
    }
  }
}
