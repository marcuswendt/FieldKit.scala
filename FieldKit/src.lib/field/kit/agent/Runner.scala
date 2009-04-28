/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 17, 2009 */
package field.kit.agent

import field.kit.Logger

// TODO implement simulation runners e.g. actor-based, singlethreaded etc. 

/** Implements a simulation update strategy */
abstract class Runner(s:Simulation) extends Logger {
  var isRunning = false
  
  def start = if(!isRunning) isRunning = true
  def stop = if(isRunning) isRunning = false
}

class SimpleRunner(s:Simulation) extends Runner(s) with Runnable {
  info("init")
  
  var t:Thread = null

  override def start = {
    if(!isRunning) {
      info("start")
      super.start
      t = new Thread(this)
      t.start
    }
  }
  
  override def stop = {
    if(isRunning) {
      info("stop")
      super.stop
    }
  }
  
  def run {
    def time = System.currentTimeMillis
    var last = time
    
	while(isRunning) {
	  val now = time
	  val dt = now - last
	  last = now
   
	  // run the update
	  s.update(dt)
   
	  Thread.sleep(s.timeStep.asInstanceOf[Long])
	}
  }
}
