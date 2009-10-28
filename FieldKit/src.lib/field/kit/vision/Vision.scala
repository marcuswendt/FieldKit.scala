/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */
package field.kit.vision

/**
 * VM Args: -Djna.library.path=res/vision/build/Release 
 */
object Vision extends Logger {
  import com.sun.jna._

  /** C function return values */
  val SUCCESS = 1
  val ERROR = 0
  
  /** Camera selection */
  val CAMERA_OPENCV = 0
  val CAMERA_OPENCV_FIRST = 1
  val CAMERA_OPENCV_SECOND = 2
  val CAMERA_OPENCV_THIRD = 3
  val CAMERA_PTGREY_BUMBLEBEE = 10

//  /** Lists all supported Camera types */
//  object Camera extends Enumeration {
//    val OpenCV = Value(0)
//    val First = Value(1)
//    val Second = Value(2)
//    val Third = Value(3)
//    val PTGreyBumblebee2 = Value(10)
//  }
                                   
  /**
   * Interface defining the available FieldVision C methods
   */
  protected trait CVision extends Library {
    def fvCreate:Int;
    def fvDestroy:Int
    
    def fvStart:Int
    def fvStop:Int
    def fvUpdate:Int
    
    def fvSetCamera(camera:Int):Int
    def fvSetSize(width:Int, height:Int):Int
    def fvSetFramerate(fps:Int):Int
  }
  
  protected val native = Native.loadLibrary("FieldVision", classOf[CVision]).asInstanceOf[CVision]
  
  protected var fps = 30
  
  // -- Methods ----------------------------------------------------------------
  protected def create = {
    fine("Creating vision object")
    native.fvCreate
  }

  def destroy {
    fine("Destroying vision object")
    native.fvDestroy
  }
  
  def start {
    fine("starting")
    if(native.fvStart == ERROR)
      warn("Couldnt start vision")
    fine("done")
  }
  
  def stop {
    fine("stopping")
    if(native.fvStop == ERROR)
      warn("Couldnt start vision")
  }
  
  def update {
    native.fvUpdate
  }

  def setCamera(camera:Int) {
    fine("Setting camera to", camera)
    if(native.fvSetCamera(camera) == ERROR)
      warn("Couldnt set camera to:", camera)
  }
  
  def setSize(width:Int, height:Int) {
    fine("Setting size to", width, "x", height)
    if(native.fvSetSize(width, height) == ERROR)
      warn("Couldnt set size")
  }
  
  def setFramerate(fps:Int) {
    fine("Setting framerate to", fps)
    if(native.fvSetFramerate(fps) == ERROR)
      warn("Couldnt set framerate")
    else
      this.fps = fps
  }

  // -- Initialisation ---------------------------------------------------------
  
  // automatically create vision when this singleton is instantiated
  create
  
  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable() {
    def run = destroy
  }))
  
  // -- Test -------------------------------------------------------------------
  def main(args:Array[String]) {
    
    info("---- Basic FieldVision Test ----")
    
    Logger.level = Logger.FINE
    
    Vision.setCamera(Vision.CAMERA_OPENCV)
    Vision.setSize(640, 480)
    Vision.start
    
    for(i <- 0 until 60) {
      info("frame", i)
      Vision.update
      Thread.sleep(1000/ fps)
    }
    
    info("done")
  }
}
