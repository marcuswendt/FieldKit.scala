/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */
package field.kit.vision


object Vision extends Logger {
  import com.sun.jna._

  /**
   * Interface defining the available FieldVision C methods
   */
  trait CVision extends Library {
    //def printf(format:String, args:Any*)
    def printf(format:String)
  }
  
  val native = Native.loadLibrary("c", classOf[CVision]).asInstanceOf[CVision]
 
           
  def main(args:Array[String]) {

    info("main") 	
	native.printf("Hello C World")    
    info("done")
  }
}
