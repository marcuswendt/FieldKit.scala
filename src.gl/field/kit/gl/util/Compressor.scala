/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 19, 2009 */
package field.kit.gl.util

import field.kit._
import scala.actors.Actor

/**
* an actor based image compressor uses an actor per cpu core to parallize compression of BufferedImages via ImageIO
*/
object Compressor extends Actor with Logger {
	import scala.actors.Actor._
	import scala.collection.mutable.ArrayBuffer

	import java.io.File
	import java.nio.ByteBuffer
	import java.awt.image.DataBufferByte
	import java.awt.image.BufferedImage

	// fields
	val threads = Runtime.getRuntime.availableProcessors
	val processors = new Array[ProcessorState](threads)
	val images = new ArrayBuffer[ImageState]()
	val master = this
	
	/** the global compression quality */
	var quality = 1.0f

	protected var isInitialized = false

	start

	/** requests an image and buffer with the given dimensions */
	def init(width:Int, height:Int, alpha:Boolean) = {
		val list = images filter (s => s.isAvailable)
		val is = if(list.size == 0 ) {
					val is = new ImageState
					images += is
					is
				 } else {
					list(0)
				 }
	
		// check if we need to (re)create the image and its buffer
		is.init(width, height, alpha)
	
		// mark image as in use
		is.busy  
		(is.image, is.buffer)
	}

	def act {
		for(i <- 0 until processors.size) 
			processors(i) = new ProcessorState(new Processor(i))
	
		isInitialized = true
	
		loop {
			react {
				case Done(image) => 
					processors filter (_.processor == sender) foreach (_.free)
					images filter (_.image == image) foreach(_.free)
			}
		}
	}

	/**
	* queues another image for compression
	*/
	def apply(image:BufferedImage, format:String, file:File) = {
		// wait until this object is properly initialized 
		while(!isInitialized) {
			Thread.sleep(1)
		}
	
		// process image
		var isProcessing = false
		do {
			val p = processors filter (_.isAvailable)
			if(p.size > 0) {
				p(0).busy
				p(0).processor ! Compress(image, format, file)
				isProcessing = true
			} else {
				Thread.sleep(1)
			}
		} while(!isProcessing)
	}

	// ------------------------------------------------------------------------------------
	/** tells an image processor to get going*/
	case class Compress(image:BufferedImage, format:String, file:File)

	case class Done(image:BufferedImage)

	// ------------------------------------------------------------------------------------
	/** holds the availability state for the given element */
	abstract class StateRecord {
		var isAvailable = true
		def busy = isAvailable = false
		def free = isAvailable = true
	}

	protected class ProcessorState(var processor:Processor) extends StateRecord

	protected class ImageState extends StateRecord {
		var image:BufferedImage = null
		var buffer:ByteBuffer = null

		def init(width:Int, height:Int, alpha:Boolean) {
			var reinit = false
			if(image == null || image.getWidth != width || image.getHeight != height) {
				reinit = true
			}
			if(reinit) {
				val format = if(alpha) BufferedImage.TYPE_4BYTE_ABGR else BufferedImage.TYPE_3BYTE_BGR
				image = new BufferedImage(width, height, format)
				buffer = ByteBuffer.wrap((image.getRaster.getDataBuffer.asInstanceOf[DataBufferByte]).getData)
			}
		}
	}

	// ------------------------------------------------------------------------------------
	/** represents a single image processor that does the actual encoding work */
	protected class Processor(id:Int) extends Actor with Logger {
		import com.sun.opengl.util.ImageUtil
		import javax.imageio._
		import javax.imageio.plugins.jpeg.JPEGImageWriteParam
		import java.util.Locale
		
		logName = "Processor("+ id +")"

		start

		def act {
			loop {
				react {
					case Compress(image, format, file) =>
						info(file, "...")
						ImageUtil.flipImageVertically(image)
				
						format match {
							case "jpg" =>
								compressJpegImage(image, file)
								
							case _ =>
								ImageIO.write(image, format, file)
						}
						
						master ! Done(image)
				}
			}
		}
		
		protected def compressJpegImage(image:BufferedImage, file:File) {

			// Find a jpeg writer
			var writer:ImageWriter = null
			val iter = ImageIO.getImageWritersByFormatName("jpg")
			if(iter.hasNext())
			writer = iter.next.asInstanceOf[ImageWriter]
	
			// Prepare output file
			val ios = ImageIO.createImageOutputStream(file)
			writer.setOutput(ios)
	
			// Set the compression quality
			val iwparam = new JPEGImageWriteParam(Locale.getDefault)
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT)
			iwparam.setCompressionQuality(quality)
	
			// Write the image
			writer.write(null, new IIOImage(image, null, null), iwparam)
	
			// Cleanup
			ios.flush
			writer.dispose
			ios.close
		}
		
//		class MyImageWriteParam extends JPEGImageWriteParam(Locale.getDefault) {
//		
//		    // This method accepts quality levels between 0 (lowest) and 1 (highest) and simply converts
//		    // it to a range between 0 and 256; this is not a correct conversion algorithm.
//		    // However, a proper alternative is a lot more complicated.
//		    // This should do until the bug is fixed.
//		    override def setCompressionQuality(quality:Float) {
//		        if (quality < 0.0F || quality > 1.0F) {
//		            throw new IllegalArgumentException("Quality out-of-bounds!")
//		        }
//		        this.compressionQuality = 256 - (quality * 256)
//		    }
//		}
	}
}