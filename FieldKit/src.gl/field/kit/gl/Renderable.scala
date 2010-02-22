/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl

import javax.media.opengl.GLContext
import javax.media.opengl.glu.GLU

/**
 * Provides quick access to the current OpenGL context
 * @author marcus
 */
trait GLUser {
	def gl = GLContext.getCurrent.getGL
	
	private var _glu:GLU = _
	
	def glu = {
		if(_glu == null) _glu = new GLU
		_glu
	}
}

/** 
 * Base trait for all renderable elements
 */
trait Renderable extends GLUser {
	def render
}
