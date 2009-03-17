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
object ArraysVSArrayBufferTest extends Logger {
  
  def main(args : Array[String]) : Unit = {
    import scala.collection.mutable.ArrayBuffer
    val iterations = 1000000 * 30
    
    // static array
    var ar = new Array[Int](iterations)
    test("array sequential setter", {
    	for(i <- 0 until ar.length)
    		ar(i) = i
    })
    
    test("array sequential setter (length out of loop)", {
      val length = ar.length
    	for(i <- 0 until length)
    		ar(i) = i
    })
    
    // surprisingly fast!
    test("array functional foreach", ar foreach(access(_)) )
    
    // arraybuffer
    var ab = new ArrayBuffer[Int]
    test("ArrayBuffer filling "+ iterations +" ints", {
      for(i <- 0 until iterations)
    	  ab += i
    })
    
    // even faster!
    test("ArrayBuffer functional foreach", ab foreach(access(_)) )
  }
  
  def access(i:Int) = i + 1
  
  def test(name:String, func: => Unit) {
    info("test: "+ name)
    val start = System.currentTimeMillis
    func
    val dur = System.currentTimeMillis - start
    info(" => "+ dur +"ms")
  }
}
