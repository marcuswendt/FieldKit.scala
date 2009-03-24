/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Renderable

/** base class for all scene-graph elements */
abstract class Spatial(var name:String) extends Renderable {
  import field.kit._
  
  var translation = new Vec3
  var scale = new Vec3(1,1,1)
  var rotation = new Vec3
  
  def renderPre {
    gl.glPushMatrix
    gl.glTranslatef(translation.x, translation.y, translation.z)
    
    gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f)
    gl.glRotatef(rotation.y, 0.0f, 1.0f, 0.0f)
    gl.glRotatef(rotation.z, 0.0f, 0.0f, 1.0f)
    
    gl.glScalef(scale.x, scale.y, scale.z)
  }
  
  def renderPost {
    gl.glPopMatrix
  }
}
