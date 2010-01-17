/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: January 17, 2010
 * 
 */

/**
 * Simple p5 like framework for quick <canvas> based animation sketches
 */
fk.Sketch = fk.Class.extend({
	width: 640, height: 480,
	framerate: 60,
	
	container: null, canvas: null, g: null,
	
	// private
	interval: null,
	
	// constructor
	init: function(container) {
		this.container = document.getElementById(container);
		
		this.initCanvas();
		this.setup();
		this.start();
	},
	
	/** Create <canvas> and drawing context **/
	initCanvas: function() {
		this.canvas = document.createElement("canvas")	
		this.canvas.width = this.width
		this.canvas.height = this.height
		this.container.appendChild(this.canvas)
	 	this.g = this.canvas.getContext('2d')
	},
	
	// override me
	setup: function() {
		this.size(640, 480);
	},
	
	size: function(w, h) {
		this.width = w;
		this.height = h;
	},
	
	start: function() {
		var test = this
		this.interval = setInterval(
			function() { test.draw() }, 
			1000/this.framerate);
	},
	
	stop: function() {
		clearInterval(this.interval);
	},
	
	// override me
	draw: function() {
	},
	
	// -- Drawing Helpers ------------------------------------------------------
	clear: function() {
		this.g.clearRect(0,0,this.width,this.height);
	},
	
	fill: function(colour) {
		this.isFillEnabled = true;
		this.g.fillStyle = colour;
	},
	
	stroke: function(colour) {
		this.isStrokeEnabled = true;
		this.g.strokeStyle = colour;
	},
	
	noFill: function() {
		this.isFillEnabled = false;
	},
	
	noStroke: function() {
		this.isStrokeEnabled = false;
	},
	
	rect: function(x, y, w, h) {
		if(this.isFillEnabled)
			this.g.fillRect(x,y,w,h);
			
		if(this.isStrokeEnabled)
			this.g.strokeRect(x,y,w,h);
	}
});