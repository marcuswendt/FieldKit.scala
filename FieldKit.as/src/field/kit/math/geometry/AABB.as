/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.math.geometry
{
	import field.kit.math.Vec;
	import field.kit.math.Vec3;
	
	/**
	 * An axis-aligned bounding box used among other things for collision detection, physics spaces etc.
	 */
	public class AABB extends Vec3
	{
		private var _min:Vec3 = new Vec3()
		private var _max:Vec3 = new Vec3()
		private var _extent:Vec3 = new Vec3()
			
		public function AABB(position:Vec3 = null, dimension:Vec3 = null) {
			if(position != null)
				set(position)
			
			if(dimension != null)
				setDimension(dimension)
		}
		
		public function setAABB(box:AABB):void {
			_min.set(box._min)
			_max.set(box._max)
			_extent.set(box._extent)
		} 
		
		public function setDimensionS(width:Number, height:Number, depth:Number):AABB {
			this.width = width
			this.height = height
			this.depth = depth
			return this
		}
		
		public function setDimension(dimension:Vec3):AABB {
			this.width = dimension.x
			this.height = dimension.y
			this.depth = dimension.z
			return this
		}
		
		public function get dimension():Vec3 {
			return Vec3(_extent.mulS(2))
		}
		
		public function get width():Number {
			return _extent.x * 2
		}
		
		public function set width(value:Number):void {
			_extent.x = value / 2;
			updateBounds()
		}
		
		public function get height():Number {
			return _extent.y * 2;
		}
		
		public function set height(value:Number):void {
			_extent.y = value / 2;
			updateBounds()
		}
		
		public function get depth():Number {
			return _extent.z * 2;
		}
		
		public function set depth(value:Number):void {
			_extent.z = value / 2;
			updateBounds()
		}

		public function get min():Vec {
			return _min;
		}

		public function set min(value:Vec):void {
			_min.set(value);
			updateDimension();
		}

		public function get max():Vec {
			return _max;
		}

		public function set max(value:Vec):void {
			_max.set(value);
			updateDimension();
		}

		protected function updateDimension():void {
			_extent.set(_max).subSelf(_min).mulSelfS(0.5)
			this.set(min).addSelf(_extent)
		}
		
		protected function updateBounds():void {
			_min.set(this).subSelf(_extent)
			_max.set(this).addSelf(_extent)
		}
		
		public override function toString():String {
			return "AABB[position="+ x.toFixed(2) +","+ y.toFixed(2) +","+ z.toFixed(2)
				+" dimension="+ width.toFixed(2) +","+ height.toFixed(2) +","+ depth.toFixed(2) +"]"			
		}
	}
}