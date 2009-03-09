/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit.file

import scala.collection.mutable.ArrayBuffer
import field.kit.Logger

class CSVFormat extends FileFormat("csv") {
}

object CSVFile extends CSVFormat with FileReader[CSVFile] with Logger {
  import java.io.InputStream
  import scala.util.matching.Regex
  
  /** The rather involved pattern used to match CSV's consists of three
   * alternations: the first matches aquoted field, the second unquoted,
   * the third a null field. 
   */
  //val pattern = "\"([^\"]+?)\",?|([^,]+),?|,"
  val pattern = "\"([\"]+?)\",?|([^,]+),?|,"
  val regex = new Regex(pattern)
  
  def read(s:InputStream):CSVFile = {
    import java.io.BufferedReader
    import java.io.InputStreamReader
    
    // try to open the url and parse the file 
    try {
      val r = new BufferedReader(new InputStreamReader(s))
      val f = new CSVFile
      
      // add header columns
      f.headers ++= parse(r.readLine) 
      
      // add rows
      var line = ""
      do {
        line = r.readLine
        if(line != null)
          f += line 
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
  
  // TODO could be improved but works for now
  def parse(line:String) = {
    val l = new ArrayBuffer[String]()
    
    regex findAllIn line foreach { m =>
      var e = m
      
      if(e == null) e = null
      
      // trim trailing ,
      if(e.endsWith(",")) 
        e = e.substring(0, e.length - 1)
      
      // assume also ends with
      if(e.startsWith("\"")) 
        e = e.substring(1, e.length - 1)
      
      if(e.length == 0)
        e = null
      
      // replace double quotes with single quotes
      if(e != null)
        e = e.replaceAll("\"\"", "\"")
      
      l += e
    }
    l
  }
}

class CSVFile extends CSVFormat with FileWriter with Collection[ArrayBuffer[String]] {
  import java.io.File
  
  var headers = new ArrayBuffer[String]
  var rows = new ArrayBuffer[ArrayBuffer[String]]
  
  def write(file:File) {}
  
  def columns = headers.length
  def size = rows.size
  private var current = headers
  
  def elements = new Iterator[ArrayBuffer[String]] {
    var i = 0
    def next = {
      current = rows(i)
      i += 1
      current
    }
    def hasNext = i+1 < rows.length
  }
  
  def +=(line:String) = rows += CSVFile.parse(line)
  
  /** returns an entry from the current row*/
  def apply(i:Int) = {
    if(i < current.length) 
      current(i)
    else
       null
  }
  
  /** returns an entry using the name of a header column */
  def apply(n:String):String = {
    val i = headers findIndexOf (_.equals(n))
    i match {
      case -1 => null
      case _ => apply(i) 
    }
  }
}
