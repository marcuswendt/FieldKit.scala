/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 26, 2009 */
package field.kit.test.gl.render

/**
 * Tests the Camera system built into every Sketch
 */
object CameraTest extends test.Sketch {
  import kit.gl.scene._
  import kit.util.Timer
  import kit.math._
  import kit.math.FMath._
  
  var scene:Group = _
  val timer = new Timer
  val camVelocity = new Vec3
  var useLights = false
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, {
    import kit.gl.scene.shape._
    
    import processing.core.PConstants
    hint(PConstants.ENABLE_DEPTH_SORT)
    
    scene = new Group("main scene")
    scene.translation := (hwidth, hheight, 0)
    
    // floating red box
    val red = Box("Red", 64f)
    red.colour := Colour.RED
    red.translation := (0,0,128)
    red.rotation := (QUARTER_PI, QUARTER_PI, 0)
    scene += red
        
    // ground plane    
    val plane = Quad("Ground", height, height)
    plane.colour := Colour.GREEN
//    plane.translation.x = 270
    scene += plane
  })
  
  def render {
    // update 
    val dt = timer.update
    
    val rotX = dt * 0.01f
    val rotY = dt * 0.01f
    scene("Red").rotation += (rotX, rotX, 0)
    
    scene.rotation.x = (mouseY)/ height.asInstanceOf[Float] * TWO_PI * 25f
    scene.rotation.z = (mouseX -hwidth)/ width.asInstanceOf[Float] * TWO_PI * 25f
    
    import processing.core.PConstants
    val speedUp = 2f
    if(keyPressed) {
	    keyCode match {
	      case PConstants.LEFT => 
	        camVelocity.x -= speedUp
	          
	      case PConstants.RIGHT => 
	        camVelocity.x += speedUp
	        
	      case PConstants.UP => 
	        camVelocity.z -= speedUp
	    
	      case PConstants.DOWN => 
	        camVelocity.z += speedUp
	
	      case _ =>
	    }
    }
    
    activeCamera.location += camVelocity
    activeCamera.update
//    activeCamera.render
    
    camVelocity *= 0.9f
    
    // render
    import javax.media.opengl.GL
    background(64)
    
//    pushMatrix
//    translate(hwidth, hheight, 0)
    noStroke
    fill(255)
    ellipse(hwidth, hheight, 100, 100)
//    popMatrix
    
    beginGL
    if(useLights) {
      gl.glEnable(GL.GL_LIGHTING)
      gl.glEnable(GL.GL_LIGHT0)
    } else {
      gl.glDisable(GL.GL_LIGHTING)
    }
    scene.render
    endGL
  }
  
  override def keyPressed {
    key match {
      case ' ' => useLights = !useLights
      case _ =>
    }
  }
}
