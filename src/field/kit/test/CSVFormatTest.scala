/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit.test

import field.kit.Logger

object CSVFormatTest extends Logger {
  import field.kit.file.CSVFile
  
  def main(args : Array[String]) : Unit = {
     if(args.length != 1) {
      fatal("INVALID ARGUMENTS")
    } else {
      info("PARSING CSV "+ args(0))
      val csv = CSVFile.read(args(0))
      
      info("HEADER")
      info(csv.headers)
      
      info("DATA")
      var i = 0
      csv foreach { row =>
        println("")
        info("ROW", i)
        info("columns ", row.length )
        info( ("" /: row) (_+", "+_))
        i += 1
      }
      
      info("DONE")
    }
  }
}
