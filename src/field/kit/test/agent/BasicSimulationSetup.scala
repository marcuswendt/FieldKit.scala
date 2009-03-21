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
  import field.kit.agent.graph._
  import field.kit.math._
  
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

//    for(name <- Seq("One", "Two", "Three")) {
	for(name <- Seq("One")) {
      val a = s.agents += new Agent(name)
      
      // perception context
      val p = a += new Context("Perception")
      
      p += ("Countdown", { c:Context =>
        println("checking "+ name +" for context "+ c.name)
        val time = c("../time", 0)
        val limit = c("limit", 10)
        time() > limit()
      }, { (c:Context, dt:Float) => 
        info("hurrah "+ dt)
      })
      
      // reasoning context
      val r = a += new Context("Reasoning")
      
      r += ("Time Updater", { c:Context => true }, { (c:Context, dt:Float) =>
        
        s.printTree
        
        val time = c[Int]("../time")
    	println("the time is: "+ time())
    	time() = time() + 1
      })
  
      // motor context
      val m = a += new Context("Motor")
      
      // fields
      a("position", new Vec3(1,2,3))
      
      p
    }
    
    s.printTree
    
    section("Running")
    val r = new SimpleRunner(s) 
    r.start
  }
}
