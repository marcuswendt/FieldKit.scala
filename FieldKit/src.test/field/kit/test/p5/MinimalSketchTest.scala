/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 08, 2009 */
package field.kit.test.p5

/** 
 * simplistic sketch test, just to make sure there are no memory leaks in Sketch
 * needs to be started with: -Dcom.sun.management.jmxremote=true
 */
object MinimalSketchTest extends field.kit.Sketch {
  init(1024,768)
  def render {
    background(0)
    
    // as soon as we draw something, p5 starts leaking...
//    var w = width/10
//    var h = height/10
//    for(x <- 0 until width/w) {
//    	for(y <- 0 until height/h) {
//    	  noFill
//    	  stroke(255)
//    	  rect(x * w, y *h, w,  h)
//    	}  
//    }
  }
}
