/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */
package field.kit.vision

/**
 * VM Args: -Djna.library.path=res/vision/build/Release -d32
 */
object Vision extends Logger {
  import com.sun.jna._

  // external constants
  /** Camera selection */
  val CAMERA_OPENCV = 0
  val CAMERA_OPENCV_FIRST = 1
  val CAMERA_OPENCV_SECOND = 2
  val CAMERA_OPENCV_THIRD = 3
  val CAMERA_PTGREY_BUMBLEBEE = 10

  // internal constants
  /** C function return values */
  protected val SUCCESS = 1
  protected val ERROR = 0
  
  /** Frame processor properties */
  protected val PROC_BACKGROUND = 0;
  protected val PROC_THRESHOLD = 1;
  protected val PROC_DILATE = 2;
  protected val PROC_ERODE = 3;
  protected val PROC_CONTOUR_MIN = 4;
  protected val PROC_CONTOUR_MAX = 5;
  protected val PROC_CONTOUR_REDUCE = 6;
  protected val PROC_TRACK_RANGE = 7;
  
  /** maximum number of contour points*/
  val CONTOUR_DATA_MAX = 1000 * 2
                                   
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
    def fvSet(property:Int, value:Float)
    
    def fvGetBlobCount:Int
    def fvGetBlobData:Pointer
    def fvGetBlobDataLength:Int
    def fvGet(property:Int)
  }

  protected val native = Native.loadLibrary("FieldVision", classOf[CVision]).asInstanceOf[CVision]
  protected var fps = 0
  
  val blobs = new Array[Blob](native.fvGetBlobCount)

  import util.Timer
  val timer = new Timer
  
  // -- Initialisation ---------------------------------------------------------

  // automatically create vision when this singleton is instantiated
  create
  
  // set defaults
  setSize(320, 240)
  setFramerate(30)
  
  // initialize blobs
  for(i <- 0 until blobs.size)
    blobs(i) = new Blob(i)
  
// TODO find a thread safe way to call destroy 
//  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable() {
//    def run = destroy
//  }))
  
  // -- Methods ----------------------------------------------------------------
  protected def create = {
    fine("Creating vision object")
    native.fvCreate
  }

  protected def destroy {
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
  
  /**
   * processes the next frame and updates the blob list
   */
  def update {
    // do not update faster than the framerate
  	if(timer.sinceStart < 1000/ fps.toFloat)
     return
   
    timer.update
      
    // process frame
    native.fvUpdate
    
    // reset blob list
    blobs foreach { _.active = false }
    
    // begin reading the int data
    val length = native.fvGetBlobDataLength
    val data = native.fvGetBlobData
    
    // no data
    if(data == 0) return
     
    var i = 0
    def next = {
      val value = data.getInt(i * 4)
      i += 1
      value
    }
    
    while(i < length) {
      next // read blob header
      
      // get current blob using its index
      val blob = blobs(next)
      blob.active = true
      blob.x = next
      blob.y = next
      
      next // read bounding box header
      blob.bounds.x1 = next
      blob.bounds.y1 = next
      blob.bounds.width = next
      blob.bounds.height = next
      
      next // read contour header
      blob.contourPoints = next
      
      blob.contour.clear
      for(j <- 0 until blob.contourPoints) {
        blob.contour.put(next).put(next)
      }
      blob.contour.rewind
    }
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
  
  /**
   * Sets the cameras frames per second,
   * also sets the internal fps that makes sure 
   */
  def setFramerate(fps:Int) {
    fine("Setting framerate to", fps)
    if(native.fvSetFramerate(fps) == ERROR)
      warn("Couldnt set framerate")
    else
      this.fps = fps
  }
}
