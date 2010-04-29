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
	 * Base class for all vector classes
	 */
	public class Vec
	{
		public var x:Number = 0
		public var y:Number = 0
		public var z:Number = 0
		
		// -- Scalar Operations ------------------------------------------------
		public function setS(x:Number = 0, y:Number = 0, z:Number = 0):Vec {
			this.x = x; this.y = y; this.z = z;
			return this
		}
		
		public function addS(s:Number):Vec { return null }
		public function addSelfS(s:Number):Vec { return null }
		 
		public function subS(s:Number):Vec { return null }
		public function subSelfS(s:Number):Vec { return null }
		
		public function mulS(s:Number):Vec { return null }
		public function mulSelfS(s:Number):Vec { return null }
		
		public function divS(s:Number):Vec { return null }
		public function divSelfS(s:Number):Vec { return null }
			
		// -- Vector Operations ------------------------------------------------
		public function set(v:Vec):Vec {
			this.x = v.x; this.y = v.y; this.z = v.z;
			return this
		}
		
		public function add(v:Vec):Vec { return null }
		public function addSelf(v:Vec):Vec { return null }
		 
		public function sub(v:Vec):Vec { return null }
		public function subSelf(v:Vec):Vec { return null }
		
		public function mul(v:Vec):Vec { return null }
		public function mulSelf(v:Vec):Vec { return null }
		
		public function div(v:Vec):Vec { return null }
		public function divSelf(v:Vec):Vec { return null }
		
		// -- Properties -------------------------------------------------------
		public function get length():Number {
			return Math.sqrt(lengthSquared)
		}
		
		public function get lengthSquared():Number { return -1 }


		// -- Utilites ---------------------------------------------------------
		public function dot(v:Vec):Number { return -1 }
		
		public function zero():Vec {
			this.x = 0; this.y = 0; this.z = 0;
			return this
		}
		
		public function normalize():Vec {
			var l:Number = length
			if(l != 0)
				return divS(l)
			
			return null
		}
		
		public function normalizeSelf():Vec {
			var l:Number = length
			if(l != 0)
				divSelfS(l)
			
			return this
		}
		
		public function normalizeSelfTo(len:Number):Vec { return null }
		
		public function distance(v:Vec):Number {
			return Math.sqrt(distanceSquared(v))
		}
		
		public function distanceSquared(v:Vec):Number { return -1 }
		
		/** 
		 * returns the angle between the this and another vector
		 * if both vectors are already normalized, then the normalization step can be skipped 
		 */ 
		public function angleBetween(v:Vec, normalize:Boolean=true):Number {
			var theta:Number = (normalize) ? this.normalize().dot(v.normalize()) : dot(v)
			return Math.acos(theta)
		}
		
		public function interpolate(target:Vec, delta:Number):Vec  { return null }
		public function interpolateSelf(target:Vec, delta:Number):Vec { return null }
		
		public function randomiseBetween(min:Vec, max:Vec):Vec { return null }
	}
}