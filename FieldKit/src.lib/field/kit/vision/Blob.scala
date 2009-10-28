/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 28, 2009 */
package field.kit.vision

/**
 * Represents a single tracked object in the Vision library
 * @author Marcus Wendt
 */
class Blob(val id:Int) extends math.Vec3 {
  import math.geometry.Rect
  import util.Buffer
  
  var active = false
  val bounds = new Rect(0,0,0,0)
  val contour = Buffer.float(Vision.CONTOUR_DATA_MAX)
  var contourPoints = 0
}
