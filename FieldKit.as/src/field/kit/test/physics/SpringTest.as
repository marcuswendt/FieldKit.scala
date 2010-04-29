/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.test.physics
{
	import field.kit.math.MathUtil;
	import field.kit.math.Vec2;
	import field.kit.math.Vec3;
	import field.kit.physics.Particle;
	import field.kit.physics.Physics;
	import field.kit.physics.Spring;
	import field.kit.Sketch;
	import field.kit.util.Timer;
	
	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.events.Event;
	
	import mx.utils.NameUtil;

	[SWF(width="1280", height="760", scale="noscale", backgroundColor="#191919", frameRate="60")]

	public class SpringTest extends Sketch
	{
		private var timer:Timer
		private var physics:Physics
		
		protected override function setup():void {

			timer = new Timer()
			physics = new Physics()
			physics.space.setDimensionS(stage.stageWidth, stage.stageHeight, 0)
			physics.emitter.rate = 0
				
			// initialiser
			physics.emitter.addBehaviour(function(p:Particle):void {
				p.size = MathUtil.randomRange(3, 7)
				p.position.x = MathUtil.randomRange(0, physics.space.width)
				p.position.y = MathUtil.randomRange(0, physics.space.height)
				p.position.z = MathUtil.randomRange(0, physics.space.depth)
				p.clearVelocity()
			})
				
			// gravity
			physics.addBehaviour(function(p:Particle):void {
				p.force.y += 0.1
			})
			
			// create springs
			for(var i:int=0; i<64; i++) {
				var a:Particle = physics.emitter.emit()
				var b:Particle = physics.emitter.emit()
				var len:Number = MathUtil.randomRange(10, 25)
				var strength:Number = MathUtil.randomRange(0.01, 0.5)
				var s:Spring = new Spring(a, b, strength, len)
				a.lock()
					
				physics.addSpring(s)
			}				
		}
		
		protected override function draw():void {
			// update physics system
			physics.update(timer.update())
		
			// redraw scene
			graphics.clear()
			drawParticles()
		}
		
		private function drawParticles():void {
			var g:Graphics = graphics
				
			for(var i:int = 0; i < physics.numParticles; i++) {
				var p:Particle = physics.particles[i]
					
				g.beginFill(0xffffff)
				g.drawCircle(p.position.x, p.position.y, p.size)
				g.endFill()
			}
			
			g.lineStyle(2, 0xFFFFFF)
			for(var j:int =0; j < physics.numSprings; j++) {
				var s:Spring = physics.springs[j]
				g.moveTo(s.a.position.x, s.a.position.y)
				g.lineTo(s.b.position.x, s.b.position.y)
			}
		}
	}
}