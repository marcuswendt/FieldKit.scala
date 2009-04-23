/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.render.state

import field.kit.gl.render.RenderState

/**
 * Stores blend type constants for the <code>AlphaState</code> class
 * @author Marcus Wendt
 */
object AlphaState extends Enumeration {
  import javax.media.opengl.GL
  
  /** color is ignored  */
  val ZERO = Value(GL.GL_ZERO)
  
  /** full color is taken into addition */
  val ONE = Value(GL.GL_ONE)
  
  /** multiply by source color */
  val SRC_COLOR = Value(GL.GL_SRC_COLOR)
  
  /** multiply by destination color */
  val DST_COLOR = Value(GL.GL_DST_COLOR)
  
  /** multiply by inverted source color */
  val ONE_MINUS_SRC_COLOR = Value(GL.GL_ONE_MINUS_SRC_COLOR)
  
  /** multiply by inverted destination color */
  val ONE_MINUS_DST_COLOR = Value(GL.GL_ONE_MINUS_DST_COLOR)
  
  /** multiply by source alpha */
  val SRC_ALPHA = Value(GL.GL_SRC_ALPHA)
  
  /** multiply by destination alpha */
  val DST_ALPHA = Value(GL.GL_DST_ALPHA)
  
  /** multiply by inverted source alpha  */
  val ONE_MINUS_SRC_ALPHA = Value(GL.GL_ONE_MINUS_SRC_ALPHA)
  
  /** multiply by inverted destination alpha  */
  val ONE_MINUS_DST_ALPHA = Value(GL.GL_ONE_MINUS_DST_ALPHA)
  
  /** color is multiplied by MIN(src_alpha,1-dst_alpha) */
  val SRC_ALPHA_SATURATE = Value(GL.GL_SRC_ALPHA_SATURATE)
}

/** 
 * Applies alpha blending to an object
 * @author Marcus Wendt
 */
class AlphaState extends RenderState {
  import field.kit.gl.scene.Geometry
  import javax.media.opengl.GL
  
  /** The current source blend function. */
  var src = AlphaState.SRC_ALPHA
  
  /** The current destiantion blend function. */
  var dst = AlphaState.ONE_MINUS_SRC_ALPHA
  
  def this(src:AlphaState.Value, dst:AlphaState.Value) {
    this()
    this.src = src
    this.dst = dst
  }
  
  def enable(geo:Geometry) {
    gl.glEnable(GL.GL_BLEND)
    gl.glBlendFunc(src.id, dst.id)
  }
  
  def disable(geo:Geometry) {
    gl.glDisable(GL.GL_BLEND)
  }	
}
