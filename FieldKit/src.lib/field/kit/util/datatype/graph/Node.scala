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
class Node extends Logger {
  
  var parent:Node = _
  
  var name:String = logName
  
  def this(name:String) = {
    this()
    this.name = name
  }
}