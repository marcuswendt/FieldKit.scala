/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created December 10, 2009 */
package field.kit.math.packing

import field.kit.math.Vec3
import field.kit.math.geometry.AABB

/**
 * Base class for all packing shape adapters
 */
trait ShapeAdapter extends AABB {
  
  /** @return true when inside the shape defined by this shape, false otherwise */
  def isInside(x:Float, y:Float, z:Float):Boolean
}

import java.awt.image.BufferedImage

/**
 * Treats an Image as black & white pack map where all pixels above a certain
 * threshold [0, 1] are considered inside the packing shape
 * 
 * NOTE: This class only considers the first / red-channel of an image
 * You might want to subclass it and override the valueAt method for more advanced thresholding
 */
class BufferedImageShapeAdapter(image:BufferedImage, var threshold:Float) extends ShapeAdapter {
  
  extent = new Vec3(image.getWidth, image.getHeight, 0)
  
  val raster = image.getRaster
  
  def isInside(x:Float, y:Float, z:Float) = valueAt(x.toInt, y.toInt) < threshold
  
  def valueAt(x:Int, y:Int) = raster.getSampleFloat(x, y, 0)
  
}