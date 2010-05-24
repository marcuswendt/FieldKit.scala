/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created July 05, 2009 */
package field.kit.math

import field.kit._
import java.util.Random
	
/**
 * Companion object to <code>Vec3</code>
 * @author Marcus Wendt
 */
object Vec3 {
	val ZERO = new Vec3(0, 0, 0)
	val UNIT_X = new Vec3(1, 0, 0)
	val UNIT_Y = new Vec3(0, 1, 0)
	val UNIT_Z = new Vec3(0, 0, 1)
	val UNIT_XYZ = new Vec3(1, 1, 1)

	// factory methods
	def apply() = new Vec3(0,0,0)
	def apply(s:Float) = new Vec3(s,s,s)
	def apply(v:Vec) = new Vec3(v.x, v.y, v.z)
	def apply(s:String) = { val v = new Vec3(0,0,0); v := s; v }

	/**
	* Creates a new random unit vector
	* @return a new random normalized unit vector.
	*/
	def random(r:Random = null) = new Vec3(randomNormal(r), randomNormal(r), randomNormal(r))
}

/**
* 3 Dimensional Float Vector
* @author Marcus Wendt
*/
case class Vec3(var x:Float, var y:Float, var z:Float) extends Vec {
	import java.nio.FloatBuffer
	import Common._
	
	// zero constructor to allow argument-less inheritance
	def this() = this(0,0,0)

	// -- Setters --------------------------------------------------------------
	/** 
	* Sets this Vectors components to the given Vec3
	* @return itself
	*/
	def :=(v:Vec) = { this.x=v.x; this.y=v.y; this.z=v.z; this }

	/** 
	* Sets this Vectors components to the given Vec3
	* @return itself
	*/
	final def set(v:Vec) = :=(v)

	/**
	* Sets all components of this Vector to the given Float
	* @return itself
	*/
	def :=(s:Float) = { this.x = s; this.y = s; this.z = s; this }

	/**
	* Sets all components of this Vector to the given Float
	* @return itself
	*/
	final def set(s:Float) = :=(s)

	/** 
	* Sets the xyz components to the data from a given buffer at a given index
	* @return itself
	*/
	final def :=(buffer:FloatBuffer, index:Int) = {
		val i = index * 3
		this.x = buffer get i
		this.y = buffer get i + 1
		this.z = buffer get i + 2
		this
	}

	/** 
	* Sets the xyz components to the data from a given buffer at a given index
	* @return itself
	*/
	final def set(buffer:FloatBuffer, index:Int) = :=(buffer,index)

	/** 
	* Attempts to parse the given String to set this Vectors components
	* @return itself
	*/
	final def :=(s:String) = {
		// TODO reimplement this method
		if(s != null) {
			val iter = DECIMAL.findAllIn(s)
			val list = iter.toList
	
			var index = 0
			def next = { 
				var f = abs(list(index).toFloat)
				index += 1
				f
			}

			list.size match {
				// set xyz to a scalar
				case 1 =>
					val f = next
					this.x = f 
					this.y = f
					this.z = f
					
				// set xyz independently
				case 3 =>
					this.x = next 
					this.y = next
					this.z = next
					
				case _ => throw new Exception("Couldnt parse String '"+ s +"'")
			} 
		}
		this
	}

	/** 
	* Attempts to parse the given String to set this Vectors components
	* @return itself
	*/
	final def set(s:String) = :=(s)

	// -- Immutable Operations -------------------------------------------------
	/**
	* Adds the given Float to this vector
	* @return result as new vector 
	*/
	final def +(s:Float) = new Vec3(x + s, y + s, z + s)

	/**
	* Subtracts the given float from this vector
	* @return result as new vector 
	*/
	final def -(s:Float):Vec3 = new Vec3(x - s, y - s, z - s)

	/** 
	* Multiplies the given float with this vector
	* @return result as new vector 
	*/
	final def *(s:Float):Vec3 = new Vec3(x * s, y * s, z * s)

	/** 
	* Divides this vector through the given float
	* @return result as new vector 
	*/
	final def /(s:Float):Vec3 = new Vec3(x / s, y / s, z / s)

	/** 
	* Subtracts the given <code>Vec3</code> from this <code>Vec3</code> and returns the result
	* @return result
	*/
	final def -(v:Vec) = new Vec3(this.x - v.x, this.y - v.y, this.z - v.z)

	/** 
	* Adds the given <code>Vec3</code> to this <code>Vec3</code> and returns the result 
	* @return result
	*/
	final def +(v:Vec) = new Vec3(this.x + v.x, this.y + v.y, this.z + v.z)

	/** 
	* Multiplies the given <code>Vec3</code> with this <code>Vec3</code> and returns the result
	* @return result
	*/
	final def *(v:Vec) = new Vec3(this.x * v.x, this.y * v.y, this.z * v.z)

	/** 
	* Divides this <code>Vec3</code> through the given <code>Vec3</code> and returns the result
	* @return result
	*/
	final def /(v:Vec) = new Vec3(this.x / v.x, this.y / v.y, this.z / v.z)


	// -- Mutable Operations ---------------------------------------------------
	final def +=(s:Float) = { x+=s; y+=s; z+=s; this }
	final def -=(s:Float) = { x-=s; y-=s; z-=s; this }
	final def *=(s:Float) = { x*=s; y*=s; z*=s; this }
	final def /=(s:Float) = { x/=s; y/=s; z/=s; this }

	final def +=(v:Vec) = { x+=v.x; y+=v.y; z+=v.z; this }
	final def -=(v:Vec) = { x-=v.x; y-=v.y; z-=v.z; this }
	final def *=(v:Vec) = { x*=v.x; y*=v.y; z*=v.z; this }
	final def /=(v:Vec) = { x/=v.x; y/=v.y; z/=v.z; this }


	// -- Other Operations -----------------------------------------------------
	/** Resets this vectors components all to zero */
	final def zero = { x=0; y=0; z=0 }

	/**
	* Calculates the dot product of this vector with a provided vector.
	* @return the resultant dot product of this vector and a given vector.
	*/
	final def dot(v:Vec) = x * v.x + y * v.y + z * v.z

	/**
	* Calculates the cross product of this vector with a parameter vector v.
	* @return the cross product vector
	*/
	final def cross(v:Vec3, result:Vec3) = {
		val rx = (y * v.z) - (z * v.y)
		val ry = (z * v.x) - (x * v.z)
		val rz = (x * v.y) - (y * v.x)

		val r = if(result==null) Vec3() else result
		r.set(rx,ry,rz)
		r
	}

	/**
	* Calculates the cross product of this vector with a parameter vector v.
	* @return the cross product as a new vector
	*/
	final def cross(v:Vec3):Vec3 = cross(v, null)

	final def length = sqrt(lengthSquared).toFloat

	final def lengthSquared = x * x + y * y + z * z

	final def distance(v:Vec) = sqrt(distanceSquared(v)).toFloat

	final def distanceSquared(v:Vec) = {
		val dx = x - v.x
		val dy = y - v.y
		val dz = z - v.z
		dx * dx + dy * dy + dz * dz
	}

	/**
	* Normalises this vector so that its magnitude = 1.
	* @return itself
	*/
	final def normalize = {
		val l = length
		if(l != 0)
			this /= l
		else
			this /= 1
		this
	}

	/**
	* Normalizes the vector to the given length.
	* @return itself
	*/
	final def normalizeTo(len:Float) = {
		var mag = sqrt(x * x + y * y + z * z).toFloat
		if(mag > 0) {
			mag = len / mag
			x *= mag
			y *= mag
			z *= mag
		}
		this
	}

	/** <code>angleBetween</code> returns (in radians) the angle between two vectors.
	*  It is assumed that both this vector and the given vector are unit vectors (iow, normalized). */
	final def angleBetween(v:Vec3) {
		val dotProduct = dot(v)
		acos(dotProduct).toFloat
	}

	/** makes sure this vector does not exceed a certain length */
	final def clamp(max:Float) = {
		val l = length
		if(l > max) {
		this /= l
		this *= max
		}
		this
	}

	/**
	* interpolates towards the given target Vector
	* @see <a href="http://en.wikipedia.org/wiki/Slerp">spherical linear interpolation</a>
*/
	final def slerp(target:Vec3, delta:Float) = {
		this.x = x * (1 - delta) + target.x * delta
		this.y = y * (1 - delta) + target.y * delta
		this.z = z * (1 - delta) + target.z * delta
		this
	}

	/**
	* Interpolates the vector towards the given target vector
	* using linear interpolation.
	*/
	final def interpolate(target:Vec3, delta:Float) = {
		x += (target.x - x) * delta
		y += (target.y - y) * delta
		z += (target.z - z) * delta
		this
	}

	/**
	 * TODO add comments
	 * @return
	 */
	final def jitter(amount:Float, r:Random = null) = {
		x += random(r) * amount
		y += random(r) * amount
		z += random(r) * amount
		this
	}
	
	/**
	 * TODO add comments
	 * @return
	 */
	final def randomize(r:Random = null) = {
		x = randomNormal
		y = randomNormal
		z = randomNormal
		this
	}

	/**
	 * TODO add comments
	 * @return
	 */
	final def randomizeTo(len:Float, r:Random = null) = {
		randomize(r)
		normalizeTo(len)
	}

	/** checks wether one or several components of this vector are Not a Number or Infinite */
	final def isValid:Boolean = { 
		import java.lang.Float
		if (Float.isInfinite(x)) return false
		if (Float.isInfinite(y)) return false
		if (Float.isInfinite(z)) return false
		if (Float.isNaN(x)) return false
		if (Float.isNaN(y)) return false
		if (Float.isNaN(z)) return false
		true
	}

	// -- Buffers --------------------------------------------------------------
	/**
	* Puts this vector  at the given postion into a <code>FloatBuffer</code>
	*/
	final def put(buffer:FloatBuffer, index:Int) = {
		val i = index * 3
		buffer put (i, x)
		buffer put (i+1, y)
		buffer put (i+2, z)
		this    
	}

	/**
	* Puts this vector into the given <code>FloatBuffer</code>
	*/
	final def put(buffer:FloatBuffer) = {
		buffer put x
		buffer put y
		buffer put z
		this    
	}

	// -- Misc -----------------------------------------------------------------
	final def tuple = (x, y, z)

	override def clone = new Vec3(x,y,z)

	override def toString = "Vec3["+ toLabel +"]"

	def toLabel = "X"+ x +" Y"+ y +" Z"+ z
}