/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 15, 2009 */
package field.kit.util

import field.kit.Logger

/**
 * Helpers to resolve and load Files
 * @author Marcus Wendt
 */
object Loader extends Logger {
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
      if(url == null) url = new File(file).toURI.toURL
    }
    
    url
  }
  
  /**
   * Tries to read the file from the given <code>URL</code> into a <code>String</code>
   * @return 
   */
  def read(url:URL) = {
    import java.io.BufferedReader
    import java.io.InputStreamReader

    try {
      var buffer = ""
      val reader = new BufferedReader(new InputStreamReader(url.openStream))
      var line = ""
      while (line != null) {
        line = reader.readLine
        if(line != null)
          buffer += line + "\n"
      }
      buffer
    } catch {
      case(e:Exception) =>
        warn(e)
        null
    }
  }
  
  def read(file:File) = {
    import java.io.FileReader
    import java.io.BufferedReader

    info("reading", file)
    
    try {
      var buffer = ""
      val reader = new BufferedReader(new FileReader(file))
      var line = ""
      while (line != null) {
        line = reader.readLine
        if(line != null)
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
