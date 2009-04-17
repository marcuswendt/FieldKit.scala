/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 16, 2009 */
package field.kit.math.geometry

/**
 * A Catmull-Rom spline (which is a special form of the cubic hermite curve) implementation, 
 * generates a smooth curve/interpolation from a number of points. 
 * 
 * Used in keyframe-based animation and graphics
 * 
 * @see <a href="http://www.mvps.org/directx/articles/catmull/">Catmull-Rom Splines</a>
 * @see <a href="http://en.wikipedia.org/wiki/Spline_(mathematics)">Spline (mathematics)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Hermite_curve#Catmull.E2.80.93Rom_spline">Hermite Curve</a>
 */
class Spline(capacity:Int) extends Curve(capacity) {
  import field.kit.math._
  
  // internal fields used for calculating points on the curve
  private val tmp0 = new Vec3
  private val tmp1 = new Vec3
  private val tmp2 = new Vec3
  private val tmp3 = new Vec3
  private val tmp4 = new Vec3
  
  private val first = new Vec3
  private val second = new Vec3
  private val beforeLast = new Vec3
  private val last = new Vec3
  
  private var needsUpdate = false
  
  /** alternative constructor */
  def this() = this(100)
  
  override def +=(x:Float, y:Float, z:Float) {
    super.+=(x,y,z)
    needsUpdate = true
  }
  
  override def clear {
    super.clear
    needsUpdate = true
  }
  
  /** needs to be called when modifcations to the control vertices were made */
  def update {
    needsUpdate = false
    vertex(0, first)
    vertex(1, second)
    vertex(size-2, beforeLast)
    vertex(size-1, last)
  }
  
  /**
   * <code>point</code> calculates a point on a Catmull-Rom curve from a
   * given time value within the interval [0, 1]. If the value is zero or less,
   * the first control point is returned. If the value is one or more, the last
   * control point is returned. Using the equation of a Catmull-Rom Curve, the
   * point at the interval is calculated and returned.
   * 
   * @see field.kit.math.geometry.Curve#point(float)
   */
  def point(time:Float, p:Vec3) = {
    if(size > 0) {
      if(needsUpdate) update
      
      // first point
      if(time < 0) {
        p(first)
        
      // last point
      } else if(time > 1) {
        p(last)
        
      // in between
      } else {
        val partPercentage = 1.0f / (size - 1)
        val timeBetween = time / partPercentage
        var i = FMath.floor(timeBetween).asInstanceOf[Int]
        
        val normalizedTime = timeBetween - i
        
        val t = normalizedTime * 0.5f
        val t2 = t * normalizedTime
        val t3 = t2 * normalizedTime
        i -= 1
        
        if(i == -1) {
          tmp0(second).-=(first).normalize.*=(0.000001f)
          tmp1(first) -= tmp0
        } else {
          tmp1.set(vertices, i)
        }
        
        i += 1
        tmp2.set(vertices, i)
        
        i += 1
        tmp3.set(vertices, i)
        
        i += 1
        if(i == size) {
          tmp0(beforeLast).-=(last).normalize.*=(0.000001f)
          tmp4(last) -= tmp0
        } else {
          tmp4.set(vertices, i)
        }
        
        // calculate point
        tmp1 *= -t3 + 2 * t2 -t
        p(tmp1)
        
        tmp2 *= 3 * t3 - 5 * t2 + 1
        p += tmp2
        
        tmp3 *= -3 * t3 + 4 * t2 + t
        p += tmp3
        
        tmp4 *= t3 - t2
        p += tmp4
      }
    }
    p
  }
}