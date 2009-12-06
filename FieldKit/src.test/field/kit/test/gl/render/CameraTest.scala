/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 26, 2009 */
package field.kit.test.gl.render

import field.kit.test.Sketch

/**
 * Tests the Camera system built into every Sketch
 */
object CameraTest extends Sketch {
	import processing.core.PConstants
	
	import field.kit._
	import field.kit.gl.scene._
	import field.kit.util.Timer
	import field.kit.math._
	import field.kit.math.Common._
	import field.kit.gl.scene.shape._
  
	var scene:Group = _
	val timer = new Timer
	val camVelocity = new Vec3
	var useLights = false
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    hint(PConstants.ENABLE_DEPTH_SORT)
    
    scene = new Group("main scene")
    scene.translation := Vec3(hwidth, hheight, 0)
    
    // floating red box
    val red = Box("Red", 64f)
    red.colour := Colour.RED
    red.translation := Vec3(0,0,128)
    red.rotation := Vec3(QUARTER_PI, QUARTER_PI, 0)
    scene += red
    
    // floating blue sphere
    val blue = Sphere("Blue", new Vec3(256f, 64f, 128), 32f)
    blue.colour := Colour.BLUE
    scene += blue
        
    // ground plane    
    val plane = Quad("Ground", height, height)
    plane.colour := Colour.GREEN
    plane.translation.z = -10f
    scene += plane
  })
  
  def render {
    // update 
    val dt = timer.update
    
    val rotX = dt * 0.01f
    val rotY = dt * 0.01f
    scene("Red").rotation += Vec3(rotX, rotY, 0)
    scene("Blue").rotation += Vec3(0, 0, rotX * 3f)
    
//    scene.rotation.x = (mouseY)/ height.toFloat * TWO_PI * 25f
//    scene.rotation.z = (mouseX -hwidth)/ width.toFloat * TWO_PI * 25f
    
    import processing.core.PConstants
    val speedUp = 2f
    if(keyPressed) {
	    keyCode match {
	      case PConstants.LEFT => 
	        camVelocity.x += speedUp
	          
	      case PConstants.RIGHT => 
	        camVelocity.x -= speedUp
	        
	      case PConstants.UP => 
	        camVelocity.y -= speedUp
	    
	      case PConstants.DOWN => 
	        camVelocity.y += speedUp
	
	      case _ =>
	    }
     
	    key match {
	      case 'a' => 
	        camVelocity.x += speedUp
         
	      case 'd' => 
	        camVelocity.x -= speedUp
         
	      case 'w' => 
	        camVelocity.z -= speedUp
	    
	      case 's' => 
	        camVelocity.z += speedUp
	      case _ =>
	    }
    }
    
    activeCamera.truck(camVelocity.x)
    activeCamera.boom(camVelocity.y)
    activeCamera.dolly(camVelocity.z)
    
    camVelocity *= 0.9f
    
    // render
    import javax.media.opengl.GL
    background(64)
    
    fill(0,64,32, 128);
    rect(0,0, width, height);
    
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
  
  override def mouseMoved {
    // activeCamera.arc( (mouseY - pmouseY) * DEG_TO_RAD)
    // activeCamera.circle( (mouseX - pmouseX) * DEG_TO_RAD * .5f )
    activeCamera.tumble(radian(mouseX - pmouseX), radian(mouseY - pmouseY));
  }
  
  def radian(d:Float) = DEG_TO_RAD * d
  
  override def keyPressed {
    key match {
      case ' ' => useLights = !useLights
      //case 'a' => activeCamera.aim( scene("Blue").translation )
      case _ =>
    }
  }
}
