/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 04, 2009 */
package field.kit.gl.render

import scene.state.ShaderState
import scene.shape.Quad
import math.Vec3

/** Companion object to class <code>Pass</code> */
object Pass {
  import javax.media.opengl.GLContext
  
  // defaults
  def width = GLContext.getCurrent.getGLDrawable.getWidth
  def height = GLContext.getCurrent.getGLDrawable.getHeight
  val alpha = true
  val depth = false
  
  def apply(name:String, shader:ShaderState, scene:Renderable) =
    new Pass(name,shader,scene,width,height,alpha,depth)
}

/** 
 * <code>Pass</code> renders the given 3D scene into a FBO texture, to re-render 
 * it into a <code>Quad</code> using the given <code>ShaderState</code>
 * This allows fast image processing techniques on the GPU using GLSL shaders.
 * 
 * @author Marcus Wendt
 */
class Pass(name:String, var shader:ShaderState, var scene:Renderable, 
           width:Int, height:Int, alpha:Boolean, depth:Boolean)
           extends Quad(name+"Pass", Vec3(), 1f, 1f) {
             
  import field.kit.gl.scene.state.TextureState
  
  var capture = new Capture(width, height, alpha, depth)
  states += new TextureState(capture.texture)
  states += shader
  
  override def draw {
    capture.render
    scene.render
    capture.done
    
    super.draw
  }
  
}