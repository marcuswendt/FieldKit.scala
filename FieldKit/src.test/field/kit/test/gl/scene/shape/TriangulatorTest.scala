/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.test.gl.scene.shape

import field.kit._

abstract class TriangulatorTest extends Sketch {
  init(1024,768)
}

/** 
 * tests the simple/ fast triangulator using an incremental line-drawing approach 
 */
object FastIncrementalTriangulatorTest extends TriangulatorTest {
  import field.kit.gl.scene._
  import field.kit.math._
  
  var p = new Polygon(100)
  
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
  
  class Polygon(capacity:Int) extends Mesh("polygon") {
    import field.kit.math.geometry.Spline
    import field.kit.math.Vec3
    import field.kit.Colour
    import field.kit.util.Buffer
    
    import javax.media.opengl.GL
    
    var curve = new Spline(capacity)
    data.allocVertices(capacity)
    data.allocIndices(capacity * 3)
    
    def update {
      info("update")
      
      // emit new vertex points
      val p = new Vec3
     
      val vertices = data.vertices
      vertices.clear
      var numPoints = 100
      data.vertexCount = numPoints
      for(i <- 0 until numPoints) {
        curve.point(i / numPoints.asInstanceOf[Float], p)
        p.put(vertices)
      }
      vertices.rewind
      
      solidColour(Colour.WHITE)
      
      // here's the crack!
      triangulate
      
      println("vertexCount "+ data.vertexCount +" triangleCount "+ triangleCount)
      //indexCount = triangleCount * 3
//      indexCount = 100
    }
    
    def clear {
      //super.clear
      curve.clear
      update
    }
    
    def +=(x:Float, y:Float, z:Float) = {
      curve += (x,y,z) 
      update
    }
  }
}


/** 
 * tests the simple/ fast triangulator using a set of points lying on two circles 
 */
object FastDegeneratePolygonTriangulatorTest extends TriangulatorTest {
  import field.kit.gl.scene._
  import field.kit.math._
  
  var p = new Polygon(100)
  
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
    val vertices = p.data.vertices
    vertices.rewind
    for(i <- 0 until p.data.vertexCount) {
      rect(vertices.get, vertices.get, 6, 6)
      vertices.get
    }
    p.data.vertices.rewind
  }
  
  class Polygon(capacity:Int) extends Mesh("polygon") {
    import field.kit.math.geometry.Spline
    import field.kit.math._
    import field.kit.Colour
    import field.kit.util.Buffer
    
    import javax.media.opengl.GL

    val vertices = data.allocVertices(capacity)
    data.allocIndices(capacity * 3)
    
    val offsetX = width/2f
    val offsetY = height/2f
    
    // draw outer circle
    val r1 = 350f
    var numPoints = (capacity * 0.66).asInstanceOf[Int]
    for(i <- 0 until numPoints) {
      val t = (i / numPoints.asInstanceOf[Float]) * FMath.TWO_PI
      vertices put FMath.sin(t) * r1 + offsetX
      vertices put FMath.cos(t) * r1 + offsetY
      vertices put 0
      data.vertexCount += 1
    }
    
    // draw inner circle
    val r2 = 50f
    numPoints = (capacity * 0.33).asInstanceOf[Int]
    for(i <- 0 until numPoints) {
      val t = (i / numPoints.asInstanceOf[Float]) * FMath.TWO_PI
      vertices put FMath.sin(t) * r2 + offsetX
      vertices put FMath.cos(t) * r2 + offsetY
      vertices put 0
      data.vertexCount += 1
    }
    
    // --
    vertices.rewind
    //vertexCount = capacity
    
    triangulate
    //indexCount = triangleCount * 3
    
    solidColour(Colour.WHITE)
  }
}