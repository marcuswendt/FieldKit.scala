/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.util

/** utility methods for dealing with java.nio buffers */
object BufferUtil {
  import java.nio.ByteBuffer
  import java.nio.ByteOrder
  import java.nio.FloatBuffer
  import java.nio.IntBuffer

  def byte(size:Int) = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder)
  
  def int(size:Int) = {
    val bytes = size * (Integer.SIZE / 8)
    byte(bytes).asIntBuffer
  }
  
  def float(size:Int) = {
    val bytes = size * (java.lang.Float.SIZE / 8)
    byte(bytes).asFloatBuffer
  }
  
  def vec2(size:Int) = float(size * 2)
  def vec3(vertices:Int) = float(vertices * 3)
  def colour(size:Int) = float(size * 4)
  
  def put(buf:FloatBuffer, src:Float*) = buf.put(src.toArray)
}
