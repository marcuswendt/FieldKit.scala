/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 08, 2009 */
package field.kit.gl.scene.transform

/**
 * Gives the ability to apply RenderStates during the rendering of an object
 */
trait RenderStateable {
  //import field.kit.util.datatype.collection.ArrayBuffer
  import field.kit.gl.scene.RenderState  
  import scala.collection.mutable.ArrayBuffer
  
  var states = new ArrayBuffer[RenderState]

  /** Adds the given RenderState to this object **/
  def +=(state:RenderState) =
	  states += state

  /** Remvoes the given RenderState from this object **/
  def -=(state:RenderState) =
	  states -= state
	  
  protected def enableStates = {
    var i=0
    while(i < states.size) {
      val s = states(i)
      if(s.isEnabled) s.enable
      i += 1
    }
  }
  
  protected def disableStates = {
	var i=0
    while(i < states.size) {
      val s = states(i)
      if(s.isEnabled) s.disable
      i += 1
    }                              
  }
  
  /** @return the first <code>RenderState</code> that matches the given <code>Class</code> or null */
  def state[T <: RenderState](clazz:Class[T]):T = {
    states find (_.getClass == clazz) match {
      case Some(r:RenderState) => r.asInstanceOf[T]
      case _ => null.asInstanceOf[T]
    }
  }
}
