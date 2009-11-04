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
    def fvSetStageEnabled(stage:Int, enabled:Boolean)
    def fvSetWarp(x1:Float, y1:Float, x2:Float, y2:Float, x3:Float, y3:Float, x4:Float, y4:Float)
    
    def fvGetBlobCount:Int
    def fvGetBlobData:Pointer
    def fvGetBlobDataLength:Int
    def fvGet(property:Int):Float
    
    def fvGetStageImage(stage:Int):Pointer
    def fvGetStageWidth(stage:Int):Int
    def fvGetStageHeight(stage:Int):Int
    def fvGetStageDepth(stage:Int):Int
    def fvGetStageSize(stage:Int):Int
  }

  protected[vision] val native = Native.loadLibrary("FieldVision", classOf[CVision]).asInstanceOf[CVision]
  
  protected var cameraType = 0
  protected var width:Int = 0
  protected var height:Int = 0
  protected var fps = 0
  
  val blobs = new Array[Blob](native.fvGetBlobCount)

  import util.Timer
  val timer = new Timer
  
  // -- Initialisation ---------------------------------------------------------

  // automatically create vision when this singleton is instantiated
  create
  
  // set defaults
  camera = Vision.CAMERA_OPENCV
  setSize(320, 240)
  framerate = 30
  
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
      blob.bounds.min.x = next
      blob.bounds.min.y = next
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

  /** sets the type of camera to use */
  def camera_=(cameraType:Int) {
    fine("Setting camera to", cameraType)
    if(native.fvSetCamera(cameraType) == ERROR) {
      warn("Couldnt set camera to:", cameraType)
    } else {
      this.cameraType = cameraType
    }
  }
  
  /** @return the current camera type */
  def camera = cameraType
  
  /** sets the */
  def setSize(width:Int, height:Int) {
    fine("Setting size to", width, "x", height)
    if(native.fvSetSize(width, height) == ERROR) {
      warn("Couldnt set size")
    } else {
      this.width = width
      this.height = height
    }
  }
  
  /** sets the cameras framerate in frames per second */
  def framerate_=(fps:Int) {
    fine("Setting framerate to", fps)
    if(native.fvSetFramerate(fps) == ERROR)
      warn("Couldnt set framerate")
    else
      this.fps = fps
  }
  
  /** @return the framerate */
  def framerate = fps
  
  // -- Properties -------------------------------------------------------------
  
  /** sets the background learning rate */
  def background_=(value:Float) = native.fvSet(PROC_BACKGROUND, value)
  
  /** @return the background learning rate */
  def background = native.fvGet(PROC_BACKGROUND)
  
  /** sets the background discrimination value */
  def threshold_=(value:Float) = native.fvSet(PROC_THRESHOLD, value)
  
  /** @return the background discrimination value */
  def threshold = native.fvGet(PROC_THRESHOLD)
  
  /** sets the dilate value */
  def dilate_=(value:Float) = native.fvSet(PROC_DILATE, value)
  
  /** @return the dilate value */
  def dilate = native.fvGet(PROC_DILATE)
  
  /** sets the erode value */
  def erode_=(value:Float) = native.fvSet(PROC_ERODE, value)
  
  /** @return the erode value */
  def erode = native.fvGet(PROC_ERODE)
  
  /** sets the minimum contour area */
  def contourMin_=(value:Float) = native.fvSet(PROC_CONTOUR_MIN, value)
  
  /** @return the minimum contour area */
  def contourMin = native.fvGet(PROC_CONTOUR_MIN)
  
  /** sets the minimum contour area */
  def contourMax_=(value:Float) = native.fvSet(PROC_CONTOUR_MAX, value)
  
  /** @return the maximum contour area */
  def contourMax = native.fvGet(PROC_CONTOUR_MAX)
  
  /** sets the contour reduction value */
  def contourReduce_=(value:Float) = native.fvSet(PROC_CONTOUR_REDUCE, value)
  
  /** @return the contour reduction value */
  def contourReduce = native.fvGet(PROC_CONTOUR_REDUCE)
  
  /** sets the maximum distance at which one blob in two consecutive frames can still be matched */
  def trackRange_=(value:Float) = native.fvSet(PROC_TRACK_RANGE, value)
  
  /** @return the track range value */
  def trackRange = native.fvGet(PROC_TRACK_RANGE)
  
  // -- Stages -----------------------------------------------------------------
  
  def stage(index:Int) = Stages(index).asInstanceOf[Stages.Stage]
  
  /**
   * Lists all frame processing stages and gives access to their images and properties
   */
  object Stages extends Enumeration {
    val input = new Stage(0, "Input")
    val warp = new Stage(1, "Warp")
    val background = new Stage(2, "Background")
    val difference = new Stage(3, "Difference")
    val threshold = new Stage(4, "Threshold")
    val dilate = new Stage(5, "Dilate")
    val erode = new Stage(6, "Erode")
    val contour = new Stage(7, "Contour")
    val detection = new Stage(8, "Detection")
    val tracking = new Stage(9, "Tracking")
    
    def size = maxId
    
    /**
     * Encapsulates a single processing stage image of the frame processor
     */
    class Stage(id:Int, name:String) extends Val(id:Int, name:String) {
      import java.nio.ByteBuffer
      
      def width = native.fvGetStageWidth(id)
	  def height = native.fvGetStageHeight(id)
	  def depth = native.fvGetStageDepth(id)
	  def size = native.fvGetStageSize(id)
	
	  protected var isEnabled = false
	  def enabled = isEnabled
	  def enabled_=(b:Boolean) = {
	    isEnabled = b
	    native.fvSetStageEnabled(id, isEnabled)
	  } 
	  
	  def image:ByteBuffer = {
	    val size = native.fvGetStageSize(id)
	    val pointer = native.fvGetStageImage(id)
	    if(pointer == null) return null
	    pointer.getByteBuffer(0, size)
	  }
    }
  }
  
  // -- Warp -------------------------------------------------------------------
  /** sets the image warp factor */
  def setWarp(_x1:Float, _y1:Float, _x2:Float, _y2:Float, _x3:Float, _y3:Float, _x4:Float, _y4:Float) {
    // convert from normalized [0,1] values to absolute pixels
    val x1 = _x1 * width
    val y1 = _y1 * height
    
    val x2 = _x2 * width
    val y2 = _y2 * height
    
    val x3 = _x3 * width
    val y3 = _y3 * height
    
    val x4 = _x4 * width
    val y4 = _y4 * height
    
    native.fvSetWarp(x1,y1, x2,y2, x3,y3, x4,y4)
  }
  
  /** resets the warp to its original values */
  def resetWarp = setWarp(0f,0f, 1f,0f, 1f,1f, 0f,1f)
}
