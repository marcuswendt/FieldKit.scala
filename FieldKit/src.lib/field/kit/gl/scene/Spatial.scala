/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import kit.gl.render.Drawable
import kit.util.datatype.graph._

/** base class for all scene-graph elements */
abstract class Spatial(name:String) extends BaseNode(name) with Drawable {
  import field.kit.math._
  
  logName = name
  
  var translation = new Vec3
  var scale = new Vec3(1,1,1)
  var rotation = new Vec3
  
  override def render {
    if(isVisible) {
      gl.glPushMatrix
      gl.glTranslatef(translation.x, translation.y, translation.z)
    
      gl.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f)
      gl.glRotatef(rotation.y, 0.0f, 1.0f, 0.0f)
      gl.glRotatef(rotation.z, 0.0f, 0.0f, 1.0f)
    
      gl.glScalef(scale.x, scale.y, scale.z)
      
      // draw the actual object
      draw
      
      gl.glPopMatrix  
    }
  }
}
