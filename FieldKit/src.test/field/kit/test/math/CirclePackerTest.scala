/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 12, 2009 */
package field.kit.test.math

import field.kit.test.Sketch

object CirclePackerTest extends Sketch {
  import processing.core.PConstants._
  
  import field.kit.math.Common._
  import field.kit.math._
  import field.kit.math.geometry._
  import field.kit.math.packing._

  import scala.collection.mutable.ArrayBuffer
  
  var elements = new ArrayBuffer[Circle]

  var contents:ContentAdapter[Circle] = _
  var shape:ShapeAdapter = _
  
  def initElements {
    val count = random(0, 100).toInt
    elements.clear
    
    for(i <- 0 until count) {
      val pos = Vec3.random
      pos.x *= width/2f
      pos.y *= height/2f
      pos.x += width /2f
      pos.y += height /2f
      
      elements += new Circle(pos, random(0, 100))
    }
  }

  def initPacker {
    class BoundedCircle extends BoundedShape[Circle] {
      def initBounds {
        this := shape
        this.extent = Vec3(shape.radius)
        updateBounds
      }
  
      def updateShape {
        shape := this
      }
    }
    
    contents = new ContentAdapter(elements.asInstanceOf[Seq[Circle]]) {
      def createBoundedShape = new BoundedCircle
    }
    
    shape = new ShapeAdapter() {
      val circle = new Circle(new Vec3(width/2f, height/2f, 0), width*0.33f)
      
      // update bounds of the shape
      this := circle
      extent.x = circle.radius/2f
      extent.y = circle.radius/2f
      extent.z = 0
      updateBounds
      
      def isInside(x:Float, y:Float, z:Float) = circle.contains(Vec3(x,y,z))
    }
    
    val packer = new ShapePacker(shape, contents)  
    packer.pack
  }
  
  def initAll {
    initElements     
    initPacker
  }
  
  // -- Init -------------------------------------------------------------------
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    initAll
  })

  // -- Render -----------------------------------------------------------------
  def render {
    background(255)
    
    noFill
    strokeWeight(3)
    stroke(0)
    draw(shape.asInstanceOf[{ var circle:Circle }].circle)
    
    strokeWeight(1)
    stroke(64)
    for(circle <- contents.contents)
      draw(circle)
  }
  
  def draw(c:Circle) = ellipse(c.x, c.y, c.radius, c.radius)
  
  override def keyPressed {
    key match {
      case ' ' => initAll
      case _ =>
    }
  }
}
