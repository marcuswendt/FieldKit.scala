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
  import kit.gl.render._
  import kit.gl.scene._
  import kit.gl.scene.shape._
  import kit.gl.scene.state._
  import kit.util._
  
  var pass:Pass = null
  var scene:Spatial = null
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA,  {
    info("initializing scene")
    scene = new TestParticleSystemScene
    
    info("initializing render pass")
    val ss = ShaderState(
    		"res/test/shader/default.vs", 
    		"res/test/shader/default.fs")
    pass = new Pass("blurPass", ss, width, height, true, false)
  })
  
  def render {
    background(0)
    
    beginGL
    pass.pre
    scene.render
    pass.post
    
    pass.render
    endGL
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
    ps.space = new Space(width, height, 100)
    
    val f = new Flock[Particle]
    ps += f
    
    f.emitter.rate = 1
    f.emitter.interval = 100
    
    f.emitter:= ps.space.dimension * 0.5f
    
    f.emitter += new Behaviour {
      logName = "initializer"
      def apply(p:Particle, dt:Float) {
        p.velocityMax = 100f
        p.steerMax = 25f
      }
    }

    f += new Wind
    f += new Wrap2D
    f += new Behaviour {
      logName = "perlin"
      
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
    def draw {}
    
    override def render {
      import kit.math.Common._
      import javax.media.opengl._
    
      // update
      ps.update(timer.update)
      
      // draw
//      fill(255)
//      noStroke
      //f.particles foreach(p => rect(p.x, p.y, 6, 6))
        
//	  gl.glColor4f(1f,1f,0f,1f)
      gl.glPointSize(6f)
      gl.glEnable(GL.GL_POINT_SMOOTH)
      gl.glBegin(GL.GL_POINTS)
      f.particles foreach { p =>
        gl.glVertex2f(p.x, p.y)
      }
      gl.glEnd
    }
  }
}
