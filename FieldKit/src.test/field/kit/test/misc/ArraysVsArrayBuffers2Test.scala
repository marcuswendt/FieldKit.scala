/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 16, 2009 */
package field.kit.test.misc

import field.kit.Logger

/**
 * compares access performance of regular arrays vs arraybuffers
 */
object ArraysVSArrayBuffer2Test extends Logger {
  logName = ""
  
  def main(args : Array[String]) : Unit = {
    
    // before warmup
    Logger.level = Logger.ALL
    info("-------- before warmup --------")
    run
    
    // warmup
    info("-------- warming up --------")
    Logger.level = Logger.NONE
    for(i <- 0 until 1000) run
    
    // the test
    Logger.level = Logger.ALL
    info("-------- running the test --------")
    run
  }
  
  def run {
    import scala.collection.mutable.ArrayBuffer
    import scala.collection.mutable.ListBuffer
    //val iterations = 1000000 * 10
    val iterations = 1000
    
    val dt = 0.1f
    
    // static array
    val ar = new Array[PrimitiveAgent](iterations)
    
    test("array for-loop fill", {
      for(i <- 0 until ar.length)
        ar(i) = new PrimitiveAgent(i)
    })

    test("array for-loop update", {
    	for(i <- 0 until ar.length)
    		ar(i).update(dt)
    })
    
    test("array functional foreach update with arg", ar.foreach(_.update(dt)) )
    
    test("array functional foreach update no arg", ar.foreach(_.update) )
    
    // ----------------------------------------------------------------------------------------
    
    // arraybuffer
    val ab = new ArrayBuffer[PrimitiveAgent]
    
    test("ArrayBuffer for-loop fill", {
      for(i <- 0 until iterations)
    	  ab += new PrimitiveAgent(i)
    })
    
    test("ArrayBuffer for-loop update w/ arg", {
    	for(i <- 0 until iterations)
    		ab(i).update(dt)
    })
    
    test("ArrayBuffer for-loop update no arg", {
    	for(i <- 0 until iterations)
    		ab(i).update
    })
    
    test("ArrayBuffer functional foreach update with arg", ab.foreach(_.update(dt)) )
    test("ArrayBuffer functional foreach update no arg", ab.foreach(_.update) )
    
    // -- Java ArrayList -------------------------------------------------------
    val al = new java.util.ArrayList[PrimitiveAgent]()
    test("Java ArrayList for-loop fill", {
      for(i <- 0 until iterations)
    	  al.add(new PrimitiveAgent(i))
    })
    
    test("Java ArrayList for-loop update with arg", {
      for(i <- 0 until al.size)
        al.get(i).update(dt)
    })

    test("Java ArrayList for-loop update no arg", {
      for(i <- 0 until al.size)
        al.get(i).update
    })

    /*
    // -- Scala JCL ArrayList --------------------------------------------------------
    val al2 = new collection.jcl.ArrayList[PrimitiveAgent]
    test("JCL ArrayList for-loop fill", {
      for(i <- 0 until iterations)
    	  al2 += new PrimitiveAgent(i)
    })
    
    test("JCL ArrayList functional for-loop update with arg", {
    	al2 foreach(_.update(dt))
    })

    test("JCL ArrayList functional for-loop update no arg", {
      al2 foreach(_.update)
    })
    
    test("JCL ArrayList for-loop update with arg", {
      for(i <- 0 until al2.size)
        al2(i).update(dt)
    })

    test("JCL ArrayList for-loop update no arg", {
      for(i <- 0 until al2.size)
        al2(i).update
    })
    */
    
    // -- ListBuffer -----------------------------------------------------------
    val lb = new ListBuffer[PrimitiveAgent]    
    
    test("ListBuffer for-loop fill", {
      for(i <- 0 until iterations)
    	  lb += new PrimitiveAgent(i)
    })
    
    test("ListBuffer for-loop update", {
    	for(i <- 0 until lb.length)
    		lb(i).update(dt)
    })
    
    test("ListBuffer functional foreach update with arg", lb.foreach(_.update(dt)) )
    test("ListBuffer functional foreach update no arg", lb.foreach(_.update) )
    
    // -- Immutable -------------------------------------------------------------
    var s:List[PrimitiveAgent] = Nil 
    test("Seq for-loop fill", {
      for(i <- 0 until iterations)
    	  s = new PrimitiveAgent(i) :: s
    })
    
    test("Seq functional for-loop update with arg", {
      s foreach(_.update(dt))
//      for(i <- 0 until al.size)
//        al2(i).update(dt)
    })

    test("Seq functional for-loop update no arg", {
      s foreach(_.update(dt))
//      for(i <- 0 until al.size)
//        al2(i).update
    })
    
    test("Seq for-loop update with arg", {
      for(i <- 0 until s.size)
        s(i).update(dt)
    })

    test("Seq for-loop update no arg", {
      for(i <- 0 until s.size)
        s(i).update
    })
  }
  
  
  def access(i:Int) = i + 1
  
  def test(name:String, func: => Unit) {
    val start = System.nanoTime
    func
    val dur = (System.nanoTime - start) / 1000000.0
    
    var fill = ":"
    for(i <- 0 until 50 - name.length)
      fill += " "
    
    info(name + fill + dur +"ms")
  }
  
  class PrimitiveAgent(val id:Int) {
    def update = {}
    
    def update(dt:Float) = {
    	dt + 1.0
    }
  }
}
