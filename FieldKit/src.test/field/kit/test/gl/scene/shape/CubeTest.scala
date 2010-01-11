/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.test.gl.scene.shape

import field.kit.test.Sketch

object CubeTest extends Sketch {
  import field.kit.gl.scene.Mesh
  import field.kit.math.Common._
  
  var geoWidth = 400f
  var geoHeight = geoWidth
  var geoDepth = geoWidth

  var s:Mesh = _
  initMesh(4,4,4)
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    background(240)
    
    // TODO improve rendering with points, need to figure out renderstates first
    beginGL
    //s.rotation.y = TWO_PI * (mouseX/width.toFloat)
    s.rotation.y = 360f * (mouseX/width.toFloat)
    s.rotation.x = 360f * (1f- mouseY/height.toFloat)
    s.translation.x = width/2f
    s.translation.y = height/2f
    s.render
    endGL
  }
  
  def initMesh(d:Int):Unit = initMesh(d,d,d)
  
  def initMesh(r:Int, c:Int, l:Int) {
    import field.kit.gl.scene.shape._
//    s = new Cube("test", r,c,l, geoWidth,geoHeight,geoDepth)
//    s.solidColour(Colour.BLACK)
    s.randomizeColours
  }
  
  
  override def keyPressed {
    key match {
      case '0' => initMesh(0)
      case '1' => initMesh(1)
      case '2' => initMesh(2)
      case '3' => initMesh(3,10,2)
      case '4' => initMesh(4)
      case '5' => initMesh(5)
      case '6' => initMesh(6)
      case '7' => initMesh(7)
      case '8' => initMesh(40)
      case '9' => initMesh(96)
      case _ =>
    }
  }
}