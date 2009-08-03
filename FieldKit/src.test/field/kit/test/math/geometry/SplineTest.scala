/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 16, 2009 */
package field.kit.test.math.geometry

/** test for the spline math class */
object SplineTest extends test.Sketch {
  import kit.math.geometry._
  import kit.math._
  
  var s = new Spline 
  
  init(1024,768)
  
  def render {
    background(235)
    
    // draw spline vertices
    noStroke
    fill(255,0,0)
    val v = new Vec3
    for(i <- 0 until s.size) {
      s.vertex(i,v)
      rect(v.x, v.y, 6, 6)
    }
    
    // draw spline points
    fill(0)
    val p = new Vec3
    val numPoints = 100
    for(i <- 0 to numPoints) {
      s.point(i / numPoints.asInstanceOf[Float],p)
      rect(p.x, p.y, 3f, 3f)
    }
  }
  
  override def mousePressed {
    info("adding point @", mouseX, mouseY)
    s += (mouseX, mouseY, 0)
  }
  
  override def keyPressed {
    key match {
      case ' ' => s.clear
      case _ =>
    }
  }
}
