/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 07, 2009 */
package field.kit.test.gl.scene.shape

object SphereTest extends test.Sketch {
  import kit.gl.scene.shape._
  import kit.math.FMath._

  init {}
  
  var t = 0f
  
  val s = new Sphere("Sphere", 250f, 7)
  //s.init(250f, 12, 12)
  s.translation := (hwidth, hheight, 0)
  s.colour := new Colour(1f, 0f, 0f, 0.5f)
  
  def gl = pgl.gl
  
  def render {
    import javax.media.opengl.GL

    t += 0.01f
    val r = 200f
    //s.translation := (hwidth + sin(t) * r, hheight, -250f + cos(t) * r)
    
    background(64)
    beginGL
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glEnable(GL.GL_LIGHTING)
    //gl.glDisable(GL.GL_LIGHT0)
    gl.glEnable(GL.GL_LIGHT0)
    gl.glPointSize(5f)
//    gl.glEnable(GL.GL_LIGHT1)
//    gl.glEnable(GL.GL_LIGHT2)
    
//    val lightPos = Array((hwidth + sin(t) * r).asInstanceOf[Float],  
//    					 hheight.asInstanceOf[Float], 
//    					 (-250f + cos(t) * r).asInstanceOf[Float], 
//                         0f)
//    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos)
    
    s.rotation.x = mouseY/ height.asInstanceOf[Float] * TWO_PI * 100f
    s.rotation.y = mouseX/ width.asInstanceOf[Float] * TWO_PI * 100f
    s.render
    endGL
  }
}
