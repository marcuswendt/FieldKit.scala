/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 13, 2010 */
package field.kit.test.math.geometry
{
	import field.kit.Sketch;
	import field.kit.math.MathUtil;
	import field.kit.math.Vec;
	import field.kit.math.Vec2;
	import field.kit.math.geometry.Curve;
	import field.kit.math.geometry.Polyline;
	import field.kit.math.geometry.Spline;
	import field.kit.math.geometry.BSpline;
	
	import flash.display.Graphics;
	
	[SWF(width="1280", height="760", scale="noscale", backgroundColor="#191919", frameRate="60")]

	public class CurveTest extends Sketch
	{
		private var curve:Curve
		private var curveType:int = 0
		private var numSamples:int = 50
			
		protected override function setup():void {
			curve = createCurve()
		}
		
		protected override function draw():void {
			graphics.clear()
			
			drawCurve()
			drawPoints()
			drawVertices()
		}
		
		protected function drawCurve():void {
			var p:Vec = curve.point(0)
			
			var g:Graphics = graphics
			g.lineStyle(1, 0xFFFFFF)
			g.moveTo(p.x, p.y)
			
			for(var i:int = 1; i <= numSamples; i++) {
				var t:Number = Number(i) / Number(numSamples)
				curve.point(t, p)
				g.lineTo(p.x, p.y)
			}
		}
		
		protected function drawPoints():void {
			var p:Vec = curve.point(0)
			
			var g:Graphics = graphics
			g.lineStyle()
			
			for(var i:int = 0; i<numSamples; i++) {
				var t:Number = i / Number(numSamples)
				curve.point(t, p)
				
				g.beginFill(0x00FF00)
				g.drawCircle(p.x, p.y, 3)
				g.endFill()
			}
		}
		
		protected function drawVertices():void {
			var g:Graphics = graphics
			g.lineStyle()
				
			for(var i:int = 0; i < curve.size; i++) {
				var p:Vec = curve.vertices[i]
				
				g.beginFill(0xFF0000)
				g.drawCircle(p.x, p.y, 6)
				g.endFill()
			}
		}
		
		protected function createCurve():Curve {
			var c:Curve
			
			if(curveType == 0) {
				c = new Polyline()
			} else if(curveType == 1) {
				c = new Spline()	
			} else if(curveType == 2) {
				c = new BSpline()
			}
			
			trace("Creating curve", c)
			
			var stage:Vec2 = new Vec2(stage.stageWidth, stage.stageHeight)

			var p:Vec2 = new Vec2
			p.y = stage.y * 0.5
				
			// add 4 initial points
			var numPoints:int = 4
			for(var i:int=0; i < numPoints; i++) {
				p.x = (i + 1)/ Number(numPoints+1) * stage.x
				p.y = MathUtil.randomRange(stage.y * 0.15, stage.y * 0.85)  
				c.addVertex(p.clone())
			}
			
			return c
		}
		
		protected override function keyPressed():void {
			//trace("keyDown", keyCode)
			
			if(keyCode == KEY_SPACE) {
				curveType += 1
				if(curveType == 3) curveType = 0
				curve = createCurve()
			}
		}
	}
}