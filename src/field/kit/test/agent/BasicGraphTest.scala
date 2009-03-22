/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.test.agent

object BasicGraphTest extends field.kit.Logger {
  import field.kit.agent.graph._
  
  def section(s:String) {
    println("")
    info("------------------------------------------------")
    info(s)
    info("------------------------------------------------")
  }
  
  def main(args : Array[String]) : Unit = {

    section("constructing graph")
    val r = new Root
    val debug = r("debug", true)
    
    r("/settings/numParticles", 1000)
    r("settings/numObjects", 2000)
    r("settings/X", 3000)
    //r("../settings/Y", false)
    
    r("/agents/1/behaviours/test/var1", 12345)
    
    section("=> tree")
    r.printTree
    
    section("access tests")
    val particles = r("settings")("numParticles", 1000)
    info("numParticles", particles())

    val objects = r("settings/numObjects", 4000)
    objects() = 100
    
    // test access from another object - other than root
    val s = r("settings")
    val x = s("../debug")
    info("x", x)

    section("=> tree")
    r.printTree
    
  }
}
