/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 12, 2010 */
package field.kit
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.KeyboardEvent;

	[SWF(width="1280", height="760", scale="noscale", backgroundColor="#191919", frameRate="60")]
	
	/**
	 * Base class for all test sketches
	 */
	public class Sketch extends Sprite
	{
		public function Sketch() {
			super();
			addEventListener(Event.ENTER_FRAME, init)
		}
		
		private function init(event:Event):void {
			trace("init")
			removeEventListener(Event.ENTER_FRAME, init)
			setup()
			
			stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDown)
			addEventListener(Event.ENTER_FRAME, update)			
		}

		protected function setup():void {
			trace("setup")
		}
		
		private function update(event:Event):void {
			draw()	
		}
		
		protected function draw():void {
//			trace("draw")
		}
		
		// -- Event Handling ---------------------------------------------------
		public static var KEY_SPACE:int = 32
		public static var KEY_D:int = 68
			
		public static var KEY_0:int = 48
		public static var KEY_1:int = 49
		public static var KEY_2:int = 50
		public static var KEY_3:int = 51
		public static var KEY_4:int = 52
		public static var KEY_5:int = 53
		public static var KEY_6:int = 54
		public static var KEY_7:int = 55
		public static var KEY_8:int = 56
		public static var KEY_9:int = 57
		
		protected var keyCode:int
		
		private function keyDown(e:KeyboardEvent):void {
//			trace("keyDown", e.keyCode)
			keyCode = e.keyCode
			keyPressed()
		}
		
		protected function keyPressed():void {}
	}
}