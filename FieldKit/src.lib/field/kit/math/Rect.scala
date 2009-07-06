/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 06, 2009 */
package field.kit.math

/**
 * Represents a 2D rectangle 
 * @author Marcus Wendt
 */
class Rect(var x1:Float, var y1:Float, var width:Float, var height:Float) {
  def x2 = x1 + width
  def y2 = y1 + height
  
  def this() = this(0f,0f,0f,0f)
  
  def intersects(r:Rect):Boolean = {
    //if(width <= 0 || height <= 0) return false
    r.x2 > x1 && 
    r.y2 > y1 &&
    r.x1 < x2 && 
    r.y1 < y2
  }
  
  /** @return true when this rectangle is inside the given rectangle **/
  def contains(r:Rect):Boolean = {
    r.x1 >= x1 && 
    r.y1 >= y1 &&
    r.x2 <= x2 && 
    r.y2 <= y2
  }
  
  override def toString = 
    "Rect[x1: "+ x1 +" y1: "+ y1 +" w: "+ width +" h: "+ height +"]"
}