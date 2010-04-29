/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 13, 2010 */
package field.kit.math
{
	/**
	 * Various mathematical helper tools
	 */
	public final class MathUtil
	{
		public function MathUtil() {}
		
		public static const EPSILON:Number = Number.MIN_VALUE
			
		public static const PI:Number = Math.PI
		public static const HALF_PI:Number = PI / 2.0
		public static const TWO_PI:Number = PI * 2.0
			
		/** The value 1/PI as a float. */
		public static const INV_PI:Number = 1.0 / PI
		
		/** A value to multiply a degree value by, to convert it to radians. */
		public static const DEG_TO_RAD:Number = PI / 180.0
		
		/** A value to multiply a radian value by, to convert it to degrees. */
		public static const RAD_TO_DEG:Number = 180.0 / PI
			
		public static function randomRange(min:Number, max:Number):Number {
			return min + (max - min) * Math.random()
		}
		
		public static function flipCoin(chance:Number = 0.5):Boolean {
			return Math.random() < chance
		}
		
		public static function slerp(current:Number, target:Number, delta:Number):Number {
			return current * (1 - delta) + target * delta
		}
		
		public static function clamp(value:Number, min:Number, max:Number):Number {
			if(value < min) return min
			if(value > max) return max
			return value
		}
	}
}