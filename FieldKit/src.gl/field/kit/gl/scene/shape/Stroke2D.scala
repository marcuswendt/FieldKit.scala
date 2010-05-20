/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene.shape

import field.kit._
import field.kit.gl._
import field.kit.gl.scene._

import field.kit.util.Buffer

import java.nio.FloatBuffer

	
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
class Stroke2D(name:String, defaultCapacity:Int) extends Mesh(name) {
	
	/** line end/start style */
	var capMode = Stroke2D.Cap.FLAT

	/** current number of points */
	var length = 0

	/** 2d vertex data storage */
	var points:FloatBuffer = null

	/** 1d weights data storage */
	var weights:FloatBuffer = null

	/** the maximum number of vertices this geometry object can hold */
	protected var capacity = 0

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
	init(defaultCapacity)

	def init(capacity:Int) {
		this.capacity = capacity
		points = Buffer.vec2(capacity) 
		weights = Buffer.float(capacity)
		
		val verts = capacity * 2
		data.allocVertices(verts)
		data.allocNormals(verts)
		data.allocTextureCoords(verts)
		
		data.indexModes(0) = IndexMode.QUAD_STRIP 
	}

	/** adds a point to this stroke and incrementally updates the outline */
	def +=(x:Float, y:Float, weight:Float) {
		// dont add when buffers are full
		if(length < capacity-1) {
			// skip point when distance to previous point is < minDistance
			var isTooClose = false
			if(length > 0) {
				val minDistance = weight * 0.5f
				val dist = (v1 := (points, length - 1)) distance (x, y)
				isTooClose = dist < minDistance
			}

			if(!isTooClose) {
				val index = length * 2
				points.put(index, x).put(index + 1, y)
				weights.put(length, weight)
				length += 1
		
				// cant inflate with less than 2 points
				if(length > 2) {
					val length3 = length-3
					val length1 = length-1
					
					for(i <- length3 until length1) {
						calcOutlineVertex(Stroke2D.Side.LEFT, i, weight)
						calcOutlineVertex(Stroke2D.Side.RIGHT, i, -weight)
					}
				}
			}
		}
	}

	/** updates all points of this stroke */
	def update() {
		if(length <= 2) return
		
		points.rewind
		weights.rewind
		var i = 0
		while(i < length) {
			val weight = weights.get(i)
			calcOutlineVertex(Stroke2D.Side.LEFT, i, weight)
			calcOutlineVertex(Stroke2D.Side.RIGHT, i, -weight)
			
			// texture coords
			val textureCoords = data.textureCoords(0)
			val tcS = i/length.toFloat
			textureCoords.position(i * 2 * 2)
			textureCoords.put(tcS).put(0).put(tcS).put(1)
			
			i += 1
		}
		
		data.vertexCount = length * 2
	}

	/** resets this stroke */
	def clear {
		length = 0
		points.rewind
		weights.rewind
		
		data.vertices.clear
		data.refresh
	}

	//
	// Line Inflation
	//
	protected def calcOutlineVertex(side:Stroke2D.Side.Value, i:Int, weight:Float) {
		
		def outlineIndex(side:Stroke2D.Side.Value, index:Int) = index * 2 + side.id
		
		def insertVertex(v:Vec2, index:Int) {
			val i = index * 3
			data.vertices.put(i, v.x)
			data.vertices.put(i + 1, v.y)
		}
		
		vPrev := (points, max(0, i-1))
		vCur := (points, i)
		vNext := (points, i + 1)

		val index = outlineIndex(side, i)

		// start
		if(i == 0) {
			capMode match {
				case Stroke2D.Cap.FLAT =>
					v1 := vNext
					v1 -= vCur
			
					v2 := v1
					v2.perpendiculate
					v2.normalize
					v2 *= weight
			
					p1 := vCur
					p1 += v2
		
				case Stroke2D.Cap.ACUTE =>
					p1 := vCur
			}
			insertVertex(p1, index)

		// end
		} else if(i == length-1) {
			capMode match {
				case Stroke2D.Cap.FLAT =>
					v1 := vPrev
					v1 -= vCur
			
					v2 := v1
					v2.perpendiculate
					v2.normalize
					v2 *= -weight
			
					p1 := vCur
					p1 += v2
		
				case Stroke2D.Cap.ACUTE =>
					p1 := vCur
			}
			insertVertex(p1, index)

		// inbetween
		} else {
			// vector PREV->CUR
			v1 := vCur
			v1 -= vPrev
	
			// perpendicular of v1
			v2 := v1
			v2.perpendiculate
			v2.normalize
			v2 *= weight
	
			p1 := vCur
			p1 += v2
	
			// vector NEXT->CUR
			v3 := vCur
			v3 -= vNext
	
			// perpendicular of v3
			v4 := v3
			v4.perpendiculate
			v4.normalize
			v4 *= -weight
	
			p2 := vCur
			p2 += v4
	
			val d = p1.distanceSquared(p2)
	
			if(d < 1f)
				vIntersect := p1
			else
				Vec2.rayIntersectionPoint(p1, v1, p2, v3, vIntersect)
	
			if(!vIntersect.isValid) {
				vIntersect := vCur
				vIntersect += v2
			}
	
			v5 := vCur
			v5 -= vIntersect
			val l = v5.length
			val absWeight = abs(weight * 2)
	
			if(l > absWeight) {
				v5.normalize
				v5 *= -absWeight
				v5 += vCur
			} else {
				v5 := vIntersect
			}
	
			insertVertex(v5, index)
		}
	}

//	//
//	// Render
//	//
//	override def draw {
//		import javax.media.opengl.GL
//
//		gl.glPushMatrix
//		gl.glTranslatef(translation.x, translation.y, translation.z)
//		gl.glScalef(scale.x, scale.y, scale.z)
//		gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
//
//		enableStates
//
//		// enable gl vertex & texture coord arrays
//		gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
//
//		data.vertices.rewind
//		gl.glVertexPointer(3, GL.GL_FLOAT, 0, data.vertices)
//		gl.glDrawArrays(GL.GL_QUAD_STRIP, 0, length * 2)
//
//		gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
//
//		disableStates
//
//		gl.glPopMatrix
//	}
}
