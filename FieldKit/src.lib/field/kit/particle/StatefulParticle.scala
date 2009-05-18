/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 17, 2009 */
package field.kit.particle

/**
 * Extends the basic particle with a state machine, allowing to reuse particles
 */
class StatefulParticle extends Particle {
  
  /** the current state */
  var state = Particle.UNDEFINED
  
  /** the next state after the transition*/
  var nextState = Particle.UNDEFINED
  
  /** the current time in this state */
  var time = 0f
  
  /** the duration of the next/ current state transition */
  var transitionDuration = 0f
  
  /** called when this particle instance is reused by the emitter again */
  def reinit {
    state = Particle.ALIVE
    age = 0
    position.zero
    velocity.zero
    steer.zero
  }
  
  def switch(newState:Int, transitionDuration:Float) {
    if(transitionDuration == 0) {
      this.state = newState
      this.nextState = Particle.UNDEFINED
    } else {
      this.state = Particle.TRANSITIONING
      this.nextState = newState
      this.transitionDuration = transitionDuration
    }
    time = 0
  }
  
  /** @return true when this particle is alive or transitioning otherwise false */
  def isAlive = state != Particle.DEAD
  
  /** @return true when this particle is definitively dead ;) */
  def isDead = state == Particle.DEAD
  
  override def update(dt:Float) {
    super.update(dt)
    time += dt
    
    // check if we reached the new state
    if(state == Particle.TRANSITIONING && time >= transitionDuration) 
      switch(nextState, 0)
    
    // check wether particle is dead
    if(age > lifeTime && lifeTime != Particle.UNDEFINED) 
      switch(Particle.DEAD, 0)
  }
}
