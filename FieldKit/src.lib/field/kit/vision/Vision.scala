/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */
package field.kit.vision

/**
 * -Djna.library.path=res/vision/build/Release 
 * -d32
 */
object Vision extends Logger {
  import com.sun.jna._

  /**
   * Interface defining the available FieldVision C methods
   */
  protected trait CVision extends Library {
    //def printf(format:String, args:Any*)
    //def printf(format:String)
    def fvCreate:Int;
    //def fvSimplex():Int
  }
  
  val native = Native.loadLibrary("FieldVision", classOf[CVision]).asInstanceOf[CVision]
  //val native = Native.loadLibrary("Simplex", classOf[CVision]).asInstanceOf[CVision]
 
           
  def main(args:Array[String]) {

    info("-- Vision ----------------------------------------------------------")
    
    var err = 0;
    err = native.fvCreate
    
    info("create", err)
    
	//native.printf("Hello C World")    
    info("done")
  }
}
