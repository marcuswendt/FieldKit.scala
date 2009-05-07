/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene.shape

import field.kit.gl.scene._

object Stroke2D {
	object Side extends Enumeration {
	  val LEFT = Value
	  val RIGHT = Value
	}
	
	object Cap extends Enumeration {
	  val FLAT = Value
	  val ACUTE = Value
	}  
}

/** 
 * A dynamic stroke with variable thickness 
 * 
 * The original code was written in Java for the NervousInk drawing tool
 * @see <a href="http://www.field.io/project/nervous-ink">Nervous Ink</a>
 */
class Stroke2D(name:String, defaultCapacity:Int) extends Geometry(name) {
  import java.nio.FloatBuffer
  import field.kit.math._
  
  /** line end/start style */
  var capMode = Stroke2D.Cap.FLAT
  
  /** current number of points */
  var length = 0
  
  /** 2d vertex data storage */
  var points:FloatBuffer = null
  
  /** 1d weights data storage */
  var weights:FloatBuffer = null
  
  /** length * 2 number of 2d vertices */
  var outline:FloatBuffer = null
  
  // temporary
  private val v1 = new Vec2
  private val v2 = new Vec2
  private val v3 = new Vec2
  private val v4 = new Vec2
  private val v5 = new Vec2
  private val vIntersect = new Vec2
  
  private val vPrev = new Vec2
  private val vCur = new Vec2
  private val vNext = new Vec2
  
  private val p1 = new Vec2
  private val p2 = new Vec2
  
  // resize to default
  allocate(defaultCapacity)
  
  override def allocate(capacity:Int) {
    import field.kit.util.BufferUtil
    this.capacity = capacity
    points = BufferUtil.vec2(capacity)
    weights = BufferUtil.float(capacity)
    outline = BufferUtil.vec2(capacity * 2)
  }
  
  /** adds a point to this stroke and incrementally updates the outline */
  def +=(x:Float, y:Float, weight:Float) {
    // dont add when buffers are full
    if(length < capacity-1) {
      // skip point when distance to previous point is < minDistance
      var isTooClose = false
      if(length > 0) {
        val minDistance = weight * 0.5f
        val dist = v1.set(points, length - 1).distance(x, y)
        isTooClose = dist < minDistance
      }
      
      if(!isTooClose) {
        val index = length * 2
		points.put(index, x).put(index + 1, y)
		weights.put(length, weight)
		length += 1
		
		// cant inflate with less than 2 points
		if(length > 2) {
		  //for (int i = length - 1; i > length - 3; i--) {	
		  for(i <- length-3 until length-1) {
			calcOutlinePoint(Stroke2D.Side.LEFT, i, weight)
			calcOutlinePoint(Stroke2D.Side.RIGHT, i, -weight)
		  }
		}
      }
    }
  }
  
  /** updates all points of this stroke */
  def update() {
    if(length > 2) {
      points.rewind
      weights.rewind
      for(i <- 0 until length) {
        val weight = weights.get(i)
        calcOutlinePoint(Stroke2D.Side.LEFT, i, weight)
        calcOutlinePoint(Stroke2D.Side.RIGHT, i, -weight)
      }
    }
  }
  
  /** resets this stroke */
  override def clear {
    super.clear
    length = 0
    points.rewind
    weights.rewind
    outline.rewind
  }
  
  //
  // Line Inflation
  //
  def outlineIndex(side:Stroke2D.Side.Value, index:Int) = 
    index * 2 + side.id
  
  protected def calcOutlinePoint(side:Stroke2D.Side.Value, i:Int, weight:Float) {
    vPrev.set(points, Math.max(0, i-1))
    vCur.set(points, i)
    vNext.set(points, i + 1)
    
    val index = outlineIndex(side, i)
    
    // start
    if(i == 0) {
      capMode match {
      	case Stroke2D.Cap.FLAT =>
      	  v1(vNext)
      	  v1 -= vCur

      	  v2(v1)
      	  v2.perpendiculate
      	  v2.normalize
      	  v2 *= weight

      	  p1(vCur)
      	  p1 += v2
        
      	case Stroke2D.Cap.ACUTE =>
      	  p1(vCur)
      }
      p1.put(outline, index)
      
    // end
    } else if(i == length-1) {
      capMode match {
      	case Stroke2D.Cap.FLAT =>
      	  v1(vPrev)
      	  v1 -= vCur

      	  v2(v1)
      	  v2.perpendiculate
      	  v2.normalize
      	  v2 *= -weight

      	  p1(vCur)
      	  p1 += v2
        
      	case Stroke2D.Cap.ACUTE =>
      	  p1(vCur)
      }
      p1.put(outline, index)
      
    // inbetween
    } else {
      // vector PREV->CUR
      v1(vCur)
      v1 -= vPrev
      
      // perpendicular of v1
      v2(v1)
      v2.perpendiculate
      v2.normalize
      v2 *= weight
      
      p1(vCur)
      p1 += v2
   
      // vector NEXT->CUR
      v3(vCur)
      v3 -= vNext
      
      // perpendicular of v3
      v4.set(v3)
      v4.perpendiculate
      v4.normalize
      v4 *= -weight
      
      p2(vCur)
      p2 += v4
      
      val d = p1.distanceSquared(p2)
      
      if(d < 1f)
        vIntersect(p1)
      else
        Vec2.rayIntersectionPoint(p1, v1, p2, v3, vIntersect)
      
      if(vIntersect.isNaNOrInfinite) {
        vIntersect(vCur)
        vIntersect += v2
      }
      
      v5(vCur)
      v5 -= vIntersect
      val l = v5.length
      val absWeight = Math.abs(weight * 2)
      
      if(l > absWeight) {
        v5.normalize
        v5 *= -absWeight
        v5 += vCur
      } else {
        v5(vIntersect)
      }
      
      //result
      v5.put(outline, index)
    }
  }
  
  //
  // Render
  //
  def draw {
    import javax.media.opengl.GL
    
    gl.glPushMatrix
    gl.glTranslatef(translation.x, translation.y, translation.z)
    gl.glScalef(scale.x, scale.y, scale.z)
    gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
    
    enableStates
    
    // enable gl vertex & texture coord arrays
    gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    
    outline.rewind
    gl.glVertexPointer(2, GL.GL_FLOAT, 0, outline)
    gl.glDrawArrays(GL.GL_QUAD_STRIP, 0, length * 2)
    
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    
    disableStates
    
    gl.glPopMatrix
  }
}
