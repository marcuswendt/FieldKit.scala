

/**
 * Base Class for all Vectors
 */
trait Logger {
  def info(m:Any) = System.out.println(m)
  
  def error(m:Any) = System.err.println(m)
  
  def fatal(m:Any):Unit = fatal(m, -1)
  
  def fatal(m:Any,code:Int) {
    error(m)
    System.exit(code)
  }
}
