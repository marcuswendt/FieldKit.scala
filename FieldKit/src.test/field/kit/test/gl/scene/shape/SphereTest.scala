/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 07, 2009 */
package field.kit.test.gl.scene.shape

object SphereTest extends test.Sketch {
  import kit.math.Common._
  import kit.gl.scene._
  import kit.gl.scene.shape._
  
  var scene:Group = _
  var s1:Sphere = _
  var s2:Sphere = _
  var t = 0f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, {
    import processing.core.PConstants
    hint(PConstants.ENABLE_DEPTH_SORT)
    
    scene = new Group("main scene")
    scene.translation := (hwidth, hheight, 0)
    
    s1 = Sphere("Red", 264f, 64)
    s1.translation := (-hwidth/2f, 0, 0)
    s1.colour := new Colour(1f, 0f, 0f, 0.5f)
    scene += s1
    
    s2 = Sphere("Green", 64f, 64)
    s2.translation := (hwidth/2f, 0, 0)
    s2.colour := new Colour(0f, 1f, 0f, 0.5f)
    scene += s2
  })
  
  def render {
    import javax.media.opengl.GL

    // update
    t += 0.01f
    val r = 200f
    //s.translation := (hwidth + sin(t) * r, hheight, -250f + cos(t) * r)
    scene.rotation.x = mouseY/ height.toFloat * TWO_PI * 100f
    scene.rotation.y = mouseX/ width.toFloat * TWO_PI * 100f
    
    // render
    background(64)
    
//    stroke(255)
//    strokeWeight(2)
//    val numLines = 6
//    for(i <- 0 until numLines) {
//      val x = i/numLines.toFloat * width
//      line(x, 0, x, height)
//    }
//    noStroke
    
   	val numBoxes = 3
	for(i <- 0 until numBoxes) {
	  for(j <- 0 until numBoxes) {
	    val x = i/ numBoxes.toFloat
        val y = j/ numBoxes.toFloat
        
	    fill(x * 128 + y * 128, 128)
     
        if(x == 0.2f && y == .2f)
          fill(255,0,255)
     
        if(x == 0.4f && y == .4f)
          fill(255,255,0)
        
        if(x == .5f && y == .5f)
          fill(255,0,0)
        
        if(x == 0f && y == 0f)
          fill(255,0,255)
        
        if(x == 0.9f && y == 0f)
          fill(0,255,255)
        
        if(x == 0.9f && y == 0.9f)
          fill(0,255,0)
        
        rect(x * width, y * height,
             width/ numBoxes.toFloat, 
             height/ numBoxes.toFloat)
	  }
	}
  
 	stroke(255)
 	strokeWeight(10)
 	line(0.33f * width, 0, 0.33f * width, height)
  
 	stroke(255, 0,0)
 	line(0.66f * width, 0, 0.66f * width, height)
  
 	stroke(255, 255,0)
 	line(0, 0.33f * height, width, 0.33f * height)
  
 	stroke(0, 255, 255)
 	line(0, 0.66f * height, width, 0.66f * height)
    noStroke
    
//    beginGL
//    gl.glEnable(GL.GL_LIGHTING)
//    gl.glEnable(GL.GL_LIGHT0)
//    scene.render
//    endGL
  }
  
  override def keyPressed {
    key match {
      case 'n' => rec.tiler.next
      case _ =>
    }
  }
}
