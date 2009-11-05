/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 07, 2009 */
package field.kit

/**
 * Implements a flexible Logging System
 * @author Marcus Wendt
 */
object Logger extends Enumeration {  
  val ALL = Value
  val FINE = Value
  val INFO = Value
  val WARNING = Value
  val ERROR = Value
  val FATAL = Value
  val NONE = Value
  
  var level = Logger.INFO
  
  def log(l:Value, name:String, m:Seq[Any]) {
    if(l >= level) {
      val s = if(l < WARNING) System.out else System.err
    
      val prefix = "["+ name +"]"
      s.println( (prefix /: m) (_ +" "+ _) )
    }
  }
}

trait Logger {
  private var name = className
  
  private def className = {
    try {
      var n = this.getClass.getCanonicalName
      if(n.endsWith("$")) n = n.slice(0, n.length-1)
      var dot = n.lastIndexOf('.')
      if(dot < 0) n else n.substring(dot+1)
    } catch {
      case e:NoClassDefFoundError => "Anonymous"
    }
  }
  
  private def log(l:Logger.Value, m:Seq[Any]) = Logger.log(l, name, m)
  
  def logName_=(name:String) = this.name = name
  def logName = name
  
  def fine(m:Any*) = log(Logger.FINE, m)
  def info(m:Any*) = log(Logger.INFO, m)
  def warn(m:Any*) = log(Logger.WARNING, m)
  def error(m:Any*) = log(Logger.ERROR, m)
  
  def fatal(m:Any*):Unit = { log(Logger.FATAL, m); System.exit(-1) }
  def fatal(code:Int, m:Any*) = { log(Logger.FATAL, m); System.exit(code) }
}
