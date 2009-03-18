/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent.graph

import field.kit.Logger

/**
 * <p>
 * These are the core building blocks for the agents memory system which is 
 * an acyclic connected graph structure.
 * </p>
 * 
 * <p>
 * It was partly based on my previous experiments based on a unified data-tree that
 * could be made visible to network clients and allow for interaction over osc
 * </p>
 * 
 * <p>It is also inspired by Marc Downies concept of a ContextTree described in his Master Thesis:<br />
 * p. 206, Chapter 6 "The Context Tree — a new “working memory” for agents"
 * </p>
 * 
 * @see http://en.wikipedia.org/wiki/Tree_data_structure
 * @see http://www.openendedgroup.com/index.php/publications/thesis-downie/
 * 
 * TODO caching strategy for addresses!
 */
abstract class Node(val parent:Node, val name:String) extends Logger {
  /** a reference to the graphs root-node */
  val root:Root = if(parent==null) this.asInstanceOf[Root] else parent.root
  
  /** @return an absolute path to this node as address string */
  def address = {
    def recurse(n:Node, s:String):String = 
      if(n.parent!=null) recurse(n.parent, "/" + n.name + s) else s
    recurse(this, "")
  }
  
  /** resolves the given path into an absolute node-address */
  def address(path:String):String = {
    // address is absolute
    if(Node.isAbsolute(path)) {
      path
      
    // address is relative, above this node
    } else if(Node.isRelative(path)) {
      val fragments = path.split("../")
      
      if(fragments.length == 0) {
        // address was just ../ return the parent
        this.parent.address
        
      } else {
        // construct relative path from parent address
        var tmp = this.parent.address
        fragments foreach(f => {
        	if(f == "") {
        	  tmp = tmp.slice(0, tmp.lastIndexOf(Node.seperator))
        	} else {
        	  tmp += Node.seperator + f
        	}
        })
        tmp
      }
      
    // address is below this node
    } else {
      this.address +"/"+ path
    }
  }
  
  override def hashCode:Int = address.hashCode
}

/** Companion object to Node */
object Node {
  var seperator = '/'
  var parent = "../"
  
  def isAbsolute(path:String) = path(0) == seperator
  def isRelative(path:String) = !isAbsolute(path)
}