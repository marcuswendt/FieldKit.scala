/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math
{
	/**
	 * 2 dimensional vector class
	 * @author Marcus Wendt
	 */
	public class Vec2 extends Vec
	{
		public function Vec2(x:Number = 0, y:Number = 0) {	
			setS(x, y)
		}
		
		// -- Scalar Operations ------------------------------------------------		
		public override function addS(s:Number):Vec {
			return new Vec2(x + s, y + s)
		}
		
		public override function addSelfS(s:Number):Vec {
			this.x += s; this.y += s
			return this
		}
		 
		public override function subS(s:Number):Vec {
			return new Vec2(x - s, y - s)
		}
		
		public override function subSelfS(s:Number):Vec {
			this.x -= s; this.y -= s
			return this
		}
		
		public override function mulS(s:Number):Vec {
			return new Vec2(x * s, y * s)
		}
		
		public override function mulSelfS(s:Number):Vec {
			this.x *= s; this.y *= s
			return this
		}
		
		public override function divS(s:Number):Vec {
			return new Vec2(x / s, y / s)
		}
		
		public override function divSelfS(s:Number):Vec {
			this.x /= s; this.y /= s
			return this
		}
		
		// -- Vector Operations ------------------------------------------------
		public override function set(v:Vec):Vec {
			this.x = v.x; this.y = v.y;
			return this
		}
		
		public override function add(v:Vec):Vec {
			return new Vec2(x + v.x, y + v.y)
		}
		
		public override function addSelf(v:Vec):Vec {
			this.x += v.x; this.y += v.y
			return this
		}
		 
		public override function sub(v:Vec):Vec {
			return new Vec2(x - v.x, y - v.y)
		}
		
		public override function subSelf(v:Vec):Vec {
			this.x -= v.x; this.y -= v.y
			return this
		}
		
		public override function mul(v:Vec):Vec {
			return new Vec2(x * v.x, y * v.y)
		}
		
		public override function mulSelf(v:Vec):Vec {
			this.x *= v.x; this.y *= v.y
			return this
		}
		
		public override function div(v:Vec):Vec {
			return new Vec2(x / v.x, y / v.y)
		}
		
		public override function divSelf(v:Vec):Vec {
			this.x /= v.x; this.y /= v.y
			return this
		}
		 
		public override function dot(v:Vec):Number {
			return x * v.x + y * v.y
		}
		
		public override function get lengthSquared():Number {
			return x * x + y * y
		}
				
		/** Normalizes the vector to the given length. */
		public override function normalizeSelfTo(len:Number):Vec {
			var mag:Number = Math.sqrt(x * x + y * y)
			if(mag > 0) {
				mag = len / mag
				x *= mag
				y *= mag
			}			
			return this
		}
		
		public override function distanceSquared(v:Vec):Number {
			var dx:Number = this.x - v.x
			var dy:Number = this.y - v.y
			return dx * dx + dy * dy
		}
				
		public override function interpolate(target:Vec, delta:Number):Vec {
			var tx:Number = x + (target.x - x) * delta
			var ty:Number = y + (target.y - y) * delta
			return new Vec2(tx, ty)
		}
		
		public override function interpolateSelf(target:Vec, delta:Number):Vec {
			x += (target.x - x) * delta
			y += (target.y - y) * delta
			return this
		}
		
		public function angle():Number {
			return Math.atan2(x, y)
		}
		
		/** returns a copy (new instance) of the vector */
		public function clone():Vec2 {
			return new Vec2(x,y)
		}
		
		public function toString():String {
			return "Vec2["+ x.toFixed(2) +","+ y.toFixed(2) +"]"			
		} 
	}
}