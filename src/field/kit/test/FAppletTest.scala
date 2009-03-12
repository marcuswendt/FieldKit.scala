/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 12, 2009 */
package field.kit.test

import field.kit.p5.FApplet

object FAppletTest extends FApplet {
  info("FAppletTest created")
  
  size(1024,768,OPENGL)
 
  override def draw {
    background(0)
    
    for(i <- 0 until height) {
      stroke(i % 255, i % 128, 0)
      line(0, i, width, i)
    }
  }
}