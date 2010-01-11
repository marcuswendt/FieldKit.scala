/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created Jan 11, 2010 */
package field

/**
 * Provides simplified access to the most frequently used classes and objects
 */
package object kit {
	// Utilities
	type Logger = field.kit.util.Logger
	val Logger = field.kit.util.Logger
	
	// Processing
	type Sketch = field.kit.p5.Sketch
	val Sketch = field.kit.p5.Sketch
	
	// Math
	type Vec2 = field.kit.math.Vec2
	val Vec2 = field.kit.math.Vec2
	
	type Vec3 = field.kit.math.Vec3
	val Vec3 = field.kit.math.Vec3
	
	type Vec4 = field.kit.math.Vec4
	val Vec4 = field.kit.math.Vec4
}
