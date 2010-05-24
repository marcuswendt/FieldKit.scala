/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 20, 2010 */
package field.kit.test.gl

import field.kit.p5._

object Stroke2DTest extends Sketch {
	import field.kit._
	import javax.media.opengl.GL
	import processing.core.PConstants._
	import collection.mutable.ArrayBuffer
	
	// -- Gestures -------------------------------------------------------------
	
	/** Interface to use different types of gestures */
	abstract class Gesture {
		var colour = Colour(Colour.WHITE)
		
		def update(dt:Float)
		def size:Int
		def vertex(i:Int):Vec3
		
		// mouse/ interaction events
		def begin(x:Float, y:Float)
		def end
		def insert(x:Float, y:Float)
	}
	
	class SpringGestureConfig {
		var particleInterval = 10f
		var particleMax = 1000
		var springStrength = 0.1f
	}
	
	/** special gesture constructred from springs */
	class SpringGesture(config:SpringGestureConfig) extends Gesture {
		import field.kit.physics._
		import field.kit.physics.behaviours._
		
		var isActive = false
		
		var physics = new Physics[Particle]
		physics.space = new OctreeSpace(Vec3(width, height, height))
		
		physics.emitter.rate = 0
		physics.emitter.interval = config.particleInterval
		physics.emitter.max = config.particleMax
		
		protected val curPos = new Vec3
		protected var prev:Particle = _
		
		protected val tmp = new Vec3
		
		def update(dt:Float) = {
			physics.update(dt)
			
			// spring a-b attractor
			if(isActive && physics.springs != null) {
				physics.springs foreach { s =>
					tmp := s.b
					tmp -= s.a
					tmp.normalizeTo(config.springStrength)
					
					s.a += tmp
				}
			}
		}
		
		def size = physics.particles.size
		
		def vertex(i:Int) = physics.particles(i)
		
		// mouse/ interaction events
		def begin(x:Float, y:Float) {
			curPos := (x,y,0f)
			prev = physics.emitter.emit(curPos)
		}
		
		def end {
			isActive = true
		}
		
		def insert(x:Float, y:Float) {
			curPos := (x,y,0f)
			val cur = physics.emitter.emit(curPos)
			
			val len = cur.distance(prev)
			val strength = 0.1f
			val s = new Spring(prev, cur, len, strength)
			
			physics += s
			prev = cur
		}
	}
	
	class NoiseSpringGestureConfig extends SpringGestureConfig {
		var minDistance = 5f
	}
	
	class NoisySpringGesture(config:NoiseSpringGestureConfig, var offset:Float, var jitter:Float) 
	extends SpringGesture(config) {
		
		val vertexPrev = new Vec3
		
		override def begin(x:Float, y:Float) {
			super.begin(x, y)
			vertexPrev := prev
		}
		
		override def insert(x:Float, y:Float) {
			val vertex = (x,y,0f)
			
			if(vertex.distance(vertexPrev) < config.minDistance) return
			
			// vector from current to prev
			tmp := vertex
			tmp -= vertexPrev
			
			// calculate orthogonal
			val swap = tmp.x
			tmp.x = tmp.y
			tmp.y = swap
			
			// scale orthogonal
			val ortho = tmp
			ortho.normalizeTo(offset + noise(x, y) * jitter)

			// add offsetted vertex
			super.insert(x + ortho.x, y + ortho.y)
			
			vertexPrev := vertex
		}
	}
	
	// -- Strokes --------------------------------------------------------------
	import field.kit.gl.scene.shape.Stroke2D
	import field.kit.math.geometry.BSpline
	
	class StrokeConfig {
		var capacity = 1000
		var numSamples = 200
		var tipStartLength = 0.1
		var tipEndLength = 0.1
	}
	
	class GestureStroke(config:StrokeConfig, val gesture:Gesture) extends Stroke2D("stroke", config.capacity) {
		
		var width = 5f
		
		var age = 0f
		var lifeTime = 10000f
		
		var curve = new BSpline(config.capacity)
		
		def isActive = age < lifeTime
		
		def update(dt:Float) {
			
			age += dt
			
			// check if its time to remove this stroke
			if(!isActive) {
				clear
				return
			}
			
			// update gesture
			gesture.update(dt)
			
			// update curve
			curve.clear
			var i = 0
			while(i < gesture.size) {
				val p = gesture.vertex(i)
				curve.+=(p.x,p.y,p.z)
				i += 1
			}
			
			// update stroke
			clear
			i = 0
			val tmp = new Vec3
			while(i < config.numSamples) {
				val timeCurve = i/config.numSamples.toFloat
				curve.point(timeCurve, tmp)
				
				// calculate weight
				val timeLife = (1f - (age / lifeTime))
				var weight = timeLife * width
				
				// start tip
				if(timeCurve < config.tipStartLength) 
					weight *= timeCurve / config.tipStartLength
				
				// end tip
				if(1f - timeCurve < config.tipEndLength) 
					weight *= (1f - timeCurve) / config.tipEndLength
					
				this += (tmp.x,tmp.y, weight)
				i += 1
			}
			
			super.update()
		}
	}
	
	// -- App ------------------------------------------------------------------
	var strokes = new ArrayBuffer[GestureStroke]
	var current = new ArrayBuffer[Gesture]
	var timer:Timer = _
	var showDebug = false
	
	var gestureConfig = new NoiseSpringGestureConfig
	var strokeConfig = new StrokeConfig
	
	import field.kit.gl.objects._
	import field.kit.gl.scene.state._
	var shader:ShaderState = _
	
	override def setup {
		size(1280, 720, OPENGL)
		hint(ENABLE_OPENGL_4X_SMOOTH)
		smooth()
		background(0)
		
		timer = new Timer
		shader = new ShaderState(VertexShader.DEFAULT, new FragmentShader("""
			uniform sampler2D tex;
	
			void main() {
				vec2 tc = gl_TexCoord[0].xy;
				gl_FragColor = vec4(tc.s, tc.t, 0, 1);
			}   
		"""))
	}
	
	override def draw {
		background(0)

		// add points to current gesture
		if(mousePressed)
			current foreach { g =>
				g.insert(mouseX, mouseY)
			}
		
		// update and draw all gestures
		val dt = timer.update
		strokes foreach { s =>
			s.update(dt)
			if(!s.isActive)
				strokes -= s
		}
		
		beginGL
		strokes foreach { s =>
			s.render
		}
		endGL
		
		if(showDebug) {
			strokes foreach { s => 
				drawGesture(s.gesture)
				drawDebug(s.gesture)
			}
		}
	}
	
	protected def drawDebug(g:Gesture) {
		beginGL
		gl.glColor4f(1f, 0, 0, 1f)
		gl.glPointSize(5f)
		gl.glBegin(GL.GL_POINTS)
		var i = 0
		while(i < g.size) {
			val p = g.vertex(i)
			gl.glVertex2f(p.x, p.y)
			i += 1
		}
		gl.glEnd
		endGL
	}
	
	protected def drawGesture(g:Gesture) {
		beginGL
		gl.glColor4f(1f, 0f, 0f, 1f)
		gl.glBegin(GL.GL_LINE_STRIP)
		var i = 0
		while(i < g.size) {
			val p = g.vertex(i)
			gl.glVertex2f(p.x, p.y)
			i += 1
		}
		gl.glEnd
		endGL
	}
	
	override def mousePressed {
		var num = random(1, 10).toInt
		for(i <- 0 until num) {
			val g = new NoisySpringGesture(gestureConfig, randomNormal * 25f, 7.5f)
			g.begin(mouseX, mouseY)
			current += g
			
			val s = new GestureStroke(strokeConfig, g)
			s.width = random(2f, 15f)
			s.lifeTime = random(2f, 10f) * 1000f
			s += shader
			strokes += s
		}
	}
	
	override def mouseReleased {
		current foreach { _.end }
		current.clear
	}
	
	override def keyPressed {
		super.keyPressed()
		
		key match {
			case 'd' => showDebug = !showDebug
			case ' ' => strokes.clear
			case _ =>
		}
	}
}
