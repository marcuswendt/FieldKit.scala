/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 08, 2009 */
package field.kit.test.p5

import field.kit.test.Sketch

/** 
 * quick test for the fullscreen mode switch
 */
object FullscreenSwitchTest extends Sketch {
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    import processing.core.PConstants._
    background(64)
    
    rectMode(CENTER)
    noFill
    stroke(255)
    rect(width/2, height/2, width/10f, height/10f)
  }
}
