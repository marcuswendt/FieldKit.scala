/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math
{
	import flash.geom.Vector3D;

	/**
	 * 3 dimensional vector class
	 * @author Marcus Wendt
	 */
	public class Vec3 extends Vec
	{
		
		public function Vec3(x:Number = 0, y:Number = 0, z:Number = 0) {	
			setS(x, y, z)
		}

		// -- Scalar Operations ------------------------------------------------
		public override function addS(s:Number):Vec {
			return new Vec3(x + s, y + s, z + s)
		}
		
		public override function addSelfS(s:Number):Vec {
			this.x += s; this.y += s; this.z += s
			return this
		}
		 
		public override function subS(s:Number):Vec {
			return new Vec3(x - s, y - s, z - s)
		}
		
		public override function subSelfS(s:Number):Vec {
			this.x -= s; this.y -= s; this.z -= s;
			return this
		}
		
		public override function mulS(s:Number):Vec {
			return new Vec3(x * s, y * s, z * s)
		}
		
		public override function mulSelfS(s:Number):Vec {
			this.x *= s; this.y *= s; this.z *= s
			return this
		}
		
		public override function divS(s:Number):Vec {
			return new Vec3(x / s, y / s, z / s)
		}
		
		public override function divSelfS(s:Number):Vec {
			this.x /= s; this.y /= s; this.z /= s;
			return this
		}
		
		// -- Vector Operations ------------------------------------------------
		public override function add(v:Vec):Vec {
			return new Vec3(x + v.x, y + v.y, z += v.z)
		}
		
		public override function addSelf(v:Vec):Vec {
			this.x += v.x; this.y += v.y; this.z += v.z
			return this
		}
		 
		public override function sub(v:Vec):Vec {
			return new Vec3(x - v.x, y - v.y, z - v.z)
		}
		
		public override function subSelf(v:Vec):Vec {
			this.x -= v.x; this.y -= v.y; this.z -= v.z
			return this
		}
		
		public override function mul(v:Vec):Vec {
			return new Vec3(x * v.x, y * v.y, z * v.z)
		}
		
		public override function mulSelf(v:Vec):Vec {
			this.x *= v.x; this.y *= v.y; this.z *= v.z
			return this
		}
		
		public override function div(v:Vec):Vec {
			return new Vec3(x / v.x, y / v.y, z / v.z)
		}
		
		public override function divSelf(v:Vec):Vec {
			this.x /= v.x; this.y /= v.y; this.z /= v.z
			return this
		}
		
		public override function zero():Vec {
			this.x = 0; this.y = 0; this.z = 0
			return this
		}
		 
		public override function dot(v:Vec):Number {
			return x * v.x + y * v.y + z * v.z
		}
		
		public override function get lengthSquared():Number {
			return x * x + y * y + z * z
		}
		
		public override function distanceSquared(v:Vec):Number {
			var dx:Number = this.x - v.x
			var dy:Number = this.y - v.y
			var dz:Number = this.z - v.z
			return dx * dx + dy * dy + dz * dz
		}
		
		/** Normalizes the vector to the given length. */
		public override function normalizeSelfTo(len:Number):Vec {
			var mag:Number = Math.sqrt(x * x + y * y + z * z)
			if(mag > 0) {
				mag = len / mag
				x *= mag
				y *= mag
				z *= mag
			}			
			return this
		}
		
		public override function interpolate(target:Vec, delta:Number):Vec {
			var tx:Number = x + (target.x - x) * delta
			var ty:Number = y + (target.y - y) * delta
			var tz:Number = z + (target.z - z) * delta
			return new Vec3(tx, ty, tz)
		}
		
		public override function interpolateSelf(target:Vec, delta:Number):Vec {
			x += (target.x - x) * delta
			y += (target.y - y) * delta
			z += (target.z - z) * delta
			return this
		}
		
		public override function randomiseBetween(min:Vec, max:Vec):Vec {
			x = min.x + (max.x - min.x) * Math.random()
			y = min.y + (max.y - min.y) * Math.random()
			z = min.z + (max.z - min.z) * Math.random()
			return this
		}
		
		/** returns a copy (new instance) of the vector */
		public function clone():Vec3 {
			return new Vec3(x,y,z)
		}
		
		public function toString():String {
			return "Vec3["+ x.toFixed(2) +","+ y.toFixed(2) +","+ z.toFixed(2) +"]"			
		} 
	}
}