/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 23, 2010 */
package field.kit.physics

import field.kit._

/**
 * Contains several particles or flocks
 */
class PhysicsSystem extends Updateable with Behavioural with Logger {
	import field.kit.math.geometry.AABB
	import scala.collection.mutable.ArrayBuffer
	
	var space = new AABB((100f, 100f, 100f))
	
	val elements = new ArrayBuffer[Updateable]
	
	def update(dt:Float) {
		
	}
	
	def updateBehaviours {
		
	}
}


trait Updateable {
	def update(dt:Float)
}

// TODO support emitters & world behaviours