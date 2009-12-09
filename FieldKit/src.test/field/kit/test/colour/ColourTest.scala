package field.kit.test.colour

import field.kit.Colour
import field.kit.test.Sketch

object ColourTest extends Sketch {
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    background(16)
    
    val red = Colour(1f,0,0)
    fill(red.toInt)
    rect(10, 10, 100, 100)
    
    val redVar = Colour(red).shiftHue(0.125f)
    fill(redVar.toInt)
    rect(120, 10, 100, 100)
    
    val var2 = Colour(redVar).shiftSaturation(-0.75f)
//    val var2 = Colour(redVar).shiftSaturation(0)
    fill(var2.toInt)
    rect(230, 10, 100, 100)
  }
}
