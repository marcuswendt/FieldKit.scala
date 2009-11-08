/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 8, 2009 */
package field.kit.particle

import actors.Actor
import actors.Actor._

case object Status
case object IsFinished
case object IsWorking

case object Update
case class Update(dt:Float)
case object Complete

/**
 * An actor based multi-threaded particle system
 * @author Marcus Wendt
 */
//class ParallelParticleSystem(val teamSize:Int) extends ParticleSystem with Actor {
class ParallelParticleSystem(val teamSize:Int) extends ParticleSystem {

  val flockUpdaters = new Array[FlockUpdater](teamSize)
  val flockPreparer = createFlockPreparer
  val spaceUpdater = createSpaceUpdater
    
  // initialise
  for(i <- 0 until teamSize) flockUpdaters(i) = createFlockUpdater(i)
  
  // start
  flockUpdaters foreach (_.start)
  flockPreparer.start
  spaceUpdater.start
  
  // helpers
  protected def createFlockUpdater(id:Int) = new FlockUpdater(this, id)
  
  protected def createFlockPreparer = new FlockPreparer(this)
    
  protected def createSpaceUpdater = new SpaceUpdater(this)
    
  override def update(dt:Float) {
    flockPreparer ! Update(dt)
    
    flockUpdaters foreach (_ ! Update(dt))
    
    var isWorking = true
    while(isWorking) {
      isWorking = false
      flockUpdaters foreach {
        _ !? Status match {
          case IsWorking => isWorking = true
          case _ =>
        }
      }
      
      Thread.sleep(2)
    }
    
    spaceUpdater ! Update
  }
}

class FlockUpdater(ps:ParallelParticleSystem, id:Int) extends Actor {
  protected var isWorking = false
  
  def act {
    while(true) {
      receive {
        case Update(dt:Float) =>
          isWorking = true
          var i = 0
          while(i < ps.flocks.size) {
            ps.flocks(i).update(dt, id, ps.teamSize)
            i += 1
          }          
          //sender ! ParallelParticleSystem.Complete
          isWorking = false
          
        case Status =>
          if(isWorking) 
            sender ! IsWorking  
          else 
            sender ! IsFinished
      }
    }
  }
}

class FlockPreparer(ps:ParallelParticleSystem) extends Actor {
   def act {
    while(true) {
      receive {
        case Update(dt:Float) =>
//          println("prepare", dt)
          
          var i = 0
          while(i < ps.flocks.size) {
            ps.flocks(i).prepare(dt)
            i += 1
          }
      }
    }
  }
}

class SpaceUpdater(ps:ParallelParticleSystem) extends Actor {
   def act {
    while(true) {
      receive {
        case Update =>
//          println("space")
          ps.updateSpace
      }
    }
  }
}
