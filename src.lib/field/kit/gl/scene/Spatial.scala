/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl.render.Drawable
import field.kit.util.datatype.graph.Node

/** base class for all scene-graph elements */
abstract class Spatial(name:String) extends Node(name) with Drawable {
  import field.kit.math._
  
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
