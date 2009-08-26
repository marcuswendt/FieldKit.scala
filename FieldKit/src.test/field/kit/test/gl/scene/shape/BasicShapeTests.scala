/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.test.gl.scene.shape

import field.kit._

abstract class ShapeTest extends Sketch {
  import field.kit.gl.scene.Mesh
  
  var s:Mesh = _
  var geoWidth = 500f
  var geoHeight = 500f
  
  init(1024,768)
  
  def render {
    background(0)
    
    // TODO improve rendering with points, need to figure out renderstates first
    beginGL
    s.translation.x = width/2f - geoWidth / 2f
    s.translation.y = height/2f - geoHeight / 2f
    s.render
    endGL
  }
}

object PlaneTest extends ShapeTest {
  import field.kit.gl.scene.shape.Plane
  s = new Plane("planeoid", geoWidth, geoHeight)
  s.randomizeColours
  
  override def keyPressed {
    def reinit(cols:Int, rows:Int) {
      info("reinitializing cols: "+ cols +" rows: "+ rows)
      val p = s.asInstanceOf[Plane]
      p.init(cols,rows)
      p.randomizeColours
    }
    
    key match {
      case '0' => reinit(2,2)
      case '1' => reinit(3,3)
      case '2' => reinit(4,4)
      case '3' => reinit(10,10)
      case '4' => reinit(10,3)
      case '5' => reinit(-1, -1)
      case '6' => reinit(100, 100)
      case '7' => reinit(333, 128)
      case '8' => reinit(200, 15)
      case '9' => reinit(1000, 1000)
      case _ =>
    }
  }
}

object QuadTest extends ShapeTest {
  import field.kit.gl.scene.shape.Quad
  s = Quad(geoWidth, geoHeight)
  s.solidColour(Colour.GREEN)
  val q = s.asInstanceOf[Quad]
  q.mode = Quad.TOP_LEFT
  q.init(geoWidth, geoHeight)
}
