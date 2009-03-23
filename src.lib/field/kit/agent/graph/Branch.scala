
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
 * 
 */
class Branch(name:String) extends Node(name) with Collection[Node] {
  import scala.reflect.Manifest
  import scala.collection.mutable.HashMap
  
  /** a mutable list of all childnodes */
  val children = new HashMap[String,Node]
  
  def apply[T](name:String, default:T)(implicit m:Manifest[T]):Leaf[T] =
    get(name) match {
      case Some(l:Leaf[_]) => l.asInstanceOf[Leaf[T]]
      case None => this += new Leaf(name, default)
    }
  
  def apply(name:String):Branch =
    get(name) match {
      case Some(b:Branch) => b
      case None => this += new Branch(name)
    }     
  
  def apply[T <: Branch](name:String, branch:T):T =
    get(name) match {
      case Some(b:Branch) => b.asInstanceOf[T]
      case None => this += branch
    }
  
  def update[T](name:String, value:T)(implicit m:Manifest[T]):Leaf[T] =
    get(name) match {
      case Some(l:Leaf[_]) => l.asInstanceOf[Leaf[T]]() = value
      case None => this += new Leaf(name, value)
    }
  
  def get(name:String) = children.get(name)

  /** checks wether a given node exists */
  def exists(name:String):Boolean =
    children.get(name) match {
      case Some(n:Node) => true
      case None => false
    }
  
  def +=[T <: Node](node:T):T = {
    children.put(node.name, node)
    node
  }
  
  def -=(node:Node) = {
    // TODO implement this
    throw new Error("Not implemented!")
    node
  }

  /** required by the Collection trait to make this iterable */
  def size = children.size
  def elements = children.values
  
  override def toString = "Branch("+name+")"
  
  /** recursively goes through the whole subtree structure from this node and prints it*/
  def printTree = {
    def recurseNodes(n:Node, d:Int):Unit = {
      for(i <- 0 until d) print("  ")
      
      n match {
        case l:Leaf[_] => println("-"+ l)
        case b:Branch => 
          println("+"+ b)
          b.children.values foreach(recurseNodes(_, d+1))
          
          if(b.isInstanceOf[Group]) 
            b.asInstanceOf[Group].behaviours foreach(recurseBehaviours(_, d + 1))
          
        case _ => println(n)
      }
    }
    
    def recurseBehaviours(b:Behaviour, d:Int):Unit = {
      for(i <- 0 until d) print("  ")
      println("*"+ b)
      b.behaviours foreach(recurseBehaviours(_, d + 1))
    }
    recurseNodes(this, 0)
  }
}
