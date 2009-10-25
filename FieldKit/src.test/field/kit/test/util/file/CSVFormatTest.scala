/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit.test.util.file

object CSVFormatTest extends field.kit.Logger {
  import field.kit.util.file._
    
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
//        info("ROW", i, "columns ", row.length)
		info( ("" /: row) (_+" | "+_))
        i += 1
      }
      
      info("DONE")
    }
  }
}
