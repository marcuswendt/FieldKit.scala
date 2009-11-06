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
  import math.Common._

  // -- Camera -----------------------------------------------------------------
  import kit.gl.render.AdvancedCamera
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
      drawable = factory.getGLDrawable(parent, capabilities, null);
      context = drawable.createContext(null);

      // need to get proper opengl context since will be needed below
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
      
    /*
    modelview.m00 = activeCamera.modelView.m00
    modelview.m01 = activeCamera.modelView.m01
    modelview.m02 = activeCamera.modelView.m02
    modelview.m03 = activeCamera.modelView.m03
    
    modelview.m10 = activeCamera.modelView.m10
    modelview.m11 = activeCamera.modelView.m11
    modelview.m12 = activeCamera.modelView.m12
    modelview.m13 = activeCamera.modelView.m13
    
    modelview.m20 = activeCamera.modelView.m20
    modelview.m21 = activeCamera.modelView.m21
    modelview.m22 = activeCamera.modelView.m22
    modelview.m23 = activeCamera.modelView.m23
    
    modelview.m30 = activeCamera.modelView.m30
    modelview.m31 = activeCamera.modelView.m31
    modelview.m32 = activeCamera.modelView.m32
    modelview.m33 = activeCamera.modelView.m33

    projection.m00 = activeCamera.projection.m00
    projection.m01 = activeCamera.projection.m01
    projection.m02 = activeCamera.projection.m02
    projection.m03 = activeCamera.projection.m03
    
    projection.m10 = activeCamera.projection.m10
    projection.m11 = activeCamera.projection.m11
    projection.m12 = activeCamera.projection.m12
    projection.m13 = activeCamera.projection.m13
    
    projection.m20 = activeCamera.projection.m20
    projection.m21 = activeCamera.projection.m21
    projection.m22 = activeCamera.projection.m22
    projection.m23 = activeCamera.projection.m23
    
    projection.m30 = activeCamera.projection.m30
    projection.m31 = activeCamera.projection.m31
    projection.m32 = activeCamera.projection.m32
    projection.m33 = activeCamera.projection.m33
    */
  }
}
