/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 04, 2009 */
package field.kit.gl.render

/** 
 * <code>Capture</code> captures everything that is rendered between its <code>render</code> 
 * and <code>done</code> methods into a <code>Texture</code> using a <code>FrameBuffer</code> object.
 * <br />
 * A typical use case is to apply the <code>Texture</code> to a <code>Quad</code> 
 * which has a <code>ShaderState</code> applied to it. 
 * <br />
 * could also implement the MRT multiple render targets feature
 * @see http://wiki.delphigl.com/index.php/Tutorial_Framebufferobject
 * @author Marcus Wendt
 */
class Capture(width:Int, height:Int, alpha:Boolean, depth:Boolean) extends Renderable {
  import javax.media.opengl.GL
  
  import field.kit.Colour
  import field.kit.gl.render.objects._
  
  var clearBuffer = true
  var clearColour = new Colour(0f,0f,0f,1f)
  
  var fbo:FrameBuffer = _
  var depthBuffer:DepthBuffer = _

  // -- Initialise -------------------------------------------------------------
  fbo = new FrameBuffer
  fbo.bind
  
  // create depth buffer
  if(depth) {
    depthBuffer = new DepthBuffer(width, height)
    depthBuffer.bind
    fbo.init(FrameBuffer.Format.DEPTH, width, height)
    fbo += depthBuffer
  }
  
  // create texture target
  var texture = Texture(width, height, alpha)
  var textureUnit = 0
  texture.wrap = Texture.Wrap.CLAMP
  texture.filter = Texture.Filter.LINEAR
  texture.bind
  fbo += texture
    
  // check if everything went well
  fbo.isComplete
  
  // clean up
  texture.unbind
  fbo.unbind
  
  /**
   * Call this method to begin rendering into the custom <code>FrameBuffer</code>
   */
  def render {
    fbo.bind
    gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0_EXT + textureUnit)
    
    if(clearBuffer) {
     gl.glClearColor(clearColour.r, clearColour.g, clearColour.b, clearColour.a)
     gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
    }
  }
  
  /**
   * Call this method when you're done rendering, to release the <code>FrameBuffer</code> context
   */
  def done = fbo.unbind
  
  /**
   * Clean up the used ressources
   */
  def destroy {
    texture.destroy
    fbo.destroy
    if(depthBuffer != null) depthBuffer.destroy
  }
}
