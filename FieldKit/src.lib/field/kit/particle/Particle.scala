/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.math._
import field.kit._

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
  
  // basic properties
  var age = 0f
  var lifeTime = 10 * 1000f
  var size = 1f
  
  var velocity = Vec3()
  var steer = Vec3()
  var steerMax = 1f
  var velocityMax = 10f
  
  // extended properties
  var colour = Colour()
  var colourSteer = Vec4()
  var colourVelocity = Vec4()
  var colourSteerMax = 0.1f
  var colourVelocityMax = 0.5f
  
  // internal
  protected val absVelocity = Vec3()
  protected val absColourVelocity = Vec4()
  
  /** called automatically when the particle is added to the flock */
  def init {}
  
  // perform euler integration
  def update(dt:Float) {
    age += dt
    updatePosition(dt)
    updateColour(dt)
  }
  
  def updatePosition(dt:Float) {
    // integrate velocity -> position 
    steer.clamp(steerMax)
    velocity += steer
    velocity.clamp(velocityMax)
  
    // make velocity time invariant
    absVelocity := velocity *= (dt / ps.timeStep)
    this += absVelocity
    
    velocity *= ps.friction
    steer.zero
  }

  /** integrates colour steering*/
  def updateColour(dt:Float) {
    colourSteer.clamp(colourSteerMax)
    colourVelocity += colourSteer
    colourVelocity.clamp(colourVelocityMax)
    
    absColourVelocity := colourVelocity *= (dt / ps.timeStep)
    this.colour += (absColourVelocity.x, absColourVelocity.y, absColourVelocity.z, absColourVelocity.w)
    
    colourVelocity *= ps.friction
    colourSteer.zero
  }
  
  override def toString = "Particle["+ toLabel +"]"
}
