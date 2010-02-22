/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.util.datatype.graph

import field.kit.Logger

/** 
 * Atomic building block of an acyclic connected graph structure 
 * @see http://en.wikipedia.org/wiki/Tree_data_structure
 * @author Marcus Wendt
 */
trait Node extends Logger {
  /**
   * This node's parent
   */
  var parent:Node = _
  
  /**
   * The name of this node
   */
  var name:String = logName
}

class BaseNode(_name:String) extends Node {
  this.name = _name
}