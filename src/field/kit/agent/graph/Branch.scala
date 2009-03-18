/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 18, 2009 */
package field.kit.agent.graph

/**
 * a special node that contains several children
 */
class Branch(parent:Node, name:String) 
extends Node(parent, name) 
with Collection[Node] {
  import scala.reflect.Manifest
  import scala.collection.mutable.ArrayBuffer
  import scala.collection.mutable.HashMap
  
  val children = new ArrayBuffer[Node]
 
  /** @return the child identified by its index */
  def apply(index:Int):Node = children(index) 
  
  /** follows the given path and returns a Node or null */
  def apply(path:String):Node = {
    val elements = path.split(Node.seperator)
    
    def recurse(branch:Branch, depth:Int):Node = {
      val next = elements(depth)
      next match {
        case "" => recurse(branch, depth + 1)
        case _ => 
          branch.children find(node => node.name == next) match {
            case None => null
            case Some(node) =>
              if(depth == elements.length-1) 
                node
              else if(node.isInstanceOf[Branch])
                recurse(node.asInstanceOf[Branch], depth+1)
              else
            	null
          }
      }
    }

    if(Node.isAbsolute(path)) {
      recurse(root, 0)
    } else {
      recurse(this, 0)
    }
  }
  
  /** sets the node's value at the given index */
  def update[T](index:Int, value:T):Leaf[T] = {
    val leaf = children(index).asInstanceOf[Leaf[T]]
    leaf.value = value
    leaf
  }
  
  /** follows the path to its terminal node and attempts to set its value */
  def update[T](path:String, value:T):Leaf[T] = {
    val node = this(path)
    if(node == null) {
      null
    } else {
      if(node.isInstanceOf[Leaf[_]]) {
        val leaf = node.asInstanceOf[Leaf[T]]
        leaf.value = value
        leaf
      } else {
        null
      }
    }
  }
  
  /** creates, adds and returns a named branch */
  // TODO check if name is a path
  def +=(name:String) = {
    val b = new Branch(this, name)
    children += b
    b
  }
  
  /** adds and returns the given branch as a child */
  // TODO check if name is a path
  def +=(b:Branch) = {
    children += b
    b
  }
  
  /** adds and returns a named leaf of type T */
  // TODO check if name is a path
  def +=[T](name:String, value:T)
  	(implicit m:Manifest[T]):Leaf[T] = {
	val leaf = new Leaf[T](this, name, value)
	children += leaf
    leaf
  }

  /** removes the child with the given name
   * returns the child on success or null */
  // TODO check if name is a path
  def -=(name:String) = {
    val i = indexOf(name)
    if(i != -1) {
      val child = children(i)
      children.remove(i)
      child
    } else {
      null
    }
  }
  
  /** returns the index of a child with the given name or -1 */
  def indexOf(name:String) = children findIndexOf(_.name.equals(name))
  
  def clear = children.clear
    
  def size = children.length
  
  /** required by the Collection trait to make this iterable */
  def elements = children.elements
  
  override def toString = "Branch("+name+")"
  
  /** recursively goes through the whole subtree structure from this node and prints it*/
  def printTree = {
    def recurse(n:Node, d:Int):Unit = {
      for(i <- 0 until d) print("  ")
      println(n)
      if(n.isInstanceOf[Branch]) 
        n.asInstanceOf[Branch].children.foreach(recurse(_, d+1))
    }    
    recurse(this, 0)
  }
}

/** helper class to hold hashmap keys */
class Key(val address:String) {
  val hash = address.hashCode
  override def hashCode = hash
  override def toString = "Key("+ address +") => "+ hash
}