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
object TextureStateTest extends field.kit.Sketch {
  import field.kit.gl.scene._
  import field.kit.gl.scene.shape._
  import field.kit.gl.scene.state._

  val w = 350f
  val h = 350f
  val offset = w/5f
  
  var qr = Quad("regular texture", w, h)
  qr.states += new AlphaState
  qr.translation.x = -offset
  qr.translation.y = -offset
//  qr.solidColour(new Colour(1f, 0, 0, 0.5f))
  qr.states += TextureState("res/test/test.jpg")
  
  
  var qg = Quad("alpha texture", w, h)
  qg.states += new AlphaState
  qg.states += TextureState("res/test/test_alpha.png")
  
  var qb = Quad("no alpha png texture", w, h)
  qb.states += new AlphaState
  qb.translation.x = offset
  qb.translation.y = offset
  qb.solidColour(new Colour(1f, 0.75f))
  qb.states += TextureState("res/test/test.png")
  
  var qc = Quad("tga", w, h)
  qc.states += new AlphaState
  qc.translation.x = offset + w
  qc.translation.y = offset
//  qc.solidColour(new Colour(1f, 0.75f))
  qc.states += TextureState("res/test/test.tga")
  
  var qd = Quad("gif", w, h)
  qd.states += new AlphaState
  qd.translation.x = offset + w
  qd.translation.y = offset - h
  qd.states += TextureState("res/test/test.gif")
  
  val scene = new Group("scene")
  scene += qr
  scene += qg
  scene += qb
  scene += qc
  scene += qd
  
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
