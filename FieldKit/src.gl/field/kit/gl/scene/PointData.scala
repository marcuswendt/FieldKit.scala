/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 04, 2010 */
package field.kit.gl.scene

import java.nio.FloatBuffer
import field.kit.util.Buffer
import field.kit.gl.objects.VertexBuffer

/**
 * Companion object to class <code>PointData</code>
 * @author Marcus Wendt
 */
object PointData {
	val FLOAT_SIZE = 4
	
	/** 
	 * Describes the interleaved PointData buffer contents
	 */
	trait Format {
		var elementSize = 0
		def elementStride = elementSize * FLOAT_SIZE
		
		var vertexAttrib = "InVertex"
		var vertexOffset = 0
		
		var colour = false
		var colourAttrib = "InColour"
		var colourOffset = 0
		
		var size = false
		var sizeAttrib = "InSize"
		var sizeOffset = 0
	}
	
	object Position extends Format {
		elementSize = 3
	}
	
	object PositionSize extends Format {
		elementSize = 4
		
		size = true
		sizeOffset = 3 * FLOAT_SIZE
	}
	
	object PositionColour extends Format {
		elementSize = 7

		colour = true
		colourOffset = 3 * FLOAT_SIZE
	}
	
	object PositionColourSize extends Format {
		elementSize = 8
		
		colour = true
		colourOffset = 3 * FLOAT_SIZE
		
		size = true
		sizeOffset = 7 * FLOAT_SIZE
	}
}

/**
 * Holds all drawing relevant data for a <code>PointCloud</code> object.
 * 
 * @author Marcus Wendt
 */
class PointData(var capacity:Int, val elementSize:Int) {
	var buffer = allocate(capacity)
	var elementCount = 0
	
	var vbo:VertexBuffer = _  
	var vboUsage = VertexBuffer.Usage.DYNAMIC_DRAW 
	
	def allocate(capacity:Int):FloatBuffer = {
		buffer = Buffer.float(buffer, capacity * elementSize)
		this.capacity = capacity
	    buffer
	}
	
	def clear {
		elementCount = 0
		buffer.rewind
	}
}