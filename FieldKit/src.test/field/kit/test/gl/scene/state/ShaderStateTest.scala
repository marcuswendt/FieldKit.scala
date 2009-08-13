/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 03, 2009 */
package field.kit.test.gl.scene.state

/** 
 * quick test for the glsl shader state feature
 */
object ShaderStateTest extends test.Sketch {
  import field.kit.gl.scene._
  import field.kit.gl.scene.shape._
  import field.kit.gl.scene.state._
  
  var scene:Group = null
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, {
    import java.net.URL
    
    info("initializing scene")
    scene = new Group("scene")
    
    val q1 = new Quad("test", width/2f, height/2f)
    q1.translation := (width/2f, height/2f, 0)
    q1.states += ShaderState("""
	void main()
	{
		gl_FrontColor = gl_Color;
		gl_TexCoord[0] = gl_MultiTexCoord0;
		gl_Position = ftransform();
	}
    """, """
	uniform sampler2D tex;
	
	void main()
	{
		vec4 color = vec4(1.0, 0.0, 0.0, 1.0);
		gl_FragColor = color; // * texture2D(tex, gl_TexCoord[0].xy);
	}   
    """)
    
    //q1.states += ShaderState(null.asInstanceOf[URL],null.asInstanceOf[URL])
    
    scene += q1
  }:Unit)
  
  def render {
    background(0)
    beginGL
    scene.render
    endGL
  }
}
