/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent.space

/** holds information about the simulated environment */
class Space {
  var width = 100
  var height = 100
  var depth = 100
}

/** marks the given object as having a position */
trait Spaceable {
  import field.kit.math.Vec3
  var location = new Vec3
}


// -----------------------------------------------------------------
// Helpers
// -----------------------------------------------------------------

/** a spatial optimisation method (e.g. distance-checking) */
class SpaceLattice {}

/** a spatial optimisation method (e.g. distance-checking) */
class Octtree {}