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

  /** signals that an option has neither found Some(x) or None but another unrequested x */
  case class Result()
  case class Nothing[A](val x:A, val name:String) extends Result
  case class Requested[A](val x:A) extends Result
  case class Another[A](val x:A, val name:String) extends Result
  
  /** a mutable list of all childnodes */
  val children = new ArrayBuffer[Node]
  
  /** maps keys to nodes for faster node lookup */
  // val map = new HashMap[Int,Tweak]
  
  
  // -----------------------------------------------------------------------
  // GETTER
  // -----------------------------------------------------------------------
  /** @return a Tweak for the given path or null if the specified node didnt exist */
  def apply(path:String):Node = {
//    val a = Address(this, path)
//    val elements = a.elements
//    
//    def recurse(branch:Branch, depth:Int):Node = {
//      val next = elements(depth)
//      next match {
//        case "" => recurse(branch, depth + 1)
//        case _ => 
//          branch.children find(node => node.name == next) match {
//            case None => null
//            case Some(node) =>
//              if(depth == elements.length-1) 
//                node
//              else if(node.isInstanceOf[Branch])
//                recurse(node.asInstanceOf[Branch], depth+1)
//              else
//            	null
//          }
//      }
//    }
//
//    val node = if(a.isAbsolute) recurse(root, 0) else recurse(this, 0)
//    node match {
//      case b:Branch => new BranchTweak(b)
//      case l:Leaf[_] => new LeafTweak(l)
//      case _ => null
//    }
  	null
  }

  
  def apply[T](path:String, default:T)(implicit m:Manifest[T]):Leaf[T] = 
    apply(Address(this, path), default)
  
  def apply[T](address:Address, default:T)(implicit m:Manifest[T]):Leaf[T] = {
//    println(" ")
//    info("attempting to set", address)
//    printTree
    
    find(address) match {
      case Requested(l:Leaf[_]) => l.asInstanceOf[Leaf[T]]      
      case Requested(b:Branch) => throw new Exception("Unexpected branch: "+ b)
      case Another(l:Leaf[_], name:String) => throw new Exception("Unexpected leaf:" + l)
      case Another(b:Branch, name:String) => 
        b += name
        apply(address, default)

      // the node is not set, yet
      case Nothing(b:Branch, name:String) => b += (name, default)
    }
  }
  
  protected def find(address:Address):Result = {
    val elements = address.elements
    
    def recurse(branch:Branch, depth:Int):Result = {
      val name = elements(depth)
      val last = depth+1 == elements.length
      
      branch find(_.name == name) match {
        case None => if(last) Nothing(branch, name) else Another(branch, name)
        case Some(b:Branch) => if(last) Requested(b) else recurse(b, depth+1)
        case Some(l:Leaf[_]) => if(last) Requested(l) else Another(l, name)
      }
    }
    
    if(address.isAbsolute) 
      recurse(root, 0)
    else 
      recurse(this, 0) 
  } 
  
  /*
  // -----------------------------------------------------------------------
  // SETTER
  // -----------------------------------------------------------------------
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
  */
  
  // -----------------------------------------------------------------------
  // OPERATORS
  // -----------------------------------------------------------------------
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
  
  // -----------------------------------------------------------------------
  // HELPERS
  // -----------------------------------------------------------------------
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
