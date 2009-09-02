/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 29, 2009 */
package field.kit.gl.render

/**
 * Implements more intuitive camera manipulation methods
 */
class AdvancedCamera(width:Int, height:Int) extends Camera(width, height) {
  import math.Vec3
  import math.Common._
  
  protected var azimuth = 0f
  protected var elevation = 0f
  protected var roll = 0f
  protected var shotLength = 0f

  /** target position */
  protected val target = Vec3(_location.x, _location.y, 0)
  
  /** distance differences between camera and target */
  protected val delta = Vec3()
  
  protected val TOL = 0.00001f
  
  // -- Initialisation ---------------------------------------------------------
  updateDelta
  
  def :=(c:AdvancedCamera) = {
    super.:=(c)
    azimuth = c.azimuth
    elevation = c.elevation
    roll = c.roll
    shotLength = c.shotLength
    target := c.target
    delta := c.delta
    this
  }
  
  // -- Instantaneous Changes --------------------------------------------------
  import processing.opengl.PGraphicsOpenGL
  import processing.core.PApplet
  def feed(app:PApplet) {
    val g = app.g.asInstanceOf[PGraphicsOpenGL]
    g.perspective(fovY, width/ height.toFloat, frustumNear, frustumFar)
    g.camera(location.x, location.y, location.z,
             target.x, target.y, target.z,
             up.x, up.y, up.z)
  }
  
  def aim(target:Vec3) {
    target := target
    updateDelta
  }
  
  // -- Linear Movements -------------------------------------------------------
  /** Move the camera and target simultaneously along the camera's X axis */
  def truck(amount:Float) {
    val truck = delta.cross(up)
    truck.normalize
    truck *= amount
    location -= truck
    target -= truck
    onFrameChange
  }
  
  /** Move the camera and target simultaneously along the camera's Y axis */
  def boom(amount:Float) {
    val boom = up * amount
    location += boom
    target += boom
    onFrameChange
  }

  /** Move the camera and target along the view vector */
  def dolly(amount:Float) {
    val dolly = (delta * amount) / shotLength
    location += dolly
    target += dolly
    onFrameChange
  }
  
  // -- Arc Movements ----------------------------------------------------------
  /** Arc the camera over (under) a center of interest along a set azimuth*/
  def arc(elevationOffset:Float) {
    elevation = clamp(elevation + elevationOffset, TOL-HALF_PI, HALF_PI-TOL)
    updateCamera
  }
  
  /** Circle the camera around a center of interest at a set elevation*/
  def circle(azimuthOffset:Float) {
    azimuth = (azimuth + azimuthOffset + TWO_PI) % TWO_PI
    updateCamera
  }
  
  // -- Combinations -----------------------------------------------------------
  def tumble(azimuthOffset:Float, elevationOffset:Float) {
    elevation = clamp(elevation + elevationOffset, TOL-HALF_PI, HALF_PI-TOL)
    azimuth = (azimuth + azimuthOffset + TWO_PI) % TWO_PI
    updateCamera
  }
  
  /** Moves the camera and target simultaneously in the camera's X-Y plane */
  def track(offsetX:Float, offsetY:Float) {
    truck(offsetX)
    boom(offsetY)
  }
  
  // -- Helpers ----------------------------------------------------------------
  /** Update deltas and related information */
  protected def updateDelta {
    // Describe the new vector between the camera and the target
    delta.set(location) -= target 
    
    // Describe the new azimuth and elevation for the camera
    shotLength = delta.length
    azimuth = atan2(delta.x, delta.z)
    elevation = atan2(delta.y, sqrt(delta.z * delta.z + delta.x * delta.x))
    
    //info("updateDelta", "shotLength", shotLength, "azimuth", azimuth, "elevation", elevation)
    updateUp
  }
  
  /** Update camera and related information */
  protected def updateCamera {
    // Orbit to the new orientation while maintaining the shot distance.
    location.x = target.x + (shotLength * sin(HALF_PI + elevation) * sin(azimuth))
    location.y = target.y + (-shotLength * cos(HALF_PI + elevation))
    location.z = target.z + (shotLength * sin(HALF_PI + elevation) * cos(azimuth)) 
    updateUp
  }
  
  /** Update the up direction and related information */
  protected def updateUp {
    // Describe the new vector between the camera and the target
    delta.set(location) -= target
    
    // Calculate the new "up" vector for the camera
    up.x = -delta.x * delta.y
    up.y =  delta.z * delta.z + delta.x * delta.x
    up.z = -delta.z * delta.y
    up.normalize
    
    if(roll != 0f) {
      // Calculate the camera's X axis in world space
      delta.cross(up, direction)
      direction.normalize
      
      // perform the roll
      up.x = up.x * cos(roll) + direction.x * sin(roll)
      up.y = up.y * cos(roll) + direction.y * sin(roll)
      up.z = up.z * cos(roll) + direction.z * sin(roll)
    }
    
    onFrameChange
  }
  
  // -- Utilities --------------------------------------------------------------
  override def clone = {
    val c = new AdvancedCamera(width, height)
    c := this
    c
  }
  
  override def toString =
    "AdvancedCamera[azimuth:"+ azimuth +" elevation:"+ elevation +" roll:"+ roll +"\n"+
    "               target:"+ target +"\n"+
    "               location: "+ location +"]"
}
