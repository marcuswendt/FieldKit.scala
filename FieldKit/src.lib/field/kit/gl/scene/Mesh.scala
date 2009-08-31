/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import transform._

/** 
 * Base class for all sorts of polygon mesh geometry
 * 
 * To encourage encapsulation and for better readability additional functionality 
 * is added via Traits
 */
abstract class Mesh(name:String) extends Spatial(name) with RenderStateable with Triangulator {
  import javax.media.opengl.GL
  import java.nio.IntBuffer
  import java.nio.FloatBuffer
  import math.Common._

  /** Stores the actual data buffers */
  var data = new MeshData
  
  /** This objects default colour */
  var colour = new Colour(Colour.WHITE)
  
  /**
   * Draws this Mesh
   */
  def draw {
    enableStates
    if(data.useVBO) setupInterleavedDataVBO else setupArrays
    drawElements
    disableStates
  }
  
  /**
   * Setups the VBO for drawing
   */
  protected def setupInterleavedDataVBO:Unit = {
    def updateVBO(buffer:FloatBuffer, offset:Int) {
      if(data.needsRefresh) {
        buffer.rewind
        gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 
                           offset, 
                           buffer.limit * 4, 
                           buffer)
      }
    }
    
    // make sure we have a valid vbo
    if(data.vbo == null)
      initInterleavedDataVBO
      
    data.vbo.bind
    
    var offset = 0
    
    // -- setup normal coords --------------------------------------------------
    val normals = data.normals
    if(normals == null) {
      gl.glDisableClientState(GL.GL_NORMAL_ARRAY) 
    } else {
      updateVBO(normals, offset)
      gl.glNormalPointer(GL.GL_FLOAT, 0, offset)
      gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
      offset += normals.limit * 4
    }

    // -- setup colours --------------------------------------------------------
    val colours = data.colours
    if(colours == null) {
      gl.glDisableClientState(GL.GL_COLOR_ARRAY)
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
      
    } else {
      updateVBO(colours, offset)
      gl.glEnableClientState(GL.GL_COLOR_ARRAY)
      gl.glColorPointer(4, GL.GL_FLOAT, 0, offset)
      offset += colours.limit * 4
    }

    // -- setup texture coord arrays -------------------------------------------
    var j = 0
    // the completely proper way to do this would be to keep track of the activated 
    // texture units and deactivate them when not required 
    while(j < data.textureCoords.size) {
      gl.glClientActiveTexture(GL.GL_TEXTURE0 + j)
      val textureCoords = data.textureCoords(j)
      if(textureCoords == null) {
        gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
      } else {
        updateVBO(textureCoords, offset)
        gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
        gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, offset)
        offset += textureCoords.limit * 4
      }
      j += 1
    }
      
    // -- setup vertex array ---------------------------------------------------
    val vertices = data.vertices
    if(vertices == null) {
      gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    } else {
      updateVBO(vertices, offset)
      gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
      gl.glVertexPointer(3, GL.GL_FLOAT, 0, offset)
      offset += vertices.limit * 4
    }
    
    data.needsRefresh = false
  }
  
  /**
   * Creates a VBO and accompanying buffer
   */
  protected def initInterleavedDataVBO {
    import kit.gl.render.GLObject
    import kit.gl.render.objects.VertexBuffer
    
    // make sure we have a valid vbo
    if(data.vbo == null) 
      data.vbo = new VertexBuffer
    
    if(data.vbo.id == GLObject.UNDEFINED) 
      data.vbo.create
    
    val vbo = data.vbo
    vbo.bind
    
    // calculate interleaved buffer size
    var bufferSize = 0
    if(data.normals != null) bufferSize += data.normals.limit
    if(data.colours != null) bufferSize += data.colours.limit
    if(data.textureCoords != null) {
      for(buffer <- data.textureCoords)
        bufferSize += buffer.limit
    }
    if(data.vertices != null) bufferSize += data.vertices.limit
    
    // initialize interleaved buffer
    val interleaved = data.allocInterleaved(bufferSize)
    vbo.data(bufferSize, interleaved, data.vboUsage)
    vbo.unbind
    
    interleaved.rewind
  } 
  
  /**
   * Setups the vertex arrays for drawing
   */
  protected def setupArrays {
    // make sure the vbo is disabled
    if(data.vbo != null) {
      data.vbo.unbind
    } else {
      gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
    }
    
    // -- setup normal array ---------------------------------------------------
    val normals = data.normals
    if(normals == null) {
      gl.glDisableClientState(GL.GL_NORMAL_ARRAY) 
    } else {
      gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
      normals.rewind
      gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
    }

    // -- setup colour array ---------------------------------------------------
    val colours = data.colours
    if(colours == null) {
      gl.glDisableClientState(GL.GL_COLOR_ARRAY)
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
      
    } else {
      gl.glEnableClientState(GL.GL_COLOR_ARRAY)
      colours.rewind
      gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    }
    
    // -- setup texture coord arrays -------------------------------------------
    var j = 0
    // the completely proper way to do this would be to keep track of the activated 
    // texture units and deactivate them when not required 
    while(j < data.textureCoords.size) {
      gl.glClientActiveTexture(GL.GL_TEXTURE0 + j)
      val textureCoords = data.textureCoords(j)
      if(textureCoords == null) {
        gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
      } else {
        textureCoords.rewind
        gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
        gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, textureCoords)
      }
      j += 1
    }
           
    // -- setup vertex array ---------------------------------------------------
    val vertices = data.vertices
    if(vertices == null) {
      gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    } else {
      gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
      vertices.rewind
      gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    }
  }
  
  /**
   * Does the actual drawing after the vbo or the arrays have been set up
   */
  protected def drawElements {
    val indices = data.indices
    if(indices == null) {
      // simply draw everything that is in the vertex array
      if(data.indexLengths == null) {
        val glIndexMode = data.indexModes(0).id
        gl.glDrawArrays(glIndexMode, 0, data.vertexCount)
        
      // draws multiple elements using the same index buffer
      } else {
        var offset = 0
        var i = 0
        var indexModeCounter = 0
        while(i < data.indexLengths.length) {
          val count = data.indexLengths(i)
          val glIndexMode = data.indexModes(indexModeCounter).id
          gl.glDrawArrays(glIndexMode, offset, count)
          
          offset += count
          if(indexModeCounter < data.indexModes.length - 1)
            indexModeCounter += 1
          
          i += 1
        }
      }
      
    // index based drawing
    } else {
      // draws a single element only
      if(data.indexLengths == null) {
        val glIndexMode = data.indexModes(0).id
        indices.position(0)
        gl.glDrawElements(glIndexMode, indices.limit, GL.GL_UNSIGNED_INT, indices)
      
      // draws multiple elements using the same index buffer
      } else {
        var offset = 0
        var i = 0
        var indexModeCounter = 0
        while(i < data.indexLengths.length) {
          val count = data.indexLengths(i)
          val glIndexMode = data.indexModes(indexModeCounter).id
          
          indices.position(offset)
          indices.limit(offset + count)
          gl.glDrawElements(glIndexMode, count, GL.GL_UNSIGNED_INT, indices)
          
          offset += count
          if(indexModeCounter < data.indexModes.length - 1)
            indexModeCounter += 1
          
          i += 1
        }
      }
    }
  }
  
  /**
   * Draws this Mesh as a point cloud using vertex arrays
   */
  protected def drawAsPoints(pointSize:Float) {
    // -- setup vertex array ---------------------------------------------------
    val vertices = data.vertices
    if(vertices == null) {
      gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    } else {
      gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
      vertices.rewind
      gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    }
    
    // -- setup colours --------------------------------------------------------
    val colours = data.colours
    if(colours == null) {
      gl.glDisableClientState(GL.GL_COLOR_ARRAY)
      gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
      
    } else {
      gl.glEnableClientState(GL.GL_COLOR_ARRAY)
      gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    }
    
    // -- draw points ----------------------------------------------------------
    gl.glPointSize(pointSize)
    gl.glDrawArrays(GL.GL_POINTS, 0, data.vertexCount)
  }
  
  // -- Colours ----------------------------------------------------------------
  def solidColour(c:Colour) {
    colour := c
    val colours = data.allocColours
    if(colours!=null) {
      colours.clear
      for(i <- 0 until colours.capacity/4) {
        colours.put(c.r)
        colours.put(c.g)
        colours.put(c.b)
        colours.put(c.a)
      }
      colours.rewind
    }
  }
  
  def randomizeColours {
    val colours = data.allocColours
    colours.clear
    for(i <- 0 until colours.capacity/4) {
      colours.put(random)
      colours.put(random)
      colours.put(random)
      colours.put(1f)
    }
    colours.rewind
  }
  
  // -- Traits -----------------------------------------------------------------
  def triangulate:Unit = triangulate(data.vertexCount, data.vertices, data.indices)
}
