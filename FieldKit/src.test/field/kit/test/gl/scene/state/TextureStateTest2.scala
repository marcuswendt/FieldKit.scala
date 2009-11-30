/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.test.gl.scene.state

/** 
 * quick test for the texture state feature
 */
object TextureStateTest2 extends test.Sketch {
  import kit.math.Common._
  import kit.gl.scene._
  import kit.gl.scene.shape._
  import kit.gl.scene.state._

  val w = 350f
  val h = 350f
  val offset = w/5f
  
  var qr = Quad("nwse tex", w, h)
  qr.states += new AlphaState
  qr.translation.x = -offset
  qr.translation.y = -offset
//  qr.solidColour(new Colour(1f, 0, 0, 0.5f))
//  qr.states += TextureState("res/test/test_nwse.png")
  qr.states += TextureState("res/test/mm.jpg")
  
  
  var qg = Quad("alpha texture", w, h)
  qg.states += new AlphaState
  qg.states += TextureState("res/test/test_nwse.png")
  
  
  val scene = new Group("scene")
  scene += qr
  scene += qg
  
  var mode = AlphaState.BlendMode.OFF
  var onBlack = false
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {})
  
  def render {
    background(if(onBlack) 0 else 255)
    
    scene.translation := (width/2f, height/2f, 0)
    
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
