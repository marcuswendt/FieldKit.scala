/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.p5

import processing.opengl.PGraphicsOpenGL

/**
 * @author Marcus Wendt
 */
class FGraphicsOpenGL extends PGraphicsOpenGL {
  import processing.core.PConstants._
  import javax.media.opengl._
  import field.kit.math.Common._

  // -- Camera -----------------------------------------------------------------
  import field.kit.gl.render.AdvancedCamera
  var activeCamera:AdvancedCamera = null

  /**
   * Creates a new <code>GLDrawable</code> and the default <code>Camera</code> 
   */
  override protected def allocate {
    if (context == null) {
      val capabilities = new GLCapabilities
      
      // set aa samples
      val s = parent.asInstanceOf[BasicSketch]
      if(s.aaSamples > 0) {
        capabilities.setSampleBuffers(true)
        capabilities.setNumSamples(s.aaSamples)
      }
      
      val factory = GLDrawableFactory.getFactory
      drawable = factory.getGLDrawable(parent, capabilities, null)
      context = drawable.createContext(null)

      // need to get proper opengl context
      gl = context.getGL
      
      // init camera
      activeCamera = new AdvancedCamera(width, height)
      
      // Flag defaults to be reset on the next trip into beginDraw().
      settingsInited = false
    } else {
      reapplySettings
    }
  }
  
  override def beginDraw {
    super.beginDraw()
    
    // Cannot render camera directly, 
    // instead we need to override processings default matrices
//    activeCamera.feed(parent)
//    activeCamera.render
  }
}
