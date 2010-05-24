/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.gl._
import field.kit.util.datatype.graph._

/** base class for all scene-graph elements */
abstract class Spatial(name:String) extends BaseNode(name) with Renderable {
  import field.kit.math._
  
  logName = name
  
  var translation = Vec3()
  var scale = Vec3(1f)
  var rotation = Vec3()
  
  var isVisible = true
  def toggleVisibility = isVisible = !isVisible
  
  def render {
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
  
  def draw
}
