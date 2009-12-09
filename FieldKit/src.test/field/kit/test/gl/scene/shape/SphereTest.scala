/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 07, 2009 */
package field.kit.test.gl.scene.shape

import field.kit.test.Sketch

object SphereTest extends Sketch {
  import field.kit.colour._
  import field.kit.math.Common._
  import field.kit.math._
  import field.kit.gl.scene._
  import field.kit.gl.scene.shape._
  
  var scene:Group = _
  var s1:Sphere = _
  var s2:Sphere = _
  var t = 0f
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA,  {
    import processing.core.PConstants
    hint(PConstants.ENABLE_DEPTH_SORT)
    
    scene = new Group("main scene")
    scene.translation := Vec3(hwidth, hheight, 0)
    
    s1 = Sphere("Red", 264f, 64)
    s1.translation := Vec3(-hwidth/2f, 0, 0)
    s1.colour := new Colour(1f, 0f, 0f, 0.5f)
    scene += s1
    
    s2 = Sphere("Green", 64f, 64)
    s2.translation := Vec3(hwidth/2f, 0, 0)
    s2.colour := new Colour(0f, 1f, 0f, 0.5f)
    scene += s2
  })
  
  def render {
    import javax.media.opengl.GL

    // update
    t += 0.01f
    val r = 200f
    scene.rotation.x = mouseY/ height.toFloat * TWO_PI * 100f
    scene.rotation.y = mouseX/ width.toFloat * TWO_PI * 100f
    
    // render
    background(64)
    
    beginGL
    gl.glEnable(GL.GL_LIGHTING)
    gl.glEnable(GL.GL_LIGHT0)
    scene.render
    endGL
  }
}
