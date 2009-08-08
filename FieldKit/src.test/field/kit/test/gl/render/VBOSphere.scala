/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 07, 2009 */
package field.kit.test.gl.render

object VBOSphere extends test.Sketch {
  import kit.math.FMath._
  import javax.media.opengl.GL
  import kit.gl.scene._

  class Sphere(name:String, var radius:Float, ures:Int, vres:Int) extends Mesh(name, Mesh.POINTS) {
    import kit.gl.render.objects.VertexBuffer
    
    var vbo:VertexBuffer = null
    
    init(radius, ures, vres)

    def this(name:String, res:Int) = this(name, 1f, res, res)

    def this(name:String, radius:Float, res:Int) = this(name, radius, res, res)
    
    def init(radius:Float, _ures:Int, _vres:Int) {
      import java.nio.ByteBuffer
      import java.nio.ByteOrder
      import kit.util.Buffer
      
      this.radius = radius
      
      // set minimum resolution
      var ures = if(_ures < 3) 3 else _ures
      var vres = if(_vres < 2) 2 else _vres

      // allocate buffers
      vertexCount = ures * (vres-1)
      vertices = Buffer.vertex(vertexCount)
      val angleStep = TWO_PI / vres.asInstanceOf[Float]
      var angle = angleStep

      info("allocated ", vertices.capacity, ures, vres)
      
      // step along Y axis
      for(i <- 1 until vres) {
        val r = sin(angle) * radius
        val y = -cos(angle)  * radius
        for(j <- 0 until ures) {
          vertices put cos(j) * r
          vertices put y
          vertices put sin(j) * r
        }
        angle += angleStep
      }
      vertices.rewind
      
      // --
      vbo = new VertexBuffer(vertices)
    }
    
    override def draw {
      val coloursEnabled = colours != null

      enableStates
      
      vbo.bind
      gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0)
      
      gl.glPointSize(2f)      
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
      
      gl.glDrawArrays(geometryType.id, 0, vertexCount)
      
      vbo.unbind
      disableStates
    }
  }
  
  var s:Sphere = null
  var showDebug = false

  var rotationX = 0f
  var rotationY = 0f
  var rotationZ = 0f

  def gl = pgl.gl
  
  init {
    s = new Sphere("sphere", 200f, 32)
    s.translation := (hwidth, hheight, 0f)
    s.colour := Colour.RED
  }

  def render {
    background(220)

    rotationX = mouseY/ height.asInstanceOf[Float] * TWO_PI * 100f
    rotationY = mouseX/ width.asInstanceOf[Float] * TWO_PI * 100f
    
    pgl.beginGL

    s.rotation := (rotationX, rotationY, 0)
    s.render
    
    /*
    gl.glTranslatef(hwidth, hheight, 0f)
    gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f)
    gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f)
    gl.glRotatef(rotationZ, 0.0f, 0.0f, 1.0f)
    
    
    gl.glColor4f(1f, 0f, 0f, 1f)
    gl.glPointSize(4f)
    
	s.vertices.rewind
    gl.glEnableClientState(GL.GL_VERTEX_ARRAY)  
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, s.vertices) 
    gl.glDrawArrays(GL.GL_POINTS, 0, s.vertexCount)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    */
    
	pgl.endGL

 
    if(showDebug) drawDebug
  }

  def drawDebug {
    noStroke
    fill(0, 200, 0)
    sphereDetail(5)
    info("vertexCount "+ s.vertexCount)
    s.vertices.rewind
    for(i <- 0 until s.vertexCount) {
      pushMatrix
      translate(width/2f, height/2f)
      rotateX(rotationX)
      rotateY(rotationY)
      translate(s.vertices.get, s.vertices.get, s.vertices.get)
      sphere(2.0f)
      popMatrix
    } 
  }
  
  override def keyPressed {
    key match {
      case 'd' => showDebug = !showDebug
      case _ =>
    }
  }
}
