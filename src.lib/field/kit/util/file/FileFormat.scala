/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 09, 2009 */
package field.kit.util.file

import field.kit.Logger

/* base classes to implement all sorts of file format readers/writers */
abstract class FileFormat(var suffix:String) {
  import java.io.File
  
  /** reference to the original source file (only set when loaded from the disk) */
  var source:File = null
}

abstract trait FileReader[T <: FileFormat] extends Logger {
  import java.io.File
  import java.net.URL
  import java.io.InputStream
  import java.io.FileInputStream
  
  def read(s:InputStream):T
  
  def read(file:String):T = {
    import java.net.MalformedURLException
    
    var f = null.asInstanceOf[T]
    
    if(file!=null) {
      // 1. via URL
      try {
        f = read(new URL(file))
      } catch {
        case e:MalformedURLException =>
      }
      
      // 2. via Classloader
      if(f == null) {
        val url = ClassLoader.getSystemResource(file)
        if (url != null) f = read(url)
      }
      
      // 3. via File
      if(f == null) f = read(new File(file))
    }
    
    f
  }
  
  /** reads to data from a local file */
  def read(file:File):T = {
    try {
      val f = read(new FileInputStream(file))
      if(f != null) f.source = file
      f
    } catch {
       case ex:Exception =>
        error("Couldn't open file", file)
        ex.printStackTrace
        null.asInstanceOf[T]
    }
  }
  
  /** reads the data from a given url*/
  def read(url:URL):T = {
    try {
      read(url.openStream)
    } catch {
      case _ =>
        error("Couldn't open url", url)
        null.asInstanceOf[T]
    }
  }
}

// TODO implement writer interface
abstract trait FileWriter {
  import java.io.File
  def write(file:File)
}