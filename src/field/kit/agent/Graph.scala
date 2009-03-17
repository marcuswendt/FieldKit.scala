/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent

import scala.reflect.Manifest

/** 
 * these are the core building blocks for the agents memory system
 * which is actually a tree
 */
abstract class Node(val parent:Node, val name:String) {
  /** a reference to the graphs root-node */
  val root:Root = if(parent==null) parent.asInstanceOf[Root] else parent.root
  
  // TODO caching
  def address = {
    def recurse(n:Node, s:String):String = 
      if(n.parent!=null) recurse(n.parent, "/" + n.name + s) else s
    recurse(this, "")
  }
  
  // TODO caching
  override def hashCode:Int = address.hashCode
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
class Branch[T >: Node](parent:Node, name:String) 
extends Node(parent, name) {
  import scala.collection.mutable.ArrayBuffer
  var children = new ArrayBuffer[T]
  override def toString = "Branch("+name+")"
}

/**
 * the root of the whole graph
 */
class Root extends Branch(null, "Root") {
  override def toString = name
}