/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl

/**
 * Provides quick access to the current OpenGL context
 * @author marcus
 */
trait GLUser {
	import javax.media.opengl.GLContext
	def gl = GLContext.getCurrent.getGL
}

/** 
 * Base trait for all renderable elements
 */
trait Renderable extends GLUser {
	def render
}
