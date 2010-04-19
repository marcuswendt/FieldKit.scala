/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 19, 2010 */
package field.kit.test.gl

import field.kit.p5.Sketch

import field.kit.gl.objects._
import field.kit.gl.scene._
import field.kit.gl.scene.state._
import field.kit.gl.scene.shape._

/**
 * Simplest possible Sketch
 */
object TextureTest extends Sketch {
	import processing.core.PConstants._
	
	var q:Mesh = _
	
	override def setup {
		size(1280, 720, OPENGL)
		
		// create rectangle
		q = new Quad("test", Quad.CENTER, width/2, height/2)
		q.translation.x = width/2
		q.translation.y = height/2
		
		q += TextureState(ImageTexture("res/test/test.jpg"))
//		q += TextureState(Texture("res/test/test.jpg"))
	}
	
	override def draw {
		background(0)
		
		beginGL
		q.render
		endGL
	}
}
