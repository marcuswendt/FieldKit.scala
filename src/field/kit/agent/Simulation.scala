/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 13, 2009 */
package field.kit.agent

import field.kit.Logger

/** Main container structure of the simulation */
class Simulation extends Logger {
  import scala.collection.mutable.ArrayBuffer
  
  var space = new Space
  val agents = new ArrayBuffer[Agent]
}

/** Holds information about behaviours, subcontexts and memory of a certain part of the agents brain */
class Context(name:String) extends Logger {
  logName = name +"Context"
  
  import scala.collection.mutable.ArrayBuffer
  // TODO how do we set priorities here, simply by order of adding/ removing?
  // is ArrayBuffers positioning reliable?
  var children = new ArrayBuffer[Context]
  var behaviours = new ArrayBuffer[Behaviour]
  
  def update(dt:Float) {}
}

/** The simulated character that actually just is a parent context, wrapping a few subcontexts */
class Agent extends Context("Agent") {
  
}

class Behaviour(trigger: => Boolean, action: => Unit) {
	def apply(a:Agent, c:Context) {
	  if(trigger) action
	}
}

// -----------------------------------------------------------------
// Helpers
// -----------------------------------------------------------------
/** */
class Space {
	var width = 100
	var height = 100
	var depth = 100
}

trait Spaceable {
	import field.kit.math.Vec3
	var location = new Vec3
}


// create abstract base classes or direct 3d particle/ agent system?
// other non-traditional overlapping behaviours?
// prioritize behaviours ?
// differ between model and implementation?
  

// ------------------------------------
// CONTROLLER
// runs the simulation update
class Updater {
  
}

