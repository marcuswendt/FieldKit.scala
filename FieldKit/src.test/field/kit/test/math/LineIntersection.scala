/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 18, 2009 */
package field.kit.test.math

import field.kit._

object LineIntersectionTest extends Sketch {
  import field.kit.math._
  
  val l1 = new LineDebug
  val l2 = new LineDebug

  var index = 0
  
  init(1024, 768)
  
  def render {
    background(16)
    
    noFill
    stroke(255)
    line(l1.a.x, l1.a.y, l1.b.x, l1.b.y)
    line(l2.a.x, l2.a.y, l2.b.x, l2.b.y)
    
    noStroke
    fill(255,0,0)
    rect(l1.a.x, l1.a.y, 5, 5)
    rect(l1.b.x, l1.b.y, 5, 5)
    
    fill(0,255,0)
    rect(l2.a.x, l2.a.y, 5, 5)
    rect(l2.b.x, l2.b.y, 5, 5)
    
    val i = new Vec2
    if(l1.intersects(l2, i)) {
      fill(255, 128)
      ellipse(i.x, i.y, 10, 10)
    }
  }
  
  override def mousePressed {
    val v = index match {
      case 0 => l1.a
      case 1 => l1.b
      case 2 => l2.a
      case 3 => l2.b
    }
    index = (index + 1) % 4
    v.x = mouseX
    v.y = mouseY
  }
  
  class LineDebug {
    var a = new Vec2
    var b = new Vec2
    
    def intersects(other:LineDebug, result:Vec2) = 
      Common.intersects(a.x, a.y, b.x, b.y, other.a.x, other.a.y, other.b.x, other.b.y, result)
  }
}
