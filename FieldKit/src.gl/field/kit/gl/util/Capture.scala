/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created June 04, 2009 */
package field.kit.gl.util

import field.kit._
import field.kit.gl._
import field.kit.gl.objects._

import javax.media.opengl.GL

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
class Capture(width:Int, height:Int, alpha:Boolean, depth:Boolean) extends GLUser {
	var clearBuffer = true
	var clearColour = new Colour(0f,0f,0f,1f)

	var fbo:FrameBuffer = _
	var depthBuffer:DepthBuffer = _

	var texture:Texture = _
	var textureUnit = 0
	
	init 
	
	def init {
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
		texture = ImageTexture(width, height, alpha, false)
		texture.wrap = Texture.Wrap.CLAMP
		texture.filter = Texture.Filter.LINEAR
		texture.bind
		fbo += texture
	  
		// check if everything went well
		fbo.isComplete
	
		// clean up
		texture.unbind
		fbo.unbind
	}

	/**
	 * Binds the capture's texture
	 */
	def bind = texture.bind

	/**
	 * Unbinds the capture's texture
	 */
	def unbind = texture.unbind

	/**
	 * Call this method to begin rendering into the custom <code>FrameBuffer</code>
	 */
	def beginCapture {
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
	def endCapture = fbo.unbind

	/**
	 * Clean up the used ressources
	 */
	def destroy {
		texture.destroy
		fbo.destroy
		if(depthBuffer != null) depthBuffer.destroy
	}
}
