/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created February 02, 2009 */
package field.kit.gl.objects

import field.kit.gl._
import scala.collection.mutable.HashMap

/** 
 * A <code>ShaderProgramme</code> combines several Shaders into one executeable Program on the GPU 
 */
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


	// -- Attributes -----------------------------------------------------------
	private var attributes = new HashMap[String, Int]

	/**
	 * @return The attribute location for the attribute with the given name
	 */
	def attribute(name:String) = {
		attributes.get(name) match {
			case None =>
				val location = gl.glGetAttribLocation(id, name)
				attributes(name) = location
				location
				
			case Some(location:Int) => location
		}
	}
	
	/**
	 * Sets the attribute location for the given name
	 */
	def attribute(name:String, location:Int) {
		attributes(name) = location
		gl.glBindAttribLocation(id, location, name)
	}
	
	// -- Uniforms -------------------------------------------------------------
	private var uniforms = new HashMap[String, ShaderUniform]

	/**
	 * @return The shader uniform for the uniform variable with the given name
	 */
	def uniform(name:String) = {
		uniforms.get(name) match {
			case None =>
				val u = new ShaderUniform(this, name)
				uniforms(name) = u
				u
			case Some(u:ShaderUniform) => u
		}  
	}
	
	protected def apply(name:String) = uniform(name)

	// -- Updaters -------------------------------------------------------------
	def update(name:String, value:Int) = uniform(name).set(value)
	def update(name:String, value:Float) = uniform(name).set(value)
	def update(name:String, x:Float, y:Float) = uniform(name).set(x,y)
	def update(name:String, x:Float, y:Float, z:Float) = uniform(name).set(x,y,z)
	def update(name:String, x:Float, y:Float, z:Float, u:Float) = uniform(name).set(x,y,z,u)

}
