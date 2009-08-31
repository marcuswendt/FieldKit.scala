/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 25, 2009 */
package field.kit.gl.render

/**
 * Construct a new Camera with the given frame width and height.
 * 
 * The code is based on Ardor3Ds <code>Camera</code> and the 
 * camera system in Processings <code>PGraphics</code> classes.
 * 
 * Compared to Ardor3D some of the more game specific features 
 * (e.g. frustum culling planes) have been removed.
 * 
 * For better compability with Processing this classes initializes to the same 
 * default projection and modelview matrices as PGraphics3D.
 * 
 * Also to provide more intuitive camera control methods some ideas from Kristian Linn Damkjers 
 * Obsessive Camera Direction Processing plugin have been implemented. 
 * 
 * @see http://ardorlabs.trac.cvsdude.com/Ardor3Dv1/browser/trunk/ardor3d-core/src/main/java/com/ardor3d/renderer/Camera.java
 * @see http://dev.processing.org/source/index.cgi/trunk/processing/core/src/processing/core/PGraphics3D.java?rev=5653&view=markup
 * @see http://www.gdsstudios.com/processing/libraries/ocd/
 */
class Camera(var width:Int, var height:Int) extends Renderable with Logger {
  import math._
  import math.Common._
  import util.Buffer
  import javax.media.opengl.GL
  import javax.media.opengl.glu.GLU
  
  // -- Depth Range ------------------------------------------------------------
  /** The near range for mapping depth values from normalized device coordinates to window coordinates. */
  protected var depthRangeNear = 0f
  
  /** The far range for mapping depth values from normalized device coordinates to window coordinates. */
  protected var depthRangeFar = 1f
  
  // -- Frame ------------------------------------------------------------------
  /** Camera's location */
  protected val _location = Vec3(0,0,0)
  
  /** Direction of camera's 'left' */
  protected val _left = Vec3(1,0,0)
  
  /** Direction of 'up' for camera. */
  protected val _up = Vec3(0,1,0)
  
  /** Direction the camera is facing. */
  protected val _direction = Vec3(0,0,1)
  
  // -- Frustum ----------------------------------------------------------------
  /** Distance from camera to near frustum plane. */
  protected var frustumNear = 1f
  
  /** Distance from camera to far frustum plane. */
  protected var frustumFar = 2f
  
  /** Distance from camera to left frustum plane. */
  protected var frustumLeft = -0.5f
  
  /** Distance from camera to right frustum plane. */
  protected var frustumRight = 0.5f
  
  /** Distance from camera to top frustum plane. */
  protected var frustumTop = 0.5f
  
  /** Distance from camera to bottom frustum plane. */
  protected var frustumBottom = -0.5f
  
  // -- Viewport ---------------------------------------------------------------
  /** Percent value on display where horizontal viewing starts for this camera. */
  protected var viewportLeft = 0f
  
  /** Percent value on display where horizontal viewing ends for this camera. */
  protected var viewportRight = 1f
  
  /** Percent value on display where vertical viewing ends for this camera. */
  protected var viewportTop = 1f
  
  /** Percent value on display where vertical viewing begins for this camera. */
  protected var viewportBottom = 0f
  
  /** Defines wether this view is a parallel or perspective projection */
  protected var parallelProjection = false
  
  /** Convenience store for fovY. Only set during setFrustumPerspective and never used. */
  var fovY = Float.NaN
  
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
  
  // -- Initialisation ---------------------------------------------------------
  init(PI / 3f, width, height)
  
  /**
   * @param fovY field of view in radians
   * @param width of the viewport
   * @param height of the viewport
   */
  def init(fovY:Float, width:Int, height:Int) {
    var x = width / 2f
    var y = height / 2f
    var z = y / tan(fovY * 0.5f)
    var near = z / 10f
    var far = z * 10f
    var aspect = width/ height.toFloat
    
    resize(width, height)
    perspective(fovY, aspect, near, far)
    frame(x,y,z, 
    	  x,y,0,
    	  0,1,0)
//  	frame(0,0,z, 
//    	  0,0,0,
//    	  0,1,0)
  }
  
  def resize(width:Int, height:Int) {
    this.width = width
    this.height = height
    onViewportChange
  }
  
  def perspective(fovY:Float, aspect:Float, zNear:Float, zFar:Float) {
    val ymax = zNear * tan(fovY / 2)
    val ymin = -ymax
    val xmin = ymin * aspect
    val xmax = ymax * aspect
    this.fovY = fovY
    frustum(xmin, xmax, ymin, ymax, zNear, zFar)
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
    _location.set(eyeX, eyeY, eyeZ)
    _up.set(upX, upY, upZ).normalize
    _direction.set(centerX - eyeX, centerY - eyeY, centerZ - eyeZ).normalize
    onFrameChange
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
    
    _location := c._location
    _left := c._left
    _up := c._up
    _direction := c._direction
    
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
  
  protected def onDepthRangeChange = 
    depthRangeDirty = true
  
  /**
   * Updates internal frustum coefficient values to reflect the current frustum plane values.
   */
  protected def onFrustumChange = {
    updatePMatrix = true
    updateMVPMatrix = true
    updateInverseMVPMatrix = true
    
    frustumDirty = true
  }
  
  protected def onViewportChange = 
    viewportDirty = true
  
  /**
   * Updates the values of the world planes associated with this camera.
   */
  protected def onFrameChange = {    
	updateMVMatrix = true
	updateMVPMatrix = true
	updateInverseMVPMatrix = true
        
    frameDirty = true
  }
  
  // -- Render -----------------------------------------------------------------
  /**
   * Applies this cameras matrices and settings to the GL view 
   */
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
  protected def applyProjectionMatrix {
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
    modelView.put(tempMatrixBuffer)
    tempMatrixBuffer.rewind
    gl.glMatrixMode(GL.GL_MODELVIEW)
    gl.glLoadMatrixf(tempMatrixBuffer)
    // info("applyModelViewMatrix", modelViewMatrix)
  } 
  
  // -- Getters ----------------------------------------------------------------
  def location = _location
  
  def location_=(v:Vec3) {
    location := v
    onFrameChange
  }
  
  def up = _up
  
  def up_=(v:Vec3) {
    _up := v
    onFrameChange
  }
  
  def direction = _direction
  
  def direction_=(v:Vec3) {
    direction := v
    onFrameChange
  }
  
  /** @return this cameras projection matrix (updated if necessary) */
  def projection = {
    if(updatePMatrix) {
      updateProjectionMatrix
      updatePMatrix = false
    }
    _projection
  }
  
  /** @return this cameras model view matrix (updated if necessary) */
  def modelView = {
    if(updateMVMatrix) {
      updateModelViewMatrix
      updateMVMatrix = false
    }
    _modelView
  } 
  
  // -- Updater ----------------------------------------------------------------
  /** 
   * Updates the projection matrix from the current frustum
   */
  private def updateProjectionMatrix {
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
  
  /** 
   * Updates the model view matrix from the current left, up, direction and location vectors
   */
  private def updateModelViewMatrix {
    _modelView.identity
    _modelView(0, 0) = _left.x
    _modelView(1, 0) = _left.y
    _modelView(2, 0) = _left.z
    
    _modelView(0, 1) = _up.x
    _modelView(1, 1) = _up.y
    _modelView(2, 1) = _up.z

    _modelView(0, 2) = -_direction.x
    _modelView(1, 2) = -_direction.y
    _modelView(2, 2) = -_direction.z
    
    _transMatrix.identity
    _transMatrix(3, 0) = -_location.x
    _transMatrix(3, 1) = _location.y
    _transMatrix(3, 2) = -_location.z
    
    _transMatrix *= _modelView
    _modelView := _transMatrix
  }
}
