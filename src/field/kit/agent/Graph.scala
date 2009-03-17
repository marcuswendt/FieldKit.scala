/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent

import scala.reflect.Manifest
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
  val root:Root[Node] = if(parent==null) this.asInstanceOf[Root[Node]] else parent.root
  
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

/** Companion object to Node*/
object Node {
  var seperator = '/'
  var parent = "../"
  
  def isAbsolute(path:String) = path(0) == seperator
  def isRelative(path:String) = !isAbsolute(path)
}

/**
 * a special node that stores a value
 */
class Leaf[T]
  (parent:Node, name:String, var value:T)
  (implicit val clazz: Manifest[T]) 
  extends Node(parent, name) {
    
  override def toString = "Leaf("+name+") => "+ value +" type: "+ clazz  
}

/**
 * a special node that contains several children
 */
class Branch(parent:Node, name:String) 
extends Node(parent, name) 
with Collection[Node] {
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
  
  /** sets the node's value at the given index to value */
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

/**
 * the root of the whole graph
 */
class Root[T <: Node] extends Branch(null, "Root") {
  override def toString = name
}

/** helper class to hold hashmap keys */
class Key(val address:String) {
  val hash = address.hashCode
  override def hashCode = hash
  override def toString = "Key("+ address +") => "+ hash
}