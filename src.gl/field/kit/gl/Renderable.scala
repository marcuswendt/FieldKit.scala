/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
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
