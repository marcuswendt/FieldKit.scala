/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit.util
{
	import flash.utils.getTimer;

	/** 
	 * A simple helper to set up alarms and measure time between frames
	 * @author Marcus Wendt
	 */
	public class Timer
	{
		public var elapsed:Number
		public var interval:Number
		public var prevUpdate:Number
		public var start:Number
		
		public function Timer() {
			reset()
		}
		
		public function update():Number {
			var dt:Number = (now - prevUpdate)
			elapsed += dt
			prevUpdate = now
			return dt
		}
		
		public function alarm():Boolean {
			update()
			return elapsed > interval
		}
		
		public function reset():void {
			elapsed = 0
			prevUpdate = now
			start = now
		}
		
		public function get now():Number {
			return getTimer()
		}
		
		public function get sinceStart():Number {
			return now - start
		}
	}
}