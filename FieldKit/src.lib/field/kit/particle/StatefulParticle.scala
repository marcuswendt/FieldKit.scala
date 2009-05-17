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
  var state:Particle.State.Value = null
  
  /** the next state after the transition*/
  var nextState:Particle.State.Value = null
  
  /** the current time in this state */
  var time = 0f
  
  /** the duration of the next/ current state transition */
  var transitionDuration = 0f
  
  /** called when this particle instance is reused by the emitter again */
  def reinit {
    state = Particle.State.ALIVE
    age = 0
    position.zero
    velocity.zero
    steer.zero
  }
  
  def switch(newState:Particle.State.Value, transitionDuration:Float) {
    if(transitionDuration == 0) {
      this.state = newState
      this.nextState = null
    } else {
      this.state = Particle.State.TRANSITIONING
      this.nextState = newState
      this.transitionDuration = transitionDuration
    }
    time = 0
  }
  
  /** @return true when this particle is alive or transitioning otherwise false */
  def isAlive = state != Particle.State.DEAD
  
  /** @return true when this particle is definitively dead ;) */
  def isDead = state == Particle.State.DEAD
  
  override def update(dt:Float) {
    super.update(dt)
    time += dt
    
    // check if we reached the new state
    if(state == Particle.State.TRANSITIONING && time >= transitionDuration) 
      switch(nextState, 0)
    
    // check wether particle is dead
    if(age > lifeTime && lifeTime != Particle.INFINITE) 
      switch(Particle.State.DEAD, 0)
  }
}
