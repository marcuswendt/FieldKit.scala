/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.render

/** 
 * base trait for all renderable elements in the scene
 * @author Marcus Wendt
 */
trait Renderable {
  import javax.media.opengl.GL
  import javax.media.opengl.GLContext
  
  def gl = GLContext.getCurrent.getGL
  def render
}

trait Drawable extends Renderable {
  var isVisible = true
  def toggleVisibility = isVisible = !isVisible
    
  def render = if(isVisible) draw
  def draw
}
