/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 01, 2009 */
package field.kit.gl.render.objects

/** Thrown when a GLSL <code>Shader</code> couldn't be compiled. */
class ShaderCompileException(info:String) 
extends Exception(info) {}

/** Base class for all types of GLSL shaders */
abstract class Shader extends GLObject {
  import javax.media.opengl.GL
  
  def bind {}
  
  def unbind {}
  
  def destroy {}
  
  protected def compile(source:String) {
    if(source == null) {
      throw new ShaderCompileException("No source given!")
      return
    }
    
    if(id == GLObject.UNDEFINED) create
    
    gl.glShaderSource(id, 1, Array(source), null.asInstanceOf[Array[Int]], 0)
    gl.glCompileShader(id)
    
    // check for error
    val compileStatus = Array(1)
    gl.glGetObjectParameterivARB(id, GL.GL_OBJECT_COMPILE_STATUS_ARB, compileStatus, 0)
    if(compileStatus(0) == GL.GL_FALSE) {
      val infologlength = Array(1)
      val infolog = new Array[Byte](1024)
      gl.glGetInfoLogARB(id, 1024, infologlength, 0, infolog, 0)
      throw new ShaderCompileException(new String(infolog))
      //error(new String(infolog))
    }
  }
}


// -- Vertex -------------------------------------------------------------------

/** A GLSL vertex shader */
class VertexShader extends Shader {
  import javax.media.opengl.GL
  
  def this(source:String) = {
    this()
    compile(source)
  }
  
  def create =
    id = gl.glCreateShader(GL.GL_VERTEX_SHADER)
}

/** Companion object to class <code>VertexShader</code> */
object VertexShader {
  /** the default file-suffix for these types of shaders */
  val SUFFIX = "vs"
  
  /** the default fixed-pipe vertex shader */
  val DEFAULT = new VertexShader("""
	void main()
	{
		gl_FrontColor = gl_Color;
		gl_TexCoord[0] = gl_MultiTexCoord0;
		gl_Position = ftransform();
	} 
  """)
}


// -- Fragment -----------------------------------------------------------------

/** A GLSL fragment shader */
class FragmentShader extends Shader {
  import javax.media.opengl.GL
  
  def this(source:String) = {
    this()
    compile(source)
  }
  
  def create =
    id = gl.glCreateShader(GL.GL_FRAGMENT_SHADER)
}

/** Companion object to class <code>FragmentShader</code> */
object FragmentShader {
  /** the default file-suffix for these types of shaders */
  val SUFFIX = "fs"
  
  /** the default fixed-pipe fragment shader */
  val DEFAULT = new FragmentShader("""
	uniform sampler2D tex;
	
	void main()
	{
		gl_FragColor = gl_Color * texture2D(tex, gl_TexCoord[0].xy);
	}   
  """)
}


// -- Geometry -----------------------------------------------------------------

/*
 Not supported yet
class GeometryShader extends Shader {
  import javax.media.opengl.GL
  def create =
    id = gl.glCreateShader(GL.GL_GEOMETRY_SHADER)
}
  
 
// Companion object to class <code>GeometryShader</code>
object GeometryShader {
  // the default file-suffix for these types of shaders
  val SUFFIX = "gs"

  /** a default geometry shader */
  val DEFAULT = new GeometryShader("""
	void main()
	{
		gl_FrontColor = gl_FrontColorIn[0];
		gl_TexCoord[0] = gl_TexCoordIn[0][0];
		gl_Position = gl_PositionIn[0];
		EmitVertex();
		gl_FrontColor = gl_FrontColorIn[1];
		gl_TexCoord[0] = gl_TexCoordIn[1][0];
		gl_Position = gl_PositionIn[1];
		EmitVertex();
		gl_FrontColor = gl_FrontColorIn[2];
		gl_TexCoord[0] = gl_TexCoordIn[2][0];
		gl_Position = gl_PositionIn[2];
		EmitVertex();
		EndPrimitive();
	}
  """)
}
*/


// -- Program ------------------------------------------------------------------

/** A <code>ShaderProgramme</code> combines several Shaders into one executeable Program on the GPU */
class ShaderProgramme extends GLObject {
  def create = 
    id = gl.glCreateProgram
  
  /** attaches the given shader to this programme */
  def +=(shader:Shader) = 
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
