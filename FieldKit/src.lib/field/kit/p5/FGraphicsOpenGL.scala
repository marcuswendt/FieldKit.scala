/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
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
  import math.FMath

  // -- Camera -----------------------------------------------------------------
  import kit.gl.render.Camera
  var activeCamera:Camera = null

  /**
   * Creates a new <code>GLDrawable</code> and the default <code>Camera</code> 
   */
  override protected def allocate {
    if (context == null) {
      val capabilities = new GLCapabilities
      
       if (!hints(DISABLE_OPENGL_2X_SMOOTH)) {
        capabilities.setSampleBuffers(true)
        capabilities.setNumSamples(2)
        
      } else if (hints(ENABLE_OPENGL_4X_SMOOTH)) {
        capabilities.setSampleBuffers(true)
        capabilities.setNumSamples(8) // de fault to 8 instead of 4  
      }
      
      val factory = GLDrawableFactory.getFactory
      drawable = factory.getGLDrawable(parent, capabilities, null);
      context = drawable.createContext(null);

      // need to get proper opengl context since will be needed below
      gl = context.getGL
      
      // init camera
      activeCamera = new Camera(width, height)
      activeCamera.init(60 * FMath.DEG_TO_RAD, width, height)
      
      //activeCamera.location := (width*.5f, height*.5f, -623f)
      
      // Flag defaults to be reset on the next trip into beginDraw().
      settingsInited = false
    } else {
      reapplySettings
    }
  }
  
  override def beginDraw {
    super.beginDraw()
    
    // p5 overrides the projection and modelview matrices, 
    // therefore we need to refresh the camera on every frame
    activeCamera.update
    activeCamera.render
    
    /*
    if (drawable != null) {
      drawable.setRealized(parent.isDisplayable())
      if (parent.isDisplayable()) {
        drawable.setRealized(true)
      } else {
        return
      }
      detainContext()
    }
    
    if (!settingsInited) defaultSettings()
    
    // -- PGraphics3D.beginDraw ------------------------------------------------
    // reset vertices
    vertexCount = 0;
    
    // -- Camera ---------------------------------------------------------------
//              super.beginDraw()
//    activeCamera.update
	activeCamera.init(60 * FMath.DEG_TO_RAD, width, height)
    activeCamera.render

    // -- Init GL --------------------------------------------------------------
//    gl.glMatrixMode(GL.GL_MODELVIEW)
//    gl.glLoadIdentity
//    gl.glScalef(1, -1, 1)
    
    // processing defaults
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

    // this is necessary for 3D drawing
    if(hints(DISABLE_DEPTH_TEST)) {
      gl.glDisable(GL.GL_DEPTH_TEST)
    } else {
      gl.glEnable(GL.GL_DEPTH_TEST)
    }
    // use <= since that's what processing.core does
    gl.glDepthFunc(GL.GL_LEQUAL)

    // because y is flipped
    gl.glFrontFace(GL.GL_CW)

    // coloured stuff
    gl.glEnable(GL.GL_COLOR_MATERIAL)
    gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE)
    gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR)
    */
  }
}
