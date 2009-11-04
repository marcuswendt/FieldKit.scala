/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 28, 2009 */
package field.kit.test.vision

/**
 * VM Args: -Djna.library.path=lib/vision
 * Until OpenCV.framework can be compiled as 64bit also use -d32
 */
object VisionTest extends test.Sketch {
  import kit.vision._
  import processing.core._
  import processing.core.PConstants._
  import javax.media.opengl._
  import controlP5._
  
  import kit.math.Vec3
  import kit.math.Common._
  
  import kit.gl.scene.shape.Quad
  
  var index = 0
  var showStage = true
  
  var preview:Quad = _
  
  // ui components
  var ui:ControlP5 = _
  var uiWindow:ControlWindow = _
  
  // -- Init -------------------------------------------------------------------
  Logger.level = Logger.FINE
  Vision.start
  
  //init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
  init(1024, 768, DEFAULT_FULLSCREEN, 0, {
    info(width, height)
    
    // init ui
    ui = new ControlP5(this)
//    ui.setAutoDraw(false)
    
    uiWindow = ui.addControlWindow("UI",0,25,300,400)
    uiWindow.setUpdateMode(ControlWindow.NORMAL)

    val offset = 15
    var i = 0
    def slider(name:String, default:Float) = {
      val w = 200
      val h = 10
      val x = offset
      val y = offset + i * (h + 5)
      i += 1
      
      val slider = ui.addSlider(name, 0f, 1f, default, x, y, w, h)
      slider.setWindow(uiWindow)
      slider
    }
    
    slider("background", Vision.background)
    slider("threshold", Vision.threshold)
    slider("dilate", Vision.dilate)
    slider("erode", Vision.erode)
    slider("contourMin", Vision.contourMin)
    slider("contourMax", Vision.contourMax)
    slider("contourReduce", Vision.contourReduce)
    slider("trackRange", Vision.trackRange)
    
    slider("warpX1", 0)
    slider("warpY1", 0)
    
    slider("warpX2", 1)
    slider("warpY2", 0)
    
    slider("warpX3", 1)
    slider("warpY3", 1)
    
    slider("warpX4", 0)
    slider("warpY4", 1)
  })
  
  def render {
    // update
    Vision.update
    
    // render
    background(0)
    
    if(showStage)
      drawStage
    
    drawBlobs
  }
  
  
  def drawStage {
    import kit.gl.render.objects.Texture
    import kit.gl.render.Image
    import kit.gl.scene.state.TextureState
    
    val s = Vision.stage(index)
    if(s.image == null) return
      
    val image = Image.create(s.width, s.height, Image.Format.GREY, s.image)
    
    if(preview == null) {
      preview = Quad()
      preview.translation := (width/2f, height/2f, 0)
      preview.scale := (width, -height, 1) 
      preview.states += TextureState(Texture(image))
      
    } else {      
      val ts = preview.state(classOf[TextureState])
      ts.textures(0).image = image 
    }
    
    beginGL
    preview.render
    endGL
  }
  
  def drawBlobs {
    rectMode(PConstants.CORNER)
    pushMatrix
    scale(width / 320f, height / 240f, 1f)
    Vision.blobs filter (_.active == true) foreach { b =>
      val c = 128 + 128 * (b.id / Vision.blobs.size.toFloat)
      stroke(255, c, c)
      strokeWeight(4)
      noFill
      rect(b.bounds.min.x, b.bounds.min.y, 
           b.bounds.width, b.bounds.height)
      
      noStroke
      fill(255, c, c)
      rect(b.x, b.y, 10, 10)
    }
    popMatrix
  }
  
  def controlEvent(event:ControlEvent) {
    val value = event.value
    event.label match {
      case "background" => Vision.background = value
      case "threshold" => Vision.threshold = value
      case "dilate" => Vision.dilate = value
      case "erode" => Vision.erode = value
      case "contourMin" => Vision.contourMin = value
      case "contourMax" => Vision.contourMax = value
      case "contourReduce" => Vision.contourReduce = value
      case "trackRange" => Vision.trackRange = value
      case _ => 
        def ctrl(name:String) = ui.controller(name).value
        Vision.setWarp(ctrl("warpX1"), ctrl("warpY1"),
        			   ctrl("warpX2"), ctrl("warpY2"),
        			   ctrl("warpX3"), ctrl("warpY3"),
        			   ctrl("warpX4"), ctrl("warpY4"))
//        info("event", event.label, "x1", ui.controller("warpX1").value)
    }
  } 
  
  override def keyPressed {
    keyCode match {
      case LEFT =>
        index -= 1
        if(index < 0) index = Vision.Stages.size - 1
      case RIGHT => 
        index += 1
        if(index == Vision.Stages.size) index = 0
      case UP => index = 0
      case _ =>
    }
    
    key match {
      case ' ' => showStage = !showStage
      case _ =>
    }
  }
}