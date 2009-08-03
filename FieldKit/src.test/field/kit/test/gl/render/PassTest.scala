/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 04, 2009 */
package field.kit.test.gl.render

/** 
 * quick test for the glsl shader state feature
 * TODO needs finishing
 */
object PassTest extends field.kit.test.Sketch {
  import field.kit.gl.render._
  import field.kit.gl.scene._
  import field.kit.gl.scene.shape._
  import field.kit.gl.scene.state._
  
  var pass:Pass = null
  var scene:Spatial = null
  
  init({
    import java.net.URL
    
    info("initializing scene")
    scene = new TestParticleSystemScene
    
  })
  
  def render {
    background(0)
//    beginGL
//    scene.render
//    endGL
  	scene.render
  }
  
  
  // -- Scene ------------------------------------------------------------------
  
  /** implements a simple particle system based scene */
  class TestParticleSystemScene extends Spatial("ParticleSystemScene") {
    import field.kit.particle._
    import field.kit.particle.behaviour._
    import field.kit.util.Timer
  
    info("width", width, "height", height)
    
    // initialize particle system
    val ps = new ParticleSystem
    ps.space.set(width, height, 100)
    
    val f = new Flock[Particle]
    f.emitter.rate = 1
    f.emitter.interval = 100
    f.emitter += new Behaviour("initializer") {
      def apply(p:Particle, dt:Float) {
        p.velocityMax = 100f
        p.steerMax = 25f
      }
    }
    
    ps += f
    f += new Wind
    f += new Wrap
    f += new Behaviour("perlin") {
      var time = 0f
      var tmp = 0f
    
      override def prepare(dt:Float) = time += dt
    
      def apply(p:Particle, dt:Float) {
        val weight = 10f
        p.steer.x += (noise(tmp, p.age) * 2f - 1f) * weight
        p.steer.y += (noise(time, p.age) * 2f - 1f) * weight
        tmp += dt
      }
    }
    
    val timer = new Timer
    
    // initialize particle geometry
  
    def draw {
//      // set scene to the center of the screen
//      translation set (width/2f, height/2f, 0)
//      scale set 2
    
      // update
      ps.update(timer.update)
      
      // draw
      fill(255)
      noStroke
      f.particles foreach(p => rect(p.x, p.y, 6, 6))
    }
  }
}
