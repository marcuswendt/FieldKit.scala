/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 31, 2009 */
package field.kit.test.p5

object TilerTest extends test.Sketch {
  import kit.math.Common
  import kit.math.Common._
  
  var gridX = 2f
  var gridY = 2f
  
  var colours = new Array[Int](256)
  for(i <- 0 until colours.length)
    colours(i) = color(Common.random(255), Common.random(255), Common.random(255))
  
  val theFont = loadFont("Inconsolata-48.vlw")
   
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, {
    //rec.init(width * gridX.toInt, height * gridY.toInt)
  })
  
  def render {
    background(64)
    textFont(theFont)
    textSize(12)
    
    // render grid of boxes with labels on top
	for(i <- 0 until gridY.toInt) {
	  for(j <- 0 until gridX.toInt) {
	    val x = (j/ gridX) * width 
        val y = (i/ gridY) * height
        val w = width/ gridX
        val h = height/ gridY
        
        //info("drawing ", i, j, " => ", x, y)
        
        fill(colours((i*gridY + j).toInt))
        stroke(255)
        strokeWeight(10)
        rect(x, y, w, h)
        
        pushMatrix
        translate(0,0,10)
        fill(0)
        noStroke
        text("x"+ j +"y"+ i, 
             x + w * .5f, 
             y + h * .5f)
        popMatrix        
	  }
	} 
  }
  
  override def keyPressed {
    import processing.core.PConstants
    var needsRefresh = false
    keyCode match {
      case PConstants.LEFT => gridX -= 1; needsRefresh = true
      case PConstants.RIGHT => gridX += 1; needsRefresh = true
      case PConstants.UP => gridY += 1; needsRefresh = true
      case PConstants.DOWN => gridY -= 1; needsRefresh = true
      case _ =>
    }
    
    if(needsRefresh) {
      gridX = clamp(gridX, 1, 10)
      gridY = clamp(gridY, 1, 10)
    
      info("initializing grid", gridX, gridY)
      //rec.init(width * gridX.toInt, height * gridY.toInt)
    }
    
    key match {
      case _ =>
    }
  }
}
