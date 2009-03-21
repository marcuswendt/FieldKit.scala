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
abstract class Node(var name:String) extends Logger {
  var parent:Branch = null 
  var root:Root = null

  /** constructor */
  def this(parent:Branch, name:String) = {
    this(name)
    this.parent(parent)
  }
  
  /** @return an absolute path to this node as address string */
  def address = Address(this)
  
  /** resolves the given path into an absolute node-address */
  def address(path:String) = Address(this, path)
  
  def parent(node:Branch) {
    this.parent = node
    this.root = if(node==null) this.asInstanceOf[Root] else node.root
  }
}