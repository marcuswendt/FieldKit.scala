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
  import math.FMath._
  
  /** Stores the actual data buffers */
  var data = new MeshData
  
  /** This objects default colour */
  var colour = new Colour(Colour.WHITE)
  
  /**
   * Draws this Mesh
   */
  def draw {
    enableStates
    // TODO implement VBO render path
    drawArrays
    disableStates
  }
  
  /**
   * Draws this Mesh using vertex arrays
   */
  protected def drawArrays {
    // -- setup normal array ---------------------------------------------------
    val normals = data.normals
    if(normals == null) {
      gl.glDisableClientState(GL.GL_NORMAL_ARRAY) 
    } else {
      gl.glEnableClientState(GL.GL_NORMAL_ARRAY)
      normals.rewind
      gl.glNormalPointer(GL.GL_FLOAT, 0, normals)
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
        gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
      }
      j += 1
    }
    
    // -- draw array / elements ------------------------------------------------
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
  
  // -- Colours ----------------------------------------------------------------
  def solidColour(c:Colour) {
    // TODO needs some cleaning up
    colour := c
    val colours = data.colours
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
    val colours = data.colours
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
