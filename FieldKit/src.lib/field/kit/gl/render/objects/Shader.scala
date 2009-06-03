/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 23, 2009 */
package field.kit.gl.render.objects

/** Base class for all types of GLSL shaders */
abstract class Shader extends GLObject {
  import javax.media.opengl.GL
  
  def bind {}
  
  def unbind {}
  
  def destroy {}
  
  protected def compile(source:String) {
    gl.glShaderSource(id, 1, Array(source), null.asInstanceOf[Array[Int]], 0)
    gl.glCompileShader(id)
    
    // check for error
    val compileStatus = Array(1)
    gl.glGetObjectParameterivARB(id, GL.GL_OBJECT_COMPILE_STATUS_ARB, compileStatus, 0)
    if(compileStatus(0) == GL.GL_FALSE) {
      val infologlength = Array(1)
      val infolog = new Array[Byte](1024)
      gl.glGetInfoLogARB(id, 1024, infologlength, 0, infolog, 0)
      error(new String(infolog))
    }
  }
}

/** A GLSL fragment shader */
class FragmentShader extends Shader {
  import javax.media.opengl.GL
  def create =
    id = gl.glCreateShader(GL.GL_FRAGMENT_SHADER)
}

/** A GLSL vertex shader */
class VertexShader extends Shader {
  import javax.media.opengl.GL
  def create =
    id = gl.glCreateShader(GL.GL_VERTEX_SHADER)
}

/*
 Not supported yet
class GeometryShader extends Shader {
  import javax.media.opengl.GL
  def create =
    id = gl.glCreateShader(GL.GL_GEOMETRY_SHADER)
}
*/

/** A <code>ShaderProgram</code> combines several Shaders into one executeable Program on the GPU */
class ShaderProgram extends GLObject {
  def create = 
    id = gl.glCreateProgram
  
  /** attaches the given shader to this program */
  def attach(shader:Shader) = 
    gl.glAttachShader(id, shader.id)
  
  /** call this after creating and attaching all shaders */
  def init = {
    gl.glLinkProgram(id)
    gl.glValidateProgram(id)
  }
  
  def destroy = 
    gl.glDeleteProgram(id)
  
  def bind = 
    gl.glUseProgram(id)
  
  def unbind = 
    gl.glUseProgram(0)
}
