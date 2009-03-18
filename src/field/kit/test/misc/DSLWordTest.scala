/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.test.misc

object DSLWordTest extends field.kit.Logger {
  class Key {
    def apply() = "a key"
  }
  
  def main(args : Array[String]) : Unit = {
    val k = new Key
    val x = k()
    info("k", k())
  }
}
