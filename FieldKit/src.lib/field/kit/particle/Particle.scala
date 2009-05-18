/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger

object Particle {
  val UNDEFINED = -1
  
  val ALIVE = 0
  val TRANSITIONING = 1
  val DEAD = 2
  
  object State extends Enumeration {
    val TRANSITIONING = Value
    val ALIVE = Value
    val DEAD = Value
  }
}

/**
 * a single particle from the flock
 * @author Marcus Wendt
 */
class Particle extends Logger {
  import field.kit.math.Vec3
  fine("init")
  
  // set when particle is being added to a flock
  var flock:Flock[_] = null
  var ps:ParticleSystem = null
  
  var age = 0f
  var lifeTime = 10 * 1000f
  
  var position = new Vec3
  var velocity = new Vec3
  var steer = new Vec3
  var steerMax = 1f
  var velocityMax = 10f
  
  protected val absVelocity = new Vec3
  
  /** called automatically when the particle is added to the flock */
  def init {}
  
  // perform euler integration
  def update(dt:Float) {
    age += dt
        
    // update driving force
    steer.clamp(steerMax)
    velocity += steer
    velocity.clamp(velocityMax)
  
    // make velocity time invariant
    absVelocity(velocity) *= (dt / ps.timeStep)
    position += absVelocity

    // clean up
    velocity *= ps.friction
    steer.zero
  }
}
