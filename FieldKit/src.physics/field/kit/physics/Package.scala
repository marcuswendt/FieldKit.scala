/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit

package object physics {
	
	/**
	 * Shorthand behaviour constructor
	 */
	def behaviour(body:Particle => Unit):Behaviour = {
		new Behaviour {
			
			def apply(p:Particle) = body(p)
			
			// support for creating identical copies
			type T = Behaviour
			def copy = behaviour(body)
		}
	}
}
