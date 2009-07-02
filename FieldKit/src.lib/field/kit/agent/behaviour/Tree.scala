/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 15, 2009 */
package field.kit.agent.behaviour

// -- Leaf-Nodes ---------------------------------------------------------------
/**
 * Lowlevel base class for all <code>Behaviour-Tree</code> components
 * @author Marcus Wendt
 */
abstract class Node extends field.kit.util.datatype.graph.Node {
  def execute:Boolean
}

/**
 * <code>Actions</code> "do stuff" in the simulation world.
 */
abstract class Action extends Node {
}

/**
 * <code>Conditions</code> are quick checks for information without side-effects
 */
abstract class Condition extends Node {
}


// -- Branches -----------------------------------------------------------------

/**
 * <code>Composites</code> are branches in the Behaviour-Tree that combine multiple behaviours together.
 * @author Marcus Wendt
 */
abstract class Composite extends Node with field.kit.util.datatype.graph.Branch[Node] {
}


/**
 * A <code>Sequence</code> groups a set of behaviours.
 * Running one after the other in the given order.
 * 
 * @see http://aigamedev.com/open/article/sequence/
 * @author Marcus Wendt
 */
class Sequence extends Composite {
  /** execute all children after each other, until one fails */
  def execute:Boolean = {
    var result = true
    var i = 0
    while(i < size) {
      result = children(i).execute
      // if child fails return immediately
      // in the aigamedev example they try alternative routes here
      if(!result) return false
      i += 1
    }
    result
  }
}

/**
 * A <code>Selector</code> is effectively an <code>OR</code> node in the <code>Behaviour-Tree</code>
 * @see http://aigamedev.com/open/article/selector/
 * @author Marcus Wendt
 */
abstract class Selector extends Composite {
  import scala.collection.mutable.ArrayBuffer
  
  var ordered = new ArrayBuffer[Node]
  
  def update
  
  def execute:Boolean = {
    update
    
    var result = true
    var i = 0
    while(i < size) {
      result = children(i).execute
      // if child succeeds return immediately
      if(result) return true
      i += 1
    }
    result
  }
}

/**
 * Priority selectors are simply an ordered list of behaviors that are tried one after the other until one matches.
 */
class PrioritySelector extends Selector {
  def update = ordered = children
}

/**
 * Probability selectors pick one of their child nodes randomly based on the weights provided by the designer.
 */
class ProbabilitySelector extends Selector {
  def update = {
    // TODO calculate probability weight for each child and order them 
  }
}

class RandomSelector extends Selector {
  def update = {
    // TODO add children randomly into ordered list
  }
}
  
/*
 * The parallel composite is meant to group several nodes together that can run
 * as a block in parallel to other blocks.
 * 
class Parallel(name:String) extends Composite(name) {
  def this() = this("Parallel")
}
*/