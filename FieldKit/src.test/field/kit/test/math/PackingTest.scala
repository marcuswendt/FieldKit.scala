package field.kit.test.math

import field.kit.test.Sketch

object PackingTest extends Sketch {
  import field.kit.math.geometry._
  import field.kit.math.packing._
  import scala.collection.mutable.ArrayBuffer
  
  var map = new Rect 
  var shapes = new ArrayBuffer[Rect]
  var packed = 0
  var packer:RectanglePacking = _
  
  var packContinously = false
  
  def initShapes {
    val count = random(packed, 1000).toInt
    shapes.clear
    for(i <- 0 until count) {
      shapes += new Rect(random(0, width), random(0, height), 
                         random(0, width) / 20f, random(0, height) / 20f)
    }
    
    packer = new RectanglePacking(map, shapes.asInstanceOf[Seq[Rect]])
    
    resetPacking
  }
  
  def resetPacking {
    if(packContinously)
      packer.reset
    else
      packed = packer.updateAll  
  }
  
  
  // -- Init -------------------------------------------------------------------
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
	  initShapes
  })

  // -- Render -----------------------------------------------------------------
  def render {
    if(packContinously)
      packed = packer.update
    
    background(0)
  
    noFill
    stroke(255)
    strokeWeight(3)
    rect(map.x1, map.y1, map.width, map.height)
    
    stroke(128)
    strokeWeight(1)
    noStroke
    fill(128)
    for(i <- 0 until packed) {
      val r = shapes(i)
      rect(r.x1, r.y1, r.width, r.height)
    }
  }
  
  override def mousePressed {
    map.x1 = mouseX
    map.y1 = mouseY
    resetPacking
  }

  override def mouseDragged {
    map.x2 = mouseX
    map.y2 = mouseY
    resetPacking
  }
  
  override def keyPressed {
    key match {
      case 'c' => packContinously = !packContinously
      case ' ' => initShapes
      case _ =>
    }
  }
}
