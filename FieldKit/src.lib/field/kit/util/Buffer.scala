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
}