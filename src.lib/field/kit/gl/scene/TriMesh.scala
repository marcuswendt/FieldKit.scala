/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

/** base class for all triangle meshes */
class TriMesh(name:String) extends Geometry(name) {
  import javax.media.opengl.GL
  var glGeometryMode = GL.GL_TRIANGLES
  
  def render {
    if(isVisible) {
      renderPre
      
      // enable gl vertex & texture coord arrays
      gl.glEnableClientState(GL.GL_VERTEX_ARRAY)
      gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
      gl.glEnableClientState(GL.GL_COLOR_ARRAY)

      applyStates

      gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoords)
      gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices)
      gl.glColorPointer(4, GL.GL_FLOAT, 0, colours)
      gl.glDrawArrays(glGeometryMode, 0, size)

      gl.glDisableClientState(GL.GL_VERTEX_ARRAY)
      gl.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)

      disableStates

      renderPost
    }
  }
}
