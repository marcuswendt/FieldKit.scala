/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 02, 2010 */
package field.kit.gl.objects

import field.kit.gl._

/**
 * Represents a GLSL shader uniform variable
 * @author Marcus Wendt
 */
class ShaderUniform(sp:ShaderProgramme, name:String) extends GLUser {
	
	def set(value:Int) = execute(gl.glUniform1i(location, value))
	
	def set(value:Float) = execute(gl.glUniform1f(location, value))
		
	def set(x:Float, y:Float) = execute(gl.glUniform2f(location, x,y))
		
	def set(x:Float, y:Float, z:Float) = execute(gl.glUniform3f(location, x,y,z))
	
	def set(x:Float, y:Float, z:Float, u:Float) = execute(gl.glUniform4f(location, x,y,z,u))
	
	private var location = GLObject.UNDEFINED
	
	private def execute(command: => Unit) {
		sp.bind
		
		// get shader location if not set before
		if(location == GLObject.UNDEFINED)
			location = gl.glGetUniformLocation(sp.id, name)
		
		command
		
		sp.unbind
	}
}
