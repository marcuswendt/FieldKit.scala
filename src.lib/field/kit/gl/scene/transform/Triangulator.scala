/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 17, 2009 */
package field.kit.gl.scene.transform

/**
 * A fast way to create triangles from a given set of vertices
 * 
 * @see <a href="http://www.flipcode.org/cgi-bin/fcarticles.cgi?show=63943">Efficient Polygon Triangulation</a>
 * @see <a href="http://dev.processing.org/source/index.cgi/trunk/processing/core/src/processing/core/PGraphics2D.java?view=markup">PGraphics2D</a>
 * @see <a href="http://dev.processing.org/source/index.cgi/trunk/processing/core/src/processing/core/PGraphics3D.java?view=markup">PGraphics3D</a>
 */
trait Triangulator {
  import java.nio.FloatBuffer
  import java.nio.IntBuffer
  
  private val EPSILON = 0.0001f
  private var orderedVertices:Array[Int] = Array(0)
  var triangleCount = 0
  
  def triangulate(m:Mesh):Unit = triangulate(m.vertexCount, m.vertices, m.indices)
  
  /**
   * Reads vertices from the vertexbuffer and constructs triangles from it
   * 
   * TODO this is essentially a 2D triangulation only and will probably
   * cause problems when used in 3D to fix this have a look at Processings PGraphics3D class
   */
  def triangulate(vertexCount:Int, vertices:FloatBuffer, indices:IntBuffer) {
    if(vertexCount > 2) {
      // prepare
      indices.clear
      triangleCount = 0
      
      // helper
      def x(index:Int) = vertices get (index * 3)
      def y(index:Int) = vertices get (index * 3 + 1)
      
      // allocate array when necessary
      if(orderedVertices.size != vertexCount)
        orderedVertices = new Array[Int](vertexCount)
            
      // first we check if the polygon goes clockwise or counterclockwise
      var area = 0.0f
      for(q <- 0 until vertexCount) {
        val p = (q + vertexCount - 1) % vertexCount
        area += (x(q) * y(p) - x(p) * y(q))
      }
      
      // then sort the vertices so they are always in a counterclockwise order
      if(area > 0) {
        for(i <- 0 until vertexCount) 
          orderedVertices(i) = i
      } else {
        for(i <- 0 until vertexCount) 
          orderedVertices(i) = (vertexCount - 1) - i
      }
      
      // remove vc-2 Vertices, creating 1 triangle every time
      var vc = vertexCount
      var count = 2 * vc // complex polygon detection      
      var v = vc - 1

      while(vc > 2 && count >= 0) {
        var snip = true
        
        // if we start over again, is a complex polygon
        count -= 1
        if(count <= 0) {
//          println("triangulation failed "+ vc +" count "+ count)
        }
        
        // get 3 consecutive vertices <u,v,w>
        var u = v
        if(vc <= u) u = 0 // previous
        v = u + 1
        if(vc <= v) v = 0 // current
        var w = v + 1
        if(vc <= w) w = 0 // next
        
        // triangle A B C
        val Ax = -x(orderedVertices(u))
        val Ay = y(orderedVertices(u))
        val Bx = -x(orderedVertices(v))
        val By = y(orderedVertices(v))
        val Cx = -x(orderedVertices(w))
        val Cy = y(orderedVertices(w))
        
        // first we check if <u,v,w> continues going ccw
        if(EPSILON < (((Bx - Ax) * (Cy - Ay)) - ((By - Ay) * (Cx - Ax)))) {
          for(p <- 0 until vc) {
            if(!((p == u) || (p == v) || (p == w))) {
              val Px = -x(orderedVertices(p))
              val Py = y(orderedVertices(p))
              
              val ax = Cx - Bx
              val ay = Cy - By
              val bx = Ax - Cx
              val by = Ay - Cy
              val cx = Bx - Ax
              val cy = By - Ay
              val apx = Px - Ax
              val apy = Py - Ay
              val bpx = Px - Bx
              val bpy = Py - By
              val cpx = Px - Cx
              val cpy = Py - Cy
              val aCROSSbp = ax * bpy - ay * bpx
              val cCROSSap = cx * apy - cy * apx
              val bCROSScp = bx * cpy - by * cpx
              
              if((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f)) {
                snip = false
              }
            }// if
          }// for
        
          if (snip) {
            if(indices.position < indices.capacity - 3) {
              indices put orderedVertices(u)
              indices put orderedVertices(v)
              indices put orderedVertices(w)
              triangleCount += 1
            }
            
            // remove v from remaining polygon
            for(t <- v+1 until vc) {
              val s = t-1
              orderedVertices(s) = orderedVertices(t)
            }
            
            vc -= 1
            
            // reset error detection counter
            count = 2 * vc
          }
        }
      }
      indices.rewind
    }
  }
}