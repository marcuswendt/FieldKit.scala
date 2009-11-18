/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 13, 2009 */
package field.kit.test.agent

import field.kit.test.Sketch

/*
object PredatorPreySimulation extends field.kit.Sketch {
  import field.kit.agent._
  import field.kit.math._
  
  abstract class SteeringAgent(name:String) extends Agent(name) {
    var position = new Vec3
    var velocity = new Vec3
    var steer = new Vec3
    var steerMax = 1f
    var velocityMax = 10f
    
    protected val absVelocity = new Vec3
    
    // -- Init ----------------------------------------------------------------
    var isInitialized = false
    var init = reason += ("init_checker", { !isInitialized })
    
    init += ("random_emit", {
      position.x = Random(0, width)
      position.y = Random(0, height)
      position.z = Random(0, 0)
      true
    })
    
    init += ("init_done", { isInitialized = true; true })
    
    // -- Motor ----------------------------------------------------------------
    motor += ("euler_integration", {
      // update driving force
      steer.clamp(steerMax)
      velocity += steer
      velocity.clamp(velocityMax)
  
      // make velocity time invariant
      absVelocity(velocity) *= (dt / sim.timeStep)
      position += absVelocity

      // clean up
      velocity *= 0.97f
      steer.zero
    
      true
    })
    
    motor += ("wrap", {
      val pos = position
      val _max = new Vec3(width, height, 100f)
      val _min = new Vec3()
      
      if(pos.x < _min.x)
        pos.x = _max.x
      else if(pos.x > _max.x)
        pos.x = _min.x
      
      if(pos.y < _min.y)
        pos.y = _max.y
      else if(pos.y > _max.y)
        pos.y = _min.y
      
      if (pos.z < _min.z)
        pos.z = _max.z
      else if (pos.z > _max.z)
        pos.z = _min.z
      
      true
    })
    
    def draw
  }

  // -- Init -------------------------------------------------------------------
  var sim = new Simulation("PredatorPreySimulation")
  
  init({
    info("starting simulation")
    
    sim += new SteeringAgent("predator") {
      val sight = 50f
    
      motor += {
        steer += (1.0f, 0f, 0f)
        true
      }
    
      def draw {
        noStroke
        fill(255,0,0)
        ellipse(position.x, position.y, 9, 9)
      
        noFill
        stroke(255,0,0)
        ellipse(position.x, position.y, sight, sight)
      } 
    }
  
    for(i <- 0 until 25) {
      sim += new SteeringAgent("prey"+i) {
        val sight = 100f
        var age = 1f
	    var wander1 = random(0f, 1f)
	    var wander2 = random(0f, 1f)
     
	    sensor += ("aging", {
	      age += dt
	      true
	    })
     
	    motor += ("wander", {
	      val weight = 0.1f
	      steer.x += (wander1 * 2f - 1f) * weight
	      steer.y += (wander2 * 2f - 1f) * weight
	      true
	    })
     
	    def draw {
	      noStroke
	      fill(0,255,0)
	      ellipse(position.x, position.y, 5, 5)
	      
	      noFill
	      stroke(0,255,0)
	      ellipse(position.x, position.y, sight, sight)
	    }
	  }
    }
   
    sim.start
  })
  
  def render {
    background(0)
    rect(mouseX, mouseY, 10, 10)
    
    sim foreach({ agent:Agent =>
      agent.asInstanceOf[SteeringAgent].draw
    })
  }
}
*/