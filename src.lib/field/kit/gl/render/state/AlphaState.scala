/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.render.state

import field.kit.gl.render.RenderState

/** applies alpha blending to an object */
class AlphaState extends RenderState {
  import field.kit.gl.scene.Geometry
  import javax.media.opengl.GL
  
  /** The current source blend function. */
  var src = GL.GL_SRC_ALPHA
  
  /** The current destiantion blend function. */
  var dst = GL.GL_ONE_MINUS_SRC_ALPHA
  
  def enable(geo:Geometry) {
    gl.glEnable(GL.GL_BLEND)
    gl.glBlendFunc(src, dst)
  }
  
  def disable(geo:Geometry) {
    gl.glDisable(GL.GL_BLEND)
  }	
}
