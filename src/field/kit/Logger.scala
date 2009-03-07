/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit


/**
 * Logging Class 
 */
object Logger extends Enumeration {
  val ALL = Value
  val FINE = Value
  val INFO = Value
  val ERROR = Value
  val FATAL = Value
  val NONE = Value
  
  def log(l:Value, name:String, m:Seq[Any]) {
    val s = if(l < ERROR) System.out else System.err
    val prefix = name +":" 
    s.println( (prefix /: m) (_ +" "+ _) )
  }
}

trait Logger {
  var logName = "Logger"
  var logLevel = Logger.ALL
  
  private def log(l:Logger.Value, m:Seq[Any]) = if(l >= logLevel) Logger.log(l, logName, m)
  
  def fine(m:Any*) = log(Logger.FINE, m)
  def info(m:Any*) = log(Logger.INFO, m)
  def error(m:Any*) = log(Logger.ERROR, m)
  
  def fatal(m:Any*):Unit = { log(Logger.FATAL, m); System.exit(-1) }
  def fatal(code:Int, m:Any*) = { log(Logger.FATAL, m); System.exit(code) }
}
