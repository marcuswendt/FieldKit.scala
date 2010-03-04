/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit

/**
 * Global utility methods for the physics library
 */
package object physics {
	
	/**
	 * Anonymous / shorthand force constructor
	 */
	def behaviour(body:Particle => Unit):Behaviour = {
		new Behaviour {
			def apply(p:Particle) = body(p)
		}
	}
	
	/**
	 * Anonymous / shorthand constraint constructor
	 */
	def constraint(body:Particle => Unit):Constraint = {
		new Constraint {
			def apply(p:Particle) = body(p)
		}
	}
}
