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
  
  def main(args : Array[String]) : Unit = {
    
    info("basic graph test")
    info("--------------------------------")
    info("constructing graph")
    
    val r = new Root
    val debug = r("debug", true)
    
    r("/settings/numParticles", 1000)
    r("settings/numObjects", 2000)
    r("settings/X", 3000)
    r("../settings/Y", false)
    
    r("/agents/1/behaviours/test/var1", 12345)
    
    info("--  the tree --")
    r.printTree
    
    /*
    info("-------- Constructing Graph --------")
    
    val r = new Root
    r += ("debug", true)
    
    val a = r += "group A"
    val a1 = a += "subgroup 1"
    val a1a = a1 += "sub-subgroup a1a"
    a1a += ("check this shit out", 12345687890L)
    
    val b = r += "another group B"
    val c = r += "group C!!"
    c += ("var1", 1111111111)
    c += ("var2", 22)
    c += ("var3", 333)
    
    val d = r += "group C!!/a lot of/subgroups/to this/tree"
    for(i <- 0 until 10)
    	d += ("leaf "+i, i)
    
    r.printTree
    
    println("")
    info("-------- Addresses --------")
    info("by index c(0)", c(0))
    info("by absolute address ", c("/group C!!/var2"))
    info("by relative address ", c("var3"))
    
    c("var3") = 444
    
    println("")
    info("-------- Keys --------")
    
    val k1 = c.key("../var2")
    info("key1 ", k1, c(k1))
    */
    
//    info("-------- Constructing Addresses --------")
//    val k1 = a1a % "../"  
//    info("a1a parent: ", k1)
//    
//    val k2 = a1a % "../subgroup 2"
//    info("a1a subgroup 2: ", k2)
//    
//    k2 += ("var1", 100)
//    k2 += ("var2", 200)
//    k2 += ("var3", 300)
    
//    info("-------- Misc --------")
//    info("a1a's address", a1a.address)
//    info("a1a's hashCode", a1a.hashCode)
//    
//    r.printTree
    
  }
}
