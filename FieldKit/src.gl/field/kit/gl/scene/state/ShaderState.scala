/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 01, 2009 */
package field.kit.gl.scene.state

import field.kit.gl.scene.RenderState
import field.kit.gl.objects._

/** 
 * Provides utility constructors for the <code>ShaderState</code> class
 * @author Marcus Wendt
 */
object ShaderState extends field.kit.Logger {
  import java.net.URL
  import field.kit.util.Loader

  private val URLTest = "([a-zA-Z]+://.+)".r
  
  def apply(vsSourceOrURL:String, fsSourceOrURL:String) = {
    val vs = vertexShader(getSource(vsSourceOrURL))
    val fs = fragmentShader(getSource(fsSourceOrURL))
    new ShaderState(vs, fs)
  }
    
  /** 
  	* Creates a <code>ShaderState</code> by compiling the contents from the given URL
  	* (Checks the suffix of the File specified by the URL to decide wether this is a Fragment- or Vertexshader)
  	*/
  def apply(url:URL) = {
    var vs = VertexShader.DEFAULT
    var fs = FragmentShader.DEFAULT
    
    if(url.toString.endsWith(VertexShader.SUFFIX))
      vs = vertexShader(Loader.read(url))
    else
      fs = fragmentShader(Loader.read(url))
      
    new ShaderState(vs, fs)
  }
  
  /** Creates a <code>ShaderState</code> by compiling the contents from the two given URLs */
  def apply(vsURL:URL, fsURL:URL) = {
    val vs = vertexShader(Loader.read(vsURL))
    val fs = fragmentShader(Loader.read(fsURL))
    new ShaderState(vs, fs)
  }
  
  /**
   * Tries to figure out wether the given String is an URL to load the shader code from
   * or already the source code itself.
   * @return the source code
   */
  protected def getSource(sourceOrURL:String) = {
    URLTest findFirstIn sourceOrURL match {
      case Some(url:String) => Loader.read(new URL(url))
      case None =>
        import java.io.File
        val file = new File(sourceOrURL)
        if(file.exists)
          Loader.read(file)
        else
          sourceOrURL
    }
  }

  /** 
   * Creates a new <code>VertexShader</code> from the given GLSL sourcecode.
   * Falls back to the default fixed pipeline VertexShader on error. 
   */
  protected def vertexShader(source:String) = {
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
  protected def fragmentShader(source:String) = {
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
	def enable = prog.bind
	def disable = prog.unbind
	def destroy = prog.destroy
}
