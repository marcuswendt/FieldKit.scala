/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.test.agent

import field.kit._
  
object BasicSimulationSetup extends Logger {
  import field.kit.agent._
  import field.kit.agent.graph._
  
  def section(s:String) {
    println("")
    info("------------------------------------------------")
    info(s)
    info("------------------------------------------------")
  }
  
  def main(args : Array[String]) : Unit = {
    section("BasicSimulationSetup")
    
    val s = new Simulation("Test Simulation")
    s += new Branch("config")

//    for(name <- Seq("One", "Two", "Three")) {
	for(name <- Seq("One")) {
      val a = s.agents += new Agent(name)
      
      // perception context
      val p = a += new Context("Perception")
      
      //var countdown = p += new Behaviour("Countdown") {
      val countdown = new Behaviour("Countdown") {
        def apply = {
          val time = get("time",0)
          
          set("time", time + 1)
          info("the time is", time)
          time > 100
        }
      }
      
      p += countdown
      
      countdown += new Behaviour("Alarm") {
        def apply = {
          info("Alarm!")
          s.printTree
          System.exit(0)
          false
        }
      }
      
  /*    
      p += ("Countdown", { c:Context =>
        val time = c("../time", 0)
        val limit = c("limit", 10)
        time() = time() + 1
        
        println("checking "+ name +" for context "+ c.name +" time "+ time())
        
        time() > limit()
      }, { (c:Context, dt:Float) => 
        info("hurrah "+ dt)
        val done = c("../done", true)
        done() = true
      })
      
      // reasoning context
      val r = a += new Context("Reasoning")
      
      r += ("Feierabend Checker", { c:Context => 
        val done = c("../done", false)      
        done()
      }, { (c:Context, dt:Float) =>
        section("Done!")
        s.printTree
        System.exit
      })
  */
  
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
