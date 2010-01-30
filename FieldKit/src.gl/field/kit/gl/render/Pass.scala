/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 04, 2009 */
package field.kit.gl.render

import field.kit.gl.scene.state.ShaderState
import field.kit.gl.scene.shape.Quad
import field.kit.math.Vec3

/** Companion object to class <code>Pass</code> */
object Pass {
  import javax.media.opengl.GLContext
  
  // defaults
  def width = GLContext.getCurrent.getGLDrawable.getWidth
  def height = GLContext.getCurrent.getGLDrawable.getHeight
  val alpha = true
  val depth = false
  
  def apply(name:String, shader:ShaderState) =
    new Pass(name,shader,width,height,alpha,depth)
}

/** 
 * <code>Pass</code> renders the given 3D scene into a FBO texture, to re-render 
 * it into a <code>Quad</code> using the given <code>ShaderState</code>
 * This allows fast image processing techniques on the GPU using GLSL shaders.
 * 
 * @author Marcus Wendt
 */
class Pass(name:String, var shader:ShaderState, width:Int, height:Int, alpha:Boolean, depth:Boolean)
           extends Quad(name+"Pass") {
             
  import field.kit.gl.scene.state.TextureState
  import field.kit.gl.render.objects.Texture
  import field.kit.math.Common._
  
  scale := (width.toFloat, height.toFloat, 1f)
  translation := (width/2f, height/2f, 0f)
  
  var capture = new Capture(width, height, alpha, depth)
  states += new TextureState(capture.texture)
  states += shader
  
  /** call this before rendering the contents that should go into the buffer */
  def pre = capture.render
  
  /** call this after rendering the contents that should go into the buffer */
  def post = capture.done
}