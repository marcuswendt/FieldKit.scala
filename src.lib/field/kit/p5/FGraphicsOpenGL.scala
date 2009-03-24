/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.p5

import processing.opengl.PGraphicsOpenGL

class FGraphicsOpenGL extends PGraphicsOpenGL {
  
  override protected def allocate {
    import javax.media.opengl._
    
    if (context == null) {
      val capabilities = new GLCapabilities
      capabilities.setSampleBuffers(true)
      capabilities.setNumSamples(8)
      
      val factory = GLDrawableFactory.getFactory
      
      drawable = factory.getGLDrawable(parent, capabilities, null);
      context = drawable.createContext(null);

      // need to get proper opengl context since will be needed below
      gl = context.getGL
      // Flag defaults to be reset on the next trip into beginDraw().
      settingsInited = false
      
    } else {
      reapplySettings}
	}
}
