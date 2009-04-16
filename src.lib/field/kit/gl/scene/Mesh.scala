/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Renderable

/** base class for all triangle meshes */
class Mesh(name:String) extends Geometry(name) {
  import javax.media.opengl.GL
  
  var glGeometryMode = GL.GL_TRIANGLES
  
  def draw {
    // enable gl vertex & texture coord arrays
	gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    gl.glEnableClientState(GL.GL_COLOR_ARRAY)
    
    enableStates

    gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoords)
    gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
    gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
    
    if(useIndices) {
      gl.glDrawElements(glGeometryMode, indicesCount, GL.GL_UNSIGNED_INT, indices)
    } else {
      gl.glDrawArrays(glGeometryMode, 0, size)
    }
    
    gl.glDisableClientState(GL.GL_COLOR_ARRAY)
    gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
    gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
    
    disableStates
  }
}