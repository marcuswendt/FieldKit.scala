/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent

import field.kit.Logger

// -----------------------------------------------------------------
// Basic Blocks
// -----------------------------------------------------------------
/** Main container structure of the simulation */
class Simulation extends Logger {
  import scala.collection.mutable.ArrayBuffer
  
  var space = new Space
  var agents = new ArrayBuffer[Agent]
}

/** Holds information about behaviours, subcontexts and memory of a certain part of the agents brain */
class Context(name:String) extends Logger {
  logName = name +"Context"
  
  import scala.collection.mutable.ArrayBuffer
  // TODO how do we set priorities here, simply by order of adding/ removing?
  // is ArrayBuffers positioning reliable?
  var children = new ArrayBuffer[Context]
  var behaviours = new ArrayBuffer[Behaviour]

  // TODO add getter & setter for context specific memory storage
  // how do we connect the memory to it?
  def update(dt:Float) {}
}

/** The simulated character that actually just is a parent context, wrapping a few subcontexts */
// TODO needs any more methods/ actions?
class Agent extends Context("Agent") {
  
}

/** defines a singule action-block within the context-tree */
class Behaviour(trigger: => Boolean, action: => Unit) {
  def apply(a:Agent, c:Context) = {
    if(trigger) action
  }
}

// -----------------------------------------------------------------
// Helpers
// -----------------------------------------------------------------
/** holds information about the simulated environment */
class Space {
  var width = 100
  var height = 100
  var depth = 100
}

trait Spaceable {
  import field.kit.math.Vec3
  var location = new Vec3
}

class SpaceLattice {}

class Octtree {}

/** Implements a simulation update strategy */
class Updater(s:Simulation) {
}

