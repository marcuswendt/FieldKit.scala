package field.kit.test.math

import field.kit.test.Sketch

object PackingTest extends Sketch {
  import field.kit.math.geometry._
  import field.kit.math.packing._
  import scala.collection.mutable.ArrayBuffer
  
  val MAX_ELEMENTS = 2000
  
  var packer = new RectanglePacker
    
  // defines the size of the packing elements
  def sizeMinW = 3
  def sizeMaxW = 10
  
  def sizeMinH = 3
  def sizeMaxH = 10
    
  // used as pack map
  import java.awt.image.BufferedImage
  val image = loadImage("res/test/flow.png")
  val bi = image.getImage.asInstanceOf[BufferedImage]
    
  protected var packContinously = true
  
  def resetPacking {
    packer.minArea = (sizeMinW * sizeMinH) * 10f
    
    // set packing rect
    packer.rect.x1 = mouseX
    packer.rect.y1 = mouseY
    packer.rect.width = bi.getWidth
    packer.rect.height = bi.getHeight
  
    // set pack map
    packer.map = new RectanglePacker.BufferedImageMap(bi, 0.5f)
    
    packer.mode = RectanglePacker.Mode.VerticalUpUsingMap
//    packer.mode = RectanglePacker.Mode.VerticalUp
    
    packer.clear
    packer.init

	if(!packContinously) {
	  for(i <- 0 until MAX_ELEMENTS)
	    packer += makeRect
   
	  import field.kit.util.Timer
	  val t = new Timer
	  packer.updateAll
	  info("packed", packer.size, "rects in", t.sinceStart, "ms")
	} 
  }

  def makeRect = new Rect(random(0, width), random(0, height), 
		  			      random(sizeMinW, sizeMaxW), random(sizeMinH, sizeMaxH))
  
  // -- Init -------------------------------------------------------------------
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
	  resetPacking
  })

  // -- Render -----------------------------------------------------------------
  def render {
    if(packContinously) {
      if(packer.count < MAX_ELEMENTS)
        packer += makeRect
      
      packer.update
    }
    
    background(0)
    
    stroke(0)
    strokeWeight(1)
    fill(255, 128)
    for(i <- 0 until packer.size) {
      val r = packer(i)
      rect(r.x1, r.y1, r.width, r.height)
    }
  
    noFill
    stroke(255)
    strokeWeight(3)
    rect(packer.rect.x1, packer.rect.y1, packer.rect.width, packer.rect.height)
  }
  
  override def mousePressed = resetPacking
  
  override def keyPressed {
    key match {
      case 'c' => 
        packContinously = !packContinously
        info("packContinously", packContinously)
        resetPacking
        
      case ' ' => resetPacking
      
      case _ =>
    }
  }
}
