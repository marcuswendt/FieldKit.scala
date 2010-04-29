/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 15, 2010 */
package field.kit.colour
{
	/**
	 * RGB Colour class
	 */
	public class Colour
	{			
		/** holds the r,g,b data */			
		protected var rgb:Vector.<uint> = new Vector.<uint>(3, true)
			
		/** holds the h,s,v data */
		protected var hsv:Vector.<uint> = new Vector.<uint>(3, true)
		
		public function Colour(_r:uint = 0, _g:uint = 0, _b:uint = 0) {
			setRGB(_r, _g, _b)
		}
		
		public function set(c:Colour):void {
			rgb[0] = c.rgb[0]
			rgb[1] = c.rgb[1]
			rgb[2] = c.rgb[2]
				
			hsv[0] = c.hsv[0]
			hsv[1] = c.hsv[1]
			hsv[2] = c.hsv[2]
		}
		
		public function setRGB(_r:uint = 0, _g:uint = 0, _b:uint = 0):void {
			if(_r < 256) {
				rgb[0] = _r
				rgb[1] = _g
				rgb[2] = _b
			} else {
				var _rgb:Array = HexToRGB(_r)
				rgb[0] = _rgb[0]
				rgb[1] = _rgb[1]
				rgb[2] = _rgb[2]
			}
			updateHSV()
		}
		
		public function setHSV(_h:uint = 0, _s:uint = 0, _v:uint = 0):void {
			hsv[0] = _h
			hsv[1] = _s
			hsv[2] = _v
			updateRGB()
		}
		
		/** Converts a RGB colour to a HSV triplet */
		protected function updateHSV():void {
			var _hsv:Array = RGBtoHSV(r, g, b)
			hsv[0] = _hsv[0]
			hsv[1] = _hsv[1]
			hsv[2] = _hsv[2]
		}
		
		/** Converts a HSV colour to a RGB triplet */
		protected function updateRGB():void {
			var _rgb:Array = HSVtoRGB(h, s, v)
			rgb[0] = _rgb[0]
			rgb[1] = _rgb[1]
			rgb[2] = _rgb[2]
		}
		
		// -- RGB Accessors ----------------------------------------------------
		public function get r():uint {
			return rgb[0]
		}
		
		public function set r(value:uint):void {
			rgb[0] = value
			updateHSV()
		}

		public function get g():uint {
			return rgb[1]
		}
		
		public function set g(value:uint):void {
			rgb[1] = value
			updateHSV()
		}
		
		public function get b():uint {
			return rgb[2]
		}
		
		public function set b(value:uint):void {
			rgb[2] = value
			updateHSV()
		}

		// -- HSV Accessors ----------------------------------------------------
		public function get h():uint {
			return hsv[0]
		}
		
		public function set h(value:uint):void {
			hsv[0] = value % 360
			updateRGB()
		}
		
		public function get s():uint {
			return hsv[1]
		}
		
		public function set s(value:uint):void {
			hsv[1] = value % 100
			updateRGB()
		}
		
		public function get v():uint {
			return hsv[2]
		}
		
		public function set v(value:uint):void {
			hsv[2] = value % 100
			updateRGB()
		}
		
		// -- Utilities --------------------------------------------------------
		public function toInt():uint {
			return (r << 16 | g << 8 | b)
				
//			return uint(r) & 0xFF << 16 |
//				   uint(g) & 0xFF << 8 |
//				   uint(b) & 0xFF
		}
		
		public function toString():String {
			return "Colour[r:"+ r +" g:"+ g +" b:"+ b +", h:"+ h +" s:"+ s +" v:" + v +"]"			
		} 
		
		// -- Static conversion methods ----------------------------------------
		public static function RGBToHex(r:uint, g:uint, b:uint):uint{
			var hex:uint = (r << 16 | g << 8 | b);
			return hex;
		}
		
		public static function HexToRGB(hex:uint):Array{
			var rgb:Array = [];
			
			var r:uint = hex >> 16 & 0xFF;
			var g:uint = hex >> 8 & 0xFF;
			var b:uint = hex & 0xFF;
			
			rgb.push(r, g, b);
			return rgb;
		}
		
		public static function RGBtoHSV(r:uint, g:uint, b:uint):Array{
			var max:uint = Math.max(r, g, b);
			var min:uint = Math.min(r, g, b);
			
			var hue:Number = 0;
			var saturation:Number = 0;
			var value:Number = 0;
			
			var hsv:Array = [];
			
			//get Hue
			if(max == min){
				hue = 0;
			}else if(max == r){
				hue = (60 * (g-b) / (max-min) + 360) % 360;
			}else if(max == g){
				hue = (60 * (b-r) / (max-min) + 120);
			}else if(max == b){
				hue = (60 * (r-g) / (max-min) + 240);
			}
			
			//get Value
			value = max;
			
			//get Saturation
			if(max == 0){
				saturation = 0;
			}else{
				saturation = (max - min) / max;
			}
			
			hsv = [Math.round(hue), Math.round(saturation * 100), Math.round(value / 255 * 100)];
			return hsv;
			
		}
		
		public static function HSVtoRGB(h:Number, s:Number, v:Number):Array{
			var r:Number = 0;
			var g:Number = 0;
			var b:Number = 0;
			var rgb:Array = [];
			
			var tempS:Number = s / 100;
			var tempV:Number = v / 100;
			
			var hi:int = Math.floor(h/60) % 6;
			var f:Number = h/60 - Math.floor(h/60);
			var p:Number = (tempV * (1 - tempS));
			var q:Number = (tempV * (1 - f * tempS));
			var t:Number = (tempV * (1 - (1 - f) * tempS));
			
			switch(hi){
				case 0: r = tempV; g = t; b = p; break;
				case 1: r = q; g = tempV; b = p; break;
				case 2: r = p; g = tempV; b = t; break;
				case 3: r = p; g = q; b = tempV; break;
				case 4: r = t; g = p; b = tempV; break;
				case 5: r = tempV; g = p; b = q; break;
			}
			
			rgb = [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
			return rgb;
		}
	}
}