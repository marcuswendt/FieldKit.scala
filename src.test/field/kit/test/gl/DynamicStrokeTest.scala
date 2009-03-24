/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.test.gl

import field.kit._

/** test to make sure the DynamicStroke2D is working properly */
object DynamicStrokeTest extends Sketch {
  import field.kit.gl.scene.shape._
  
  var stroke = new DynamicStroke2D("test", 1000)
  var time = 0f
  
  init(1024, 768)
  
  def render {
    // update
    if(mousePressed) {
      info("point", mouseX, mouseY, "length", stroke.length)
      //Math.sin(time).asInstanceOf[Float]
      stroke += (mouseX, mouseY, 5f)
//      stroke.update
    }
    
    // render
    time += 0.01f
    
    background(32)
    noStroke
    
    // draw points
    fill(255)
    for(i <- 0 until stroke.length) {
      val index = i*2
      rect( stroke.points.get(index), 
            stroke.points.get(index+1), 
            2, 2)
    }
    
    // draw outline
    for(i <- 0 until stroke.length) {
      var index = stroke.outlineIndex(DynamicStroke2D.Side.LEFT, i)
      fill(255, 0, 0)
      rect( stroke.outline.get(index), 
            stroke.outline.get(index+1), 
            2, 2)
      
      index = stroke.outlineIndex(DynamicStroke2D.Side.RIGHT, i)
      fill(0, 255, 0)
      rect( stroke.outline.get(index), 
            stroke.outline.get(index+1), 
            4, 4)
    } 
    
    // draw mouse
    fill(255, 0, 255)
    rect(mouseX, mouseY, 3, 3)
    
    // draw stroke
    pgl.beginGL
    stroke.render
    pgl.endGL
  }
  
  override def keyPressed {
    key match {
      case ' ' => stroke.clear
      case _ =>
    }
  }
}
