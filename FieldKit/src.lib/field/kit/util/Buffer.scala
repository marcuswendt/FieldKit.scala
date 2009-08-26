/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.util

/** 
 * utility methods for dealing with java.nio buffers
 * @author Marcus Wendt
 */
object Buffer {
  import java.nio.ByteBuffer
  import java.nio.ByteOrder
  import java.nio.FloatBuffer
  import java.nio.IntBuffer

  import math.Vec3
  
  def apply(size:Int) = 
    java.nio.ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder)
  
  // -- Primitive Types --------------------------------------------------------
  def byte(size:Int) = apply(size)
  
  def int(size:Int) = {
    val bytes = size * (Integer.SIZE / 8)
    apply(bytes).asIntBuffer
  }
  
  def float(size:Int) = {
    val bytes = size * (java.lang.Float.SIZE / 8)
    apply(bytes).asFloatBuffer
  }

  // -- Complex Types ----------------------------------------------------------
  def vertex(size:Int) = float(size * 3)
  def normal(size:Int) = float(size * 3)
  def coord(size:Int) = float(size * 2)
  def colour(size:Int) = float(size * 4)
  def index(size:Int) = int(size)
  
  def vec2(size:Int) = float(size * 2)
  def vec3(size:Int) = float(size * 3)
  
  // ---------------------------------------------------------------------------
  def put(v:Vec3, buf:FloatBuffer, index:Int) {
    val i = index * 3
    buf put (i, v.x)
    buf put (i + 1, v.y)
    buf put (i + 2, v.z)
  }
  
  /**
   * Copies floats from one position in the buffer to another.
   * 
   * @param buf
   *            the buffer to copy from/to
   * @param from
   *            the starting point to copy from
   * @param to
   *            the starting point to copy to
   * @param length
   *            the number of floats to copy
   */
  def copy(buf:FloatBuffer, from:Int, to:Int, length:Int) {
    val data = new Array[Float](length)
    buf.position(from)
    buf.get(data)
    buf.position(to)
    buf.put(data)
  }
  
  /**
   * Copies a Vector3 from one position in the buffer to another. The index values are in terms of vector number (eg,
   * vector number 0 is positions 0-2 in the FloatBuffer.)
   * 
   * @param buf
   *            the buffer to copy from/to
   * @param from
   *            the index of the vector to copy
   * @param to
   *            the index to copy the vector to
   */
  def copyVec3(buf:FloatBuffer, from:Int, to:Int) = 
    copy(buf, from * 3, to * 3, 3)
}