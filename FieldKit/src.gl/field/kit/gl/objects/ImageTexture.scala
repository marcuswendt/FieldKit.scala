/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 17, 2010 */
package field.kit.gl.objects

import field.kit.gl._
import field.kit.util.Buffer

import javax.media.opengl.GL
import java.nio.IntBuffer

/** 
* Companion object to <code>ImageTexture</code>
* @author Marcus Wendt
*/
object ImageTexture {
	import java.net.URL

	/** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given path */
	def apply(file:String) = load(file)

	/** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given <code>URL</code> */
	def apply(file:URL) = load(file)

	/** Creates a new <code>Texture</code> with a blank <code>Image</code> of the given dimensions */
	def apply(width:Int, height:Int, alpha:Boolean) = 
	create(width, height, alpha)

	def apply(image:Image) = new ImageTexture(image)

	// -- Loading & Creating -----------------------------------------------------
	/** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given path */
	def load(file:String) = new ImageTexture(Image(file))

	/** Creates a new <code>Texture</code> with an <code>Image</code> loaded from the given <code>URL</code> */
	def load(file:URL) = new ImageTexture(Image(file))

	/** Creates a new <code>Texture</code> with a blank <code>Image</code> of the given dimensions */
	def create(width:Int, height:Int, alpha:Boolean) =
		new ImageTexture(Image(width, height, alpha))
}

/**
* An OpenGL texture that keeps an internal bitmap image to allow pixel manipulation 
* @author Marcus Wendt
*/
class ImageTexture(protected var _image:Image, 
				   protected var createMipMaps:Boolean = true) 
				   extends Texture {
	
	protected var buffer:IntBuffer = _	

	image = _image
	
	def image = _image
	def image_=(image:Image) = {
		this._image = image
		updateImage
		needsUpdate = true
	}
	
	protected def updateTexture {
		if(buffer == null) return

		if(id == Texture.UNDEFINED) create

		// upload image data to texture
//		info("updating texture", this.id, "width", image.width, "height", image.height)

		// upload data
		gl.glEnable(GL.GL_TEXTURE_2D)
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.id)
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, width, height, 
		    0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer)

		// set filter parameters
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, filter.id)
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filter.id)
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrap.id)
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrap.id)

		if(createMipMaps) {
			glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, 4,
			            width, height,
			            GL.GL_RGBA,
			            GL.GL_UNSIGNED_BYTE, buffer)

			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
			      GL.GL_LINEAR);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
			      GL.GL_LINEAR_MIPMAP_LINEAR);
		}

		// select modulate to mix texture with color for shading
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE)

		unbind
		//    } catch {
		//      case e:java.lang.IndexOutOfBoundsException => 
		//        warn("update: Couldnt upload image", e)
		//        id = Texture.UNDEFINED

		needsUpdate = false
	}

	/** based on code ported from Processings PGraphicsOpenGL */
	protected def updateImage {
		if(_image == null) return

		// simply set texture dimensions to image dimensions, assuming the gpu supports
		// non power of two textures
		_width = image.width
		_height = image.height

		// make sure width & height are a power of 2
/*
		width = 2
		height = 2

		while(width < image.width)
		width *= 2

		while(height < image.height)
		height *= 2
*/

		// create pixels storage
		if(buffer == null) 
			buffer = Buffer.int(width * height)
		
		// copy data into the texture
		var t = 0
		var p = 0
		image.format match {
			case Image.Format.GREY =>
				for(y <- 0 until image.height) {
					for(x <- 0 until image.width) {
						// flip the image upside down
						val pixel = image.pixels((image.height - y - 1) * image.width + x)

						buffer.put(t, (pixel << 24) | 0x00FFFFFF)
						t += 1
						p += 1
					}
					t += width - image.width
				}

			case Image.Format.RGB =>
				for(y <- 0 until image.height) {
					for(x <- 0 until image.width) {
						// flip the image upside down
						val pixel = image.pixels((image.height - y - 1) * image.width + x)

						// needs to be ABGR, stored in memory xRGB
						// so R and B must be swapped, and the x just made FF
						buffer.put(t, 0xff000000 |  // force opacity for good measure
						((pixel & 0xFF) << 16) |
						((pixel & 0xFF0000) >> 16) |
						(pixel & 0x0000FF00))
						t += 1
						p += 1
					}
					t += width - image.width
				}

			case Image.Format.ARGB =>
				for(y <- 0 until image.height) {
					for(x <- 0 until image.width) {
						
						// flip the image upside down
						val pixel = image.pixels((image.height - y - 1) * image.width + x)

						// needs to be ABGR stored in memory ARGB
						// so R and B must be swapped, A and G just brought back in
						buffer.put(t, ((pixel & 0xFF) << 16) |
						((pixel & 0xFF0000) >> 16) |
						(pixel & 0xFF00FF00))

						t += 1
						p += 1
					}
					t += width - image.width
				}
		}

		// put pixels into buffer and rewind
		buffer.rewind
	}

	def bind {
		// check if texture is still valid
		if(!isValid && this.id != Texture.UNDEFINED) {
			warn("need to recreate texture "+ this.id)
			this.id = Texture.UNDEFINED
			needsUpdate = true
		}

		if(needsUpdate) updateTexture

		if(!isValid) return
		gl.glEnable(GL.GL_TEXTURE_2D)
		gl.glBindTexture(GL.GL_TEXTURE_2D, this.id)
	}

	def unbind {
		if(!isValid) return
		gl.glDisable(GL.GL_TEXTURE_2D)
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0)
	}
	
	def create {
		val ids = new Array[Int](1)
		gl.glGenTextures(ids.length, ids, 0)
		this.id = ids(0)
	}

	def destroy { 
		if(isValid)
			gl.glDeleteTextures(1, Array(id), 0)
	}
	
	//  import java.nio.Buffer
	//  
	//  def data(format:Int, width:Int, height:Int, data:Buffer) {
	//    if(!isValid) return
	//    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 
	//                    format, width, height, 0, format, GL.GL_UNSIGNED_BYTE, data)
	//  }
}
