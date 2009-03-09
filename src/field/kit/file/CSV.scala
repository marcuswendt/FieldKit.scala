/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit.file

import field.kit.Logger

class CSVFormat extends FileFormat("csv") {
  var seperator = ','
}

object CSVFile extends CSVFormat with FileReader[CSVFile] with Logger {
  import java.io.InputStream
  
  def read(s:InputStream):CSVFile = {
    import java.io.BufferedReader
    import java.io.InputStreamReader
    
    // try to open the url and parse the file 
    try {
      val r = new BufferedReader(new InputStreamReader(s))
      val f = new CSVFile
      
      // add header columns
      f.headers ++= r.readLine.split(seperator) 
      
      // add rows
      var line = ""
      do {
        line = r.readLine
        if(line != null)
          f.rows += line.split(seperator)
      } while(line != null)
      
      r.close
      f
      
    // log error and return null if that fails
    } catch {
      case _ => {
        error("Couldnt read stream:", s)
        null
      }
    }
  } 
}

class CSVFile extends CSVFormat with FileWriter with Collection[Array[String]] {
  import java.io.File
  import scala.collection.mutable.ArrayBuffer
  
  var headers = new ArrayBuffer[String]
  var rows = new ArrayBuffer[Array[String]]
  
  def write(file:File) {}
  
  def size = rows.size
  def elements = rows.elements
}
