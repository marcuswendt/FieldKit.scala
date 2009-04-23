/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.render

/** 
 * base class for all render states
 * @author Marcus Wendt
 */
abstract class RenderState extends Renderable {
  import field.kit.gl.scene.Geometry
  
  def enable(geo:Geometry)
  def disable(geo:Geometry)
  
  final override def render {}
}
