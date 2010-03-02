/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math

/**
* Common base class for all Vector types <code>Vec2</code> & <code>Vec3</code>
*/
trait Vec {
	def x:Float
	def x_=(value:Float)

	def y:Float
	def y_=(value:Float)

	def z:Float
	def z_=(value:Float)
}
