/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.test.gl.render.state

/** 
 * quick test for the texture state feature
 */
object TextureStateTest2 extends field.kit.Sketch {
  import field.kit.gl.scene.shape.Quad
  import field.kit.gl.scene.state.AlphaState
  import field.kit.gl.scene.state.TextureState
  import field.kit.Colour
  import field.kit.gl.scene._
  
  val w = 350f
  val h = 350f
  val offset = w/5f
  
  var qr = new Quad("nwse tex", w, h)
  qr.states += new AlphaState
  qr.translation.x = -offset
  qr.translation.y = -offset
//  qr.solidColour(new Colour(1f, 0, 0, 0.5f))
//  qr.states += TextureState("res/test/test_nwse.png")
  qr.states += TextureState("res/test/mm.jpg")
  
  
//  var qg = new Quad("alpha texture", w, h)
//  qg.states += new AlphaState
//  qg.states += TextureState("res/test/pattern_rgb.png")
  
  
  val scene = new Group("scene")
  scene += qr
//  scene += qg
  
  var mode = AlphaState.BlendMode.OFF
  var onBlack = false
  
  init(1280, 768, false, {
    info("initializer")
  })
  
  def render {
    background(if(onBlack) 0 else 255)
    
//    scene.children foreach (s => {
//      val q = s.asInstanceOf[Geometry]
//      val as = q.state(classOf[AlphaState])
//      as.set(mode)
//    })
    
    scene.translation set (width/2f, height/2f, 0)
    
    beginGL
    scene.render
    endGL
  }
  
  override def keyPressed {
//    info("keyPressed ", key)
    key match { 
      case ' ' =>
        var nextId = if(mode.id + 1 < AlphaState.BlendMode.maxId) mode.id + 1 else 0
        mode = AlphaState.BlendMode(nextId)
        info("BlendMode "+ mode)
        
      case 'b' => 
        onBlack = !onBlack
          
      case _ =>
    }
  }
}
