/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.structure.graph

import field.kit.Logger

/** 
 * Atomic building block of an acyclic connected graph structure 
 * @see http://en.wikipedia.org/wiki/Tree_data_structure
 */
class Node(var name:String) extends Logger {
  
  // TODO consider adding attach/ detach methods and a parent branch
  // pro: really useful in some situations
  // con: adds complexity, less flexibility 
  // -> do when there's a really good reason for it 
  // at the moment the scenegraph works nicely without (maybe the agent behaviour system doesnt)
  
  // TODO consider implementing basic serialisation/deserialisation techniques at the node level
  // would make all graphs instantly persistent
  
//	   var parent:Branch = null
//   
//   /** called when this node is being added to a parent branch */
//   def attach(parent:Branch) = {
//     if(parent == null) {
//       throw new Exception("Parent can't be null!")
//     } else { 
//       fine("attaching '"+ name +"' to "+ parent)
//       this.parent = parent
//     }
//   }
//   
//   /** called when this node is being detached from its parent */
//   def detach = fine("'"+ name +"' detaching from "+ parent)
}