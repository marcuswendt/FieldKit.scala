/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
package field.kit.test

object LoggerTest {
  import field.kit.Logger
  
  class Logged extends Logger {}
  
  def main(args : Array[String]) : Unit = {
    val l = new Logged
    l.info("some info")
    l.warn("i warn you")
    l.error("an error")
    l.fatal("a totally fatal mistake")
  }
}
