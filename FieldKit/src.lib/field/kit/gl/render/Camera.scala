/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 25, 2009 */
package field.kit.gl.render

/**
 * Companion object to class Camera
 */
object Camera {
  import javax.media.opengl.glu.GLU
  val glu = new GLU
}

/**
 * Construct a new Camera with the given frame width and height.
 */
class Camera(var width:Int, var height:Int) extends Renderable with Logger {
  import math._
  import math.FMath._
  import util.Buffer
  import javax.media.opengl.GL
  import javax.media.opengl.glu.GLU
  
  // -- Depth Range ------------------------------------------------------------
  /** The near range for mapping depth values from normalized device coordinates to window coordinates. */
  var depthRangeNear = 0f
  
  /** The far range for mapping depth values from normalized device coordinates to window coordinates. */
  var depthRangeFar = 1f
  
  // -- Frame ------------------------------------------------------------------
  /** Camera's location */
  val location = new Vec3(0,0,0)
  
  /** Direction of camera's 'left' */
  val left = new Vec3(1,0,0)
  
  /** Direction of 'up' for camera. */
  val up = new Vec3(0,1,0)
  
  /** Direction the camera is facing. */
  val direction = new Vec3(0,0,1)
  
  // -- Frustum ----------------------------------------------------------------
  /** Distance from camera to near frustum plane. */
  var frustumNear = 1f
  
  /** Distance from camera to far frustum plane. */
  var frustumFar = 2f
  
  /** Distance from camera to left frustum plane. */
  var frustumLeft = -0.5f
  
  /** Distance from camera to right frustum plane. */
  var frustumRight = 0.5f
  
  /** Distance from camera to top frustum plane. */
  var frustumTop = 0.5f
  
  /** Distance from camera to bottom frustum plane. */
  var frustumBottom = -0.5f
  
  // -- Viewport ---------------------------------------------------------------
  /** Percent value on display where horizontal viewing starts for this camera. */
  var viewportLeft = 0f
  
  /** Percent value on display where horizontal viewing ends for this camera. */
  var viewportRight = 1f
  
  /** Percent value on display where vertical viewing ends for this camera. */
  var viewportTop = 1f
  
  /** Percent value on display where vertical viewing begins for this camera. */
  var viewportBottom = 0f
  
  /** Defines wether this view is a parallel or perspective projection */
  var parallelProjection = false
  
  /** Convenience store for fovY. Only set during setFrustumPerspective and never used. */
  var fovY = Float.NaN
  
//  /** Array holding the planes that this camera will check for culling. */
//  var worldPlane = new Array[Plane]
  
  // -- Internal Variables -----------------------------------------------------
  protected var depthRangeDirty = false
  protected var viewportDirty = false
  protected var frustumDirty = false
  protected var frameDirty = false
  
  protected var updateMVMatrix = true
  protected var updatePMatrix = true
  protected var updateMVPMatrix = true
  protected var updateInverseMVPMatrix = true
  
  protected val _modelView = new Mat4
  protected val _projection = new Mat4
  protected val _modelViewProjection = new Mat4
  protected val _modelViewProjectionInverse = new Mat4
  protected val _transMatrix = new Mat4
  
  private val tempMatrixBuffer = Buffer.float(16)
  
//  // Temporary values computed in onFrustumChange that are needed if a call is made to onFrameChange.
//  protected val coeffLeft = new Array[Float](2)
//  protected val coeffRight = new Array[Float](2)
//  protected val coeffBottom = new Array[Float](2)
//  protected val coeffTop = new Array[Float](2)
  
  // -- Initialisation ---------------------------------------------------------
  onDepthRangeChange
  onFrustumChange
  onViewportChange
  onFrameChange
  
  def onDepthRangeChange = 
    depthRangeDirty = true
  
  /**
   * Updates internal frustum coefficient values to reflect the current frustum plane values.
   */
  def onFrustumChange = {
//    // perspective projection
//    if(!parallelProjection) {
//      val nearSq = frustumNear * frustumNear
//      val leftSq = frustumLeft * frustumLeft
//      val rightSq = frustumRight * frustumRight
//      val bottomSq = frustumBottom * frustumBottom
//      val topSq = frustumTop * frustumTop
//      
//      var inverseLength = (1.0 / Math.sqrt(nearSq + leftSq)).asInstanceOf[Float]
//      coeffLeft(0) = frustumNear * inverseLength
//      coeffLeft(1) = -frustumLeft * inverseLength
//
//      inverseLength = (1.0 / Math.sqrt(nearSq + rightSq)).asInstanceOf[Float]
//      coeffRight(0) = -frustumNear * inverseLength
//      coeffRight(1) = frustumRight * inverseLength
//
//      inverseLength = (1.0 / Math.sqrt(nearSq + bottomSq)).asInstanceOf[Float]
//      coeffBottom(0) = frustumNear * inverseLength
//      coeffBottom(1) = -frustumBottom * inverseLength
//
//      inverseLength = (1.0 / Math.sqrt(nearSq + topSq)).asInstanceOf[Float]
//      coeffTop(0) = -frustumNear * inverseLength
//      coeffTop(1) = frustumTop * inverseLength
//      
//    // parallel projection
//    } else {
//      if (frustumRight > frustumLeft) {
//        coeffLeft(0) = -1
//        coeffLeft(1) = 0
//        
//        coeffRight(0) = 1
//        coeffRight(1) = 0
//        
//      } else {
//        coeffLeft(0) = 1
//        coeffLeft(1) = 0
//        
//        coeffRight(0) = -1
//        coeffRight(1) = 0
//      }
//      
//      if (frustumBottom > frustumTop) {
//        coeffBottom(0) = -1
//        coeffBottom(1) = 0
//        
//        coeffTop(0) = 1
//        coeffTop(1) = 0
//      } else {
//        coeffBottom(0) = 1
//        coeffBottom(1) = 0
//        
//        coeffTop(0) = -1
//        coeffTop(1) = 0
//      }
//    }
    
    updatePMatrix = true
    updateMVPMatrix = true
    updateInverseMVPMatrix = true
    
    frustumDirty = true
  }
  
  def onViewportChange = 
    viewportDirty = true
  
  /**
   * Updates the values of the world planes associated with this camera.
   */
  def onFrameChange = {
//    val dirDotLocation = direction.dot(location)
//    val planeNormal = new Vec3
//    
//    // left plane
//    planeNormal.x = left.x * coeffLeft(0)
//    planeNormal.y = left.y * coeffLeft(0)
//    planeNormal.z = left.z * coeffLeft(0)
//    planeNormal += (direction.x * coeffLeft(1), 
//                    direction.y * coeffLeft(1), 
//                    direction.z * coeffLeft(1))
//    
//    worldPlane[LEFTPLANE].setNormal(planeNormal);
//    worldPlane[LEFTPLANE].setConstant(location.dot(planeNormal))
    
	updateMVMatrix = true
	updateMVPMatrix = true
	updateInverseMVPMatrix = true
        
    frameDirty = true
  }
  
  /**
   * Forces all aspect of the camera to be updated from internal values, and sets all dirty flags to true so that the
   * next render() call will fully set this camera to the render context.
   */
  def update {
    onDepthRangeChange
    onFrustumChange
    onViewportChange
    onFrameChange
  }
  
  def render {
    if(depthRangeDirty) {
      applyDepthRange
      depthRangeDirty = false
    }
    
    if(frustumDirty) {
      applyProjectionMatrix
      frustumDirty = false
    }
    
    if(viewportDirty) {
      applyViewport
      viewportDirty = false
    }
    
    if(frameDirty) {
      applyModelViewMatrix
      frameDirty = false
    }
  }
  
  // -- Perform Changes -------------------------------------------------------- 
  protected def applyDepthRange {
    gl.glDepthRange(depthRangeNear, depthRangeFar)
  }
  
  /** Apply the camera's projection matrix */
  def applyProjectionMatrix {
    tempMatrixBuffer.clear
    projection.put(tempMatrixBuffer)
    tempMatrixBuffer.rewind
    gl.glMatrixMode(GL.GL_PROJECTION)
    gl.glLoadMatrixf(tempMatrixBuffer)
    // info("applyProjectionMatrix", projection)
  }
  
  /** Apply the camera's viewport */
  protected def applyViewport {
    val x = (viewportLeft * width).asInstanceOf[Int]
    val y = (viewportBottom * height).asInstanceOf[Int]
    val w = ((viewportRight - viewportLeft) * width).asInstanceOf[Int]
    val h = ((viewportTop - viewportBottom) * height).asInstanceOf[Int]    
    gl.glViewport(x,y,w,h)
    // info("applyViewport", x,y,w,h)
  }
  
  /** Apply the camera's modelview matrix */ 
  protected def applyModelViewMatrix {
    tempMatrixBuffer.clear
    modelViewMatrix.put(tempMatrixBuffer)
    tempMatrixBuffer.rewind
    gl.glMatrixMode(GL.GL_MODELVIEW)
    gl.glLoadMatrixf(tempMatrixBuffer)
    // info("applyModelViewMatrix", modelViewMatrix)
  } 
  
  // -- Getters ----------------------------------------------------------------
  /** @return this cameras projection matrix (updated if necessary) */
  def projection = {
    if(updatePMatrix) {
      updateProjectionMatrix
      updatePMatrix = false
    }
    _projection
  }
  
  /** @return this cameras model view matrix (updated if necessary) */
  def modelViewMatrix = {
    if(updateMVMatrix) {
      updateModelViewMatrix
      updateMVMatrix = false
    }
    _modelView
  } 
  
  // -- Updater ----------------------------------------------------------------
  /** 
   * Updates the value of our projection matrix.
   */
  protected def updateProjectionMatrix {
	if (parallelProjection) {
	  _projection.identity
	  _projection(0, 0) = 2f / (frustumRight - frustumLeft)
	  _projection(1, 1) = 2f / (frustumBottom - frustumTop)
	  _projection(2, 2) = -2f / (frustumFar - frustumNear)
	  _projection(3, 3) = 1f	  
	  _projection(3, 0) = -(frustumRight + frustumLeft) / (frustumRight - frustumLeft)
	  _projection(3, 1) = -(frustumBottom + frustumTop) / (frustumBottom - frustumTop)
	  _projection(3, 2) = -(frustumFar + frustumNear) / (frustumFar - frustumNear)
   
	} else {
	  _projection.identity
	  _projection(0, 0) = (2f * frustumNear) / (frustumRight - frustumLeft)
	  _projection(1, 1) = (2f * frustumNear) / (frustumTop - frustumBottom)
	  _projection(2, 0) = (frustumRight + frustumLeft) / (frustumRight - frustumLeft)
	  _projection(2, 1) = (frustumTop + frustumBottom) / (frustumTop - frustumBottom)
	  _projection(2, 2) = -(frustumFar + frustumNear) / (frustumFar - frustumNear)
	  _projection(2, 3) = -1f
	  _projection(3, 2) = -(2f * frustumFar * frustumNear) / (frustumFar - frustumNear)
	  _projection(3, 3) = -0f
	}
  }
  
  protected def updateModelViewMatrix {
    _modelView.identity
    _modelView(0, 0) = left.x
    _modelView(1, 0) = left.y
    _modelView(2, 0) = left.z
    
    _modelView(0, 1) = up.x
    _modelView(1, 1) = up.y
    _modelView(2, 1) = up.z

    _modelView(0, 2) = -direction.x
    _modelView(1, 2) = -direction.y
    _modelView(2, 2) = -direction.z
    
    _transMatrix.identity
    _transMatrix(3, 0) = -location.x
    _transMatrix(3, 1) = -location.y
    _transMatrix(3, 2) = -location.z
    
    _transMatrix *= _modelView
    _modelView := _transMatrix
  }
  
  // -- Setters ----------------------------------------------------------------
  def resize(width:Int, height:Int) {
    this.width = width
    this.height = height
    onViewportChange
  }
  
  def frustum(fovY:Float, aspect:Float, near:Float, far:Float) {
    var h =	tan(fovY * .5f) * near
    var w = h * aspect
    frustumLeft = -w
    frustumRight = w
    frustumBottom = -h
    frustumTop = h
    frustumNear = near
    frustumFar = far
    this.fovY = fovY
    onFrustumChange
  }
  
  def frustum(left:Float, right:Float, bottom:Float, top:Float, near:Float, far:Float) {
    frustumLeft = left
    frustumRight = right
    frustumBottom = bottom
    frustumTop = top
    frustumNear = near
    frustumFar = far
    onFrustumChange
  }
  
  def frame(eyeX:Float, eyeY:Float, eyeZ:Float, 
            centerX:Float, centerY:Float, centerZ:Float, 
            upX:Float, upY:Float, upZ:Float) {
    location.set(eyeX, eyeY, eyeZ)
    up.set(upX, upY, upZ).normalize
    direction.set(centerX - eyeX, centerY - eyeY, centerZ - eyeZ).normalize
    onFrameChange
  }
  
  /** fov in degrees, width and height of the viewport */
  def init(fovY:Float, width:Int, height:Int) {
    var x = 0f
    var y = 0f
    var z = (height * .5f) / tan(fovY * 1f)
    var near = z / 10f
    var far = z * 10f
    var aspect = width/ height.asInstanceOf[Float]
    
    resize(width, height)
    frustum(fovY, aspect, near, far)
    frame(0,0,1, 
    	  0,0,0,
    	  0,-1,0)
    
//    var x = width * .5f
//    var y = height * .5f
//    var z = y / tan(fovY * .5f)    
//    frame(x,y,z, 
//    	  x,y,0,
//    	  0,-1,0)
  }
  
  /**
   * Sets this camera to the values of a given camera
   * @return itself
   */
  def :=(c:Camera) = {
    width = c.width
    height = c.height
    
    depthRangeNear = c.depthRangeNear
    depthRangeFar = c.depthRangeFar
    
    location := c.location
    left := c.left
    up := c.up
    direction := c.direction
    
    frustumNear = c.frustumNear
    frustumFar = c.frustumFar
    frustumLeft = c.frustumLeft
    frustumRight = c.frustumRight
    frustumTop = c.frustumTop
    frustumBottom = c.frustumBottom
    parallelProjection = c.parallelProjection
    fovY = c.fovY
    
    update
    
    this
  }
}
