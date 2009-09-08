/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 08, 2009 */
package field.kit.test.gl.scene.state

/** 
 * quick test for the fullscreen mode switch
 */
object AlphaStateTest extends field.kit.test.Sketch {
  import kit.math.Common._
  import kit.gl.scene._
  import kit.gl.scene.shape._
  import kit.gl.scene.state._
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    import processing.core.PConstants
    hint(PConstants.ENABLE_DEPTH_SORT)
  })
  
  val w = 550f
  val h = 550f
  val offset = w/6f
  
  var qr = Quad("red", w, h)
  qr.states += new AlphaState
  qr.translation.x = -offset
  qr.translation.y = -offset
  qr.colour := new Colour(1f, 0, 0, 0.5f)
  //qr.solidColour(new Colour(1f, 0, 0, 0.5f))
  
  var qg = Quad("green", w, h)
  qg.states += new AlphaState
  qg.randomizeColours
  
  var qb = Quad("blue", w, h)
  qb.states += new AlphaState
  qb.translation.x = offset
  qb.translation.y = offset
  qb.colour := new Colour(0, 0, 1f, 0.5f)
  //qb.solidColour(new Colour(0, 0, 1f, 0.5f))
    
  val scene = new Group("scene")
  scene += qr
  scene += qg
  scene += qb
  
  var mode = AlphaState.BlendMode.OFF
  var onBlack = false
  
  def render {
    background(if(onBlack) 0 else 255)
    
    scene foreach (s => {
      val q = s.asInstanceOf[Mesh]
      val as = q.state(classOf[AlphaState])
      as.set(mode)
    })
    
    scene.translation := (width/2f, height/2f, 0)
    
    beginGL
    scene.render
    endGL
  }
  
  override def keyPressed {
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
