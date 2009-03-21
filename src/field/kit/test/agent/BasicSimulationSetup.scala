/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.test.agent

object BasicSimulationSetup extends field.kit.Logger {
  import field.kit.agent._
  
  def section(s:String) {
    println("")
    info("------------------------------------------------")
    info(s)
    info("------------------------------------------------")
  }
  
  def main(args : Array[String]) : Unit = {
    section("BasicSimulationSetup")
    
    val s = new Simulation
    s += "config"
    
    val a = s += new Agent("One")
    
    
    
    s.printTree
  }
}
