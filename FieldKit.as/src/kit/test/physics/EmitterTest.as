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
	import field.kit.Sketch;
	import field.kit.util.Timer;
	
	import flash.display.Graphics;
	import flash.display.Sprite;
	import flash.events.Event;

	[SWF(width="1280", height="760", scale="noscale", backgroundColor="#191919", frameRate="60")]

	public class EmitterTest extends Sketch
	{
		private var timer:Timer
		private var physics:Physics
		
		protected override function setup():void {
			timer = new Timer()
			physics = new Physics()
			physics.space.setDimensionS(stage.stageWidth, stage.stageHeight, 0)
				
			// initialiser
			physics.emitter.addBehaviour(function(p:Particle):void {
				p.size = MathUtil.randomRange(0, 25)
				p.position.x = MathUtil.randomRange(0, physics.space.width)
				p.position.y = MathUtil.randomRange(0, physics.space.height)
				p.position.z = MathUtil.randomRange(0, physics.space.depth)
				p.clearVelocity()
			})
				
			// simple closure
			physics.addBehaviour(function(p:Particle):void {
				p.force.y += 0.01
			})
				
			// testing closure context
			physics.addConstraint(function(p:Particle):void {
				if(p.position.y > physics.space.height) {
					p.position.y = physics.space.height
				} 
			})
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
		}
	}
}