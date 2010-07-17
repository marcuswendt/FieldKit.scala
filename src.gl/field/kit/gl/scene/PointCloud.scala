/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 04, 2010 */
package field.kit.gl.scene

import field.kit._
import field.kit.gl.objects.VertexBuffer
import field.kit.gl.scene.transform.RenderStateable
import field.kit.gl.scene.state.ShaderState
import javax.media.opengl.GL

/**
 * 
 * @author Marcus Wendt
 */
object PointCloud {
	val DEFAULT_VS = """
	//
	// Pointcloud Vertex Shader
	//
	attribute vec3 InVertex;
	attribute vec4 InColour;
	attribute float InSize;
	
	void main() {
		vec4 vertex = vec4(InVertex, 1.0);
		vec4 position = gl_ProjectionMatrix * gl_ModelViewMatrix * vertex;
		gl_Position = position;
		gl_PointSize = InSize;
		gl_FrontColor = InColour;
	}
	"""
		
	val DEFAULT_FS = """
	//
	// Pointcloud Fragment Shader
	//
	uniform sampler2D tex0;
	
	void main() {
		//gl_FragColor = gl_Color * texture2D(tex0, gl_TexCoord[0].st);
		gl_FragColor = gl_Color;
	}
	""" 
}


/**
 * A modern (OpenGL 2.0, ready for 3.0) approach to draw a cloud of points.
 * Instead of setting the various GL array pointers we use one interleaved VBO 
 * and glAttribPointer to send the point data to a shader that decides how everything will be displayed.
 * 
 * This class assumes you have a ShaderState using these attributes assigned to it. 
 * See <code>PointCloud.DEFAULT_VS</code> and <code>PointCloud.DEFAULT_FS</code>
 * 
 * @author Marcus Wendt
 */
class PointCloud(name:String, capacity:Int, format:PointData.Format)
extends Spatial(name) with RenderStateable {

	var data = new PointData(capacity, format.elementSize)
	
	// -- Rendering --------------------------------------------------------
	def draw {
		enableStates
		setupVBO
		drawElements
		data.vbo.unbind			
		disableStates	
	}
	
	protected def setupVBO {
		if(data.vbo == null) {
			data.vbo = new VertexBuffer
			data.vbo.create
		}
		
		data.vbo.bind
		data.buffer.rewind
		data.vbo.data(data.buffer.limit, data.buffer, data.vboUsage)
	}
	
	protected def drawElements {
		val ss = state(classOf[ShaderState])
		if(ss == null) return
		
		// vertex
		val vertexLoc = ss.prog.attribute(format.vertexAttrib)
		gl.glEnableVertexAttribArray(vertexLoc)
		gl.glVertexAttribPointer(vertexLoc, 3, GL.GL_FLOAT, false, format.elementStride, format.vertexOffset)
		
		// colour
		var colourLoc = 0
		if(format.colour) {
			colourLoc = ss.prog.attribute(format.colourAttrib)
			gl.glEnableVertexAttribArray(colourLoc)
			gl.glVertexAttribPointer(colourLoc, 4, GL.GL_FLOAT, false, format.elementStride, format.colourOffset)
		}
		
		// size
		var sizeLoc = 0
		if(format.size) {
			sizeLoc = ss.prog.attribute(format.sizeAttrib)
			gl.glEnableVertexAttribArray(sizeLoc)
			gl.glVertexAttribPointer(sizeLoc, 1, GL.GL_FLOAT, false, format.elementStride, format.sizeOffset)				
			gl.glEnable(GL.GL_VERTEX_PROGRAM_POINT_SIZE)
		}
		
		// time
		var timeLoc = 0
		if(format.time) {
			timeLoc = ss.prog.attribute(format.timeAttrib)
			gl.glEnableVertexAttribArray(timeLoc)
			gl.glVertexAttribPointer(timeLoc, 1, GL.GL_FLOAT, false, format.elementStride, format.timeOffset)				
		}
		
		// -- draw (finally) ---------------------------------------------------
		gl.glEnable(GL.GL_POINT_SPRITE)
		gl.glTexEnvi(GL.GL_POINT_SPRITE, GL.GL_COORD_REPLACE, GL.GL_TRUE)
		gl.glDrawArrays(GL.GL_POINTS, 0, data.elementCount)
		gl.glDisable(GL.GL_POINT_SPRITE)
		
		// -- clean up ---------------------------------------------------------
		if(format.time)
			gl.glDisableVertexAttribArray(timeLoc)
			
		if(format.size) {
			gl.glDisableVertexAttribArray(sizeLoc)
			gl.glDisable(GL.GL_VERTEX_PROGRAM_POINT_SIZE)
		}
		
		if(format.colour)
			gl.glDisableVertexAttribArray(colourLoc)
			
		gl.glDisableVertexAttribArray(vertexLoc)
	}
	
	// -- Data Management --------------------------------------------------
	def clear {
		data.elementCount = 0
		data.buffer.rewind
	}
	
	// applies only to position only format
	def +=(pos:Vec3) {
		data.buffer.put(pos.x).put(pos.y).put(pos.z)
		data.elementCount += 1
	}
	
	def +=(pos:Vec3, size:Float) {
		data.buffer
			.put(pos.x).put(pos.y).put(pos.z)
			.put(size)
		data.elementCount += 1
	}
	
	def +=(pos:Vec3, colour:Colour) {
		data.buffer
			.put(pos.x).put(pos.y).put(pos.z)
			.put(colour.r).put(colour.g).put(colour.b).put(colour.a)
		data.elementCount += 1
	}
	
	def +=(pos:Vec3, colour:Colour, size:Float) {
		data.buffer
			.put(pos.x).put(pos.y).put(pos.z)
			.put(colour.r).put(colour.g).put(colour.b).put(colour.a)
			.put(size)
		data.elementCount += 1
	}
	
	def +=(pos:Vec3, colour:Colour, size:Float, time:Float) {
		data.buffer
			.put(pos.x).put(pos.y).put(pos.z)
			.put(colour.r).put(colour.g).put(colour.b).put(colour.a)
			.put(size)
			.put(time)
		data.elementCount += 1
	}
}