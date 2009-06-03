/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 01, 2009 */
package field.kit.gl.scene.state

import field.kit.gl.scene.RenderState
import field.kit.gl.render.objects._

/** 
 * Provides utility constructors for the <code>ShaderState</code> class
 * @author Marcus Wendt
 */
object ShaderState extends field.kit.Logger {
  import java.net.URL
  import field.kit.util.Loader
  
  def apply(vsURL:URL, fsURL:URL) = {
    val vs = vertexShader(Loader.readFile(vsURL))
    val fs = fragmentShader(Loader.readFile(fsURL))
    new ShaderState(vs, fs)
  }
  
  /** 
   * Creates a new <code>VertexShader</code> from the given GLSL sourcecode.
   * Falls back to the default fixed pipeline VertexShader on error. 
   */
  def vertexShader(source:String) = {
    try {
      new VertexShader(source)
    } catch {
      case e:ShaderCompileException =>
        warn(e)
        VertexShader.DEFAULT
    }
  }
  
  /** 
   * Creates a new <code>FragmentShader</code> from the given GLSL sourcecode.
   * Falls back to the default fixed pipeline FragmentShader on error. 
   */
  def fragmentShader(source:String) = {
    try {
      new FragmentShader(source)
    } catch {
      case e:ShaderCompileException =>
        warn(e)
        FragmentShader.DEFAULT
    }
  }
}


/**
 * Allows rendering a <code>Geometry</code> using a <code>ShaderProgramme</code>
 * constructed from a <code>VertexShader</code> and a <code>FragmentShader</code>
 * @author Marcus Wendt
 */
class ShaderState extends RenderState {
  import field.kit.gl.scene.Geometry
  
  var prog:ShaderProgramme = null
  
  def this(s:Shader) = {
    this()
    if(s.isInstanceOf[VertexShader])
      init(s.asInstanceOf[VertexShader], FragmentShader.DEFAULT)
    else
      init(VertexShader.DEFAULT, s.asInstanceOf[FragmentShader])
  }
  
  def this(vs:VertexShader, fs:FragmentShader) = {
    this()
    init(vs, fs)
  }
  
  /** initializes the <code>ShaderProgramme</code> with the given <code>Shaders</code> */
  def init(vs:VertexShader, fs:FragmentShader) {
    prog = new ShaderProgramme
    prog.create
    prog += vs
    prog += fs
    prog.init
  }
        
  // methods
  def enable(geo:Geometry) = prog.bind
  def disable(geo:Geometry) = prog.unbind
  def destroy = prog.destroy
}
