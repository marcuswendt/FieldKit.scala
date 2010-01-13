/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created Jan 11, 2010 */
package field.kit.test

import field.kit.Sketch

/**
 * Simplest possible Sketch
 */
object SketchTest extends Sketch {
	import processing.core.PConstants._
	
	override def setup {
		size(1280, 720, OPENGL)
		rectMode(CENTER)
	}
	
	override def draw {
		background(0)
		
		fill(255)
		rect(mouseX, mouseY, 25, 25)
	}
}
