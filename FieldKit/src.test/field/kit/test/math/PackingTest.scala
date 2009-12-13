package field.kit.test.math

import field.kit.test.Sketch

object PackingTest extends Sketch {
  import field.kit.math.geometry._
  import field.kit.math.packing._
  import scala.collection.mutable.ArrayBuffer
  
  var area = new Rect(100, 100, 300, 300)
  var shapes = new ArrayBuffer[Rect]
  var packed = 0
  var packer:RectanglePacking = _
  
  // used as pack map
  import java.awt.image.BufferedImage
  val image = loadImage("res/test/flow.png")
  val bi = image.getImage.asInstanceOf[BufferedImage]
    
  protected var packContinously = true
  
  def initShapes {
    val count = random(packed, 2000).toInt
    shapes.clear
    for(i <- 0 until count) {
      shapes += new Rect(random(0, width), random(0, height), 
                         random(height/4f, height) / 40f, random(width/4f, width) / 40f)
    }
    
    packer = new RectanglePacking(area, shapes.asInstanceOf[Seq[Rect]])
    
    packer.map = new RectanglePacking.BufferedImageMap(bi, 0.5f)
    packer.mode = RectanglePacking.Mode.VerticalUpUsingMap
    
    resetPacking
  }
  
  def resetPacking {
    packer.map.x1 = area.x1
    packer.map.y1 = area.y1
    
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
    
    stroke(0)
    strokeWeight(1)
    fill(255, 128)
    for(i <- 0 until packed) {
      val r = shapes(i)
      rect(r.x1, r.y1, r.width, r.height)
    }
  
    noFill
    stroke(255)
    strokeWeight(3)
    rect(area.x1, area.y1, area.width, area.height)
  }
  
  override def mousePressed {
    area.x1 = mouseX
    area.y1 = mouseY
    resetPacking
  }

  override def mouseDragged {
    area.x2 = mouseX
    area.y2 = mouseY
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
