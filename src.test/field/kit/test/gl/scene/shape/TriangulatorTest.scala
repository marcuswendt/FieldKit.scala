/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.test.gl.scene.shape

import field.kit._

/** tests the simple/ fast triangulator trait */
object TriangulatorTest extends Sketch {
  import field.kit.gl.scene._
  import field.kit.math._
  
  var p = new Polygon(100)
  var geoWidth = 500f
  var geoHeight = 500f
  
  init(1024,768)
  
  def render {
    background(10)
    
    // TODO improve rendering with points, need to figure out renderstates first
    // draw polygon
    beginGL
    p.render
    endGL
    
    // draw spline vertices
    noStroke
    fill(255,0,0)
    val v = new Vec3
    for(i <- 0 until p.curve.size) {
      p.curve.vertex(i,v)
      rect(v.x, v.y, 6, 6)
    }
  }
  
  override def mousePressed {
    info("adding point @", mouseX, mouseY)
    p += (mouseX, mouseY, 0)
  }
  
  override def keyPressed {
    key match {
      case ' ' =>
        info("clear" )
        p.clear
      case _ =>
    }
  }
  
  class Polygon(capacity:Int) extends TriMesh("polygon") {
    import field.kit.math.geometry.Spline
    import field.kit.math.Vec3
    import field.kit.Colour
    import field.kit.util.BufferUtil
    
    import javax.media.opengl.GL
    
    var curve = new Spline(capacity)
    allocate(capacity)
    
    override def allocateIndices = BufferUtil.int(capacity * 3)
    
    def update {
      info("update")
      import field.kit.util.BufferUtil
      
      // emit new vertex points
      val p = new Vec3
     
      vertices.clear
      var numPoints = 100
      vertexCount = numPoints
      for(i <- 0 until numPoints) {
        curve.point(i / numPoints.asInstanceOf[Float], p)
        p.put(vertices)
      }
      vertices.rewind
      
      solidColour(Colour.WHITE)
      
      // here's the crack!
      triangulate
      
      println("vertexCount "+ vertexCount +" triangleCount "+ triangleCount)
      indexCount = triangleCount * 3
//      indexCount = 100
    }
    
    def +=(x:Float, y:Float, z:Float) = {
      curve += (x,y,z)
      update
    }
    
    override def clear {
      super.clear
      curve.clear
      update
    }
  }
}
