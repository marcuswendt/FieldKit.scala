/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 18, 2009 */
package field.kit.test.misc

/**
 * test, trying to solve <code>ArrayBuffer</code>s memory leak problems by finding
 * a way to not use iterators
 */
object OptimizedArrayBufferTest extends field.kit.Logger {
  logName = ""
  
  val dt = 0.1f
  val iterations = 100000
  
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
    val ab = new FastArrayBuffer[PrimitiveAgent]
    
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
    
    test("ArrayBuffer functional foreach update w/ arg", ab.foreach(_.update(dt)) )
    test("ArrayBuffer functional foreach update no arg", ab.foreach(_.update) )
    
    info("-- Optimized each --------------------------------------------------")
    
    test("ArrayBuffer optimized loop w/ arg", {
      ab.fast_foreach( _.update(dt) )
    })
    
    test("ArrayBuffer optimized loop no arg", {
    	ab.fast_foreach( _.update )
    })
    
  }
  
  // -- Classes ----------------------------------------------------------------
  class FastArrayBuffer[A] extends collection.mutable.ArrayBuffer[A] {
    override def apply(i:Int) = array(i).asInstanceOf[A]
    
    def fast_foreach(func: A => Unit) {
      var i=0
      var l=size
      while(i < l) {
        func( array(i).asInstanceOf[A] )
        i += 1
      }
    }
    
    //def ar:Array[A] = array.asInstanceOf[Array[A]]
  }
  
  // -- Test Function ----------------------------------------------------------
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
    def update = 1.0f
    
    def update(dt:Float) = dt + 1.0f
  }
}
