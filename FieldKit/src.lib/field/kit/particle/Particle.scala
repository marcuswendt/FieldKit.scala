/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.math.Vec3
import field.kit.Logger

object Particle {
  val UNDEFINED = -1
  
  // states
  val TRANSITIONING = 0
  val ALIVE = 1
  val DEAD = 2
}

/**
 * A single particle from the flock
 * @author Marcus Wendt
 */
class Particle extends Vec3 with Logger {
  // set when particle is being added to a flock
  var flock:Flock[_] = null
  var ps:ParticleSystem = null
  var id = 0
  
  var age = 0f
  var lifeTime = 10 * 1000f
  var size = 1f
  
  var velocity = Vec3()
  var steer = Vec3()
  var steerMax = 1f
  var velocityMax = 10f
  
  protected val absVelocity = Vec3()
  
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
    absVelocity := velocity *= (dt / ps.timeStep)
    this += absVelocity

    // clean up
    velocity *= ps.friction
    steer.zero
  }
  
   override def toString = "Particle["+ toLabel +"]"
}
