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
 * Camera control class
 */
class Camera(var width:Float, var height:Float) extends Renderable with Logger {
  import math._
  import math.FMath._
  import javax.media.opengl.GL
  import javax.media.opengl.glu.GLU
  
  /** The near range for mapping depth values from normalized device coordinates to window coordinates. */
  protected var depthRangeNear = 0f
  
  /** The far range for mapping depth values from normalized device coordinates to window coordinates. */
  protected var depthRangeFar = 1f
  
  /** Camera's location */
  val location = new Vec3(0,0,0)
  
  /** Direction of camera's 'left' */
  val left = new Vec3(1,0,0)
  
  /** Direction of 'up' for camera. */
  val up = new Vec3(0,1,0)
  
  /** Direction the camera is facing. */
  val direction = new Vec3(0,0,-1)
  
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
  
  /** The field of view in radians */
  protected var fov = 60 * DEG_TO_RAD
  
  // -- Internal Variables -----------------------------------------------------
  protected var depthRangeDirty = false
  protected var viewportDirty = false
  protected var frustumDirty = false
  protected var frameDirty = false
  
  
  // -- Initialisation ---------------------------------------------------------
  init(width, height)
  
  def init(width:Float, height:Float) {
    fine("init", width, height)
    
    this.width = width
    this.height = height
    
    // update frustum
    val eyeX = width * .5f
    val eyeY = height * .5f
    val dist = eyeY / tan(fov * .5f)
    val near = dist / 10f
    val far = dist * 10f
    
    frustum(fov, width/ height, near, far)
    
    // update frame
    // location := (eyeX, eyeY, dist)
    
    // mark everything as dirty
    update
  }
  
  def onDepthRangeChange = depthRangeDirty = true
  
  def onViewportChange = viewportDirty = true
  
  def onFrustumChange = frustumDirty = true
  
  def onFrameChange = frameDirty = true
  
  def update {
    onDepthRangeChange
    onViewportChange
    onFrustumChange
    onFrameChange
  }
  
  def render {
    if(depthRangeDirty) {
      doDepthRangeChange
      depthRangeDirty = false
    }
    
    if(frustumDirty) {
      doFrustumChange
      frustumDirty = false
    }
    
    if(viewportDirty) {
      doViewportChange
      viewportDirty = false
    }
    
    if(frameDirty) {
      doFrameChange
      frameDirty = false
    }
  }
  
  // -- Perform Changes --------------------------------------------------------
  protected def doDepthRangeChange {
    gl.glDepthRange(depthRangeNear, depthRangeFar)
  }
  
  protected def doFrustumChange {
    gl.glMatrixMode(GL.GL_PROJECTION)
    gl.glLoadIdentity
      
    if(parallelProjection)
      gl.glOrtho(frustumLeft, frustumRight, frustumBottom, frustumTop, frustumNear, frustumFar)
    else
      gl.glFrustum(frustumLeft, frustumRight, frustumBottom, frustumTop, frustumNear, frustumFar)
  }
  
  protected def doViewportChange {
    val x = (viewportLeft * width).asInstanceOf[Int]
    val y = (viewportBottom * height).asInstanceOf[Int]
    val w = ((viewportRight - viewportLeft) * width).asInstanceOf[Int]
    val h = ((viewportTop - viewportBottom) * height).asInstanceOf[Int]
    
    gl.glViewport(x,y,w,h)
  }
  
  protected def doFrameChange {
    Camera.glu.gluLookAt(
      location.x.asInstanceOf[Double], 
      location.y.asInstanceOf[Double], 
      location.z.asInstanceOf[Double], 
      
      (location.x + direction.x).asInstanceOf[Double], 
      (location.y + direction.y).asInstanceOf[Double], 
      (location.z + direction.z).asInstanceOf[Double],
      
      up.x.asInstanceOf[Double], 
      up.y.asInstanceOf[Double], 
      up.z.asInstanceOf[Double]
    )
//    gl.glTranslatef(0, -height, 0)
  }
  
  // -- Setters ----------------------------------------------------------------
  def frame(eyeX:Float, eyeY:Float, eyeZ:Float, 
            centerX:Float, centerY:Float, centerZ:Float, 
            upX:Float, upY:Float, upZ:Float) {
    location.set(eyeX, eyeY, eyeZ)
    up.set(upX, upY, upZ).normalize
    direction.set(centerX - eyeX, centerY - eyeY, centerZ - eyeZ).normalize
    onFrameChange
  }
  
  def frustum(fov:Float, aspect:Float, near:Float, far:Float) {
    var h =	tan(fov * .5f) * near
    var w = h * aspect
    frustumLeft = -w
    frustumRight = w
    frustumBottom = -h
    frustumTop = h
    frustumNear = near
    frustumFar = far
    this.fov = fov
    onFrustumChange
  }
}
