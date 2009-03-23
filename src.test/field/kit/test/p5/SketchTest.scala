/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 12, 2009 */
package field.kit.test.p5

import field.kit._

object SketchTest extends Sketch {
  
  info("FAppletTest created")
  
  init(1024,768, {
	  info("in custom initializer")
  })
  
  // init data
  val res = 10
  val t = new Terrain
  (0 until res) foreach(x => { 
    val l = new Line
    (0 until res) foreach (y => 
      l.points += new Vec3(x,y,0)
    )
    t.lines += l
  })
  
  def render {
    background(0, 64, 32)
    
    stroke(255)
    noFill
    t.lines foreach(l => {
      for(i <- 0 until l.points.size-1) {
        val p1 = l.points(i)
        val p2 = l.points(i+1)
        line(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z)
      }
    })
  }

  class Terrain {
    import scala.collection.mutable.ArrayBuffer
    val lines = new ArrayBuffer[Line]()
  }
  
  class Line {
    import scala.collection.mutable.ArrayBuffer
    val points = new ArrayBuffer[Vec3]()
  }
}