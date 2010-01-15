/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: June 20, 2009
 * 
 * Requires the jQuery Library to be present!
 */

// =============================================================================
// FIELDKIT MAIN CLASS
// =============================================================================
fk = {
	// private static fields
	_baseURL: null,
	_templateURL: null,
	
	// -- Inheritance Support --------------------------------------------------
	// this code was adapted from John Resig's Simple JavaScript inheritance article
	// http://ejohn.org/blog/simple-javascript-inheritance
	init: function() {
	  var initializing = false, fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
	  // The base Class implementation (does nothing)
	  this.Class = function(){};

	  // Create a new Class that inherits from this class
	  fk.Class.extend = function(prop) {
	    var _super = this.prototype;

	    // Instantiate a base class (but only create the instance,
	    // don't run the init constructor)
	    initializing = true;
	    var prototype = new this();
	    initializing = false;

	    // Copy the properties over onto the new prototype
	    for (var name in prop) {
	      // Check if we're overwriting an existing function
	      prototype[name] = typeof prop[name] == "function" && 
	        typeof _super[name] == "function" && fnTest.test(prop[name]) ?
	        (function(name, fn){
	          return function() {
	            var tmp = this._super;

	            // Add a new ._super() method that is the same method
	            // but on the super-class
	            this._super = _super[name];

	            // The method only need to be bound temporarily, so we
	            // remove it when we're done executing
	            var ret = fn.apply(this, arguments);        
	            this._super = tmp;

	            return ret;
	          };
	        })(name, prop[name]) :
	        prop[name];
	    }

	    // The dummy class constructor
	    function Class() {
	      // All construction is actually done in the init method
	      if ( !initializing && this.init )
	        this.init.apply(this, arguments);
	    }

	    // Populate our constructed prototype object
	    Class.prototype = prototype;

	    // Enforce the constructor to be what we expect
	    Class.constructor = Class;

	    // And make this class extendable
	    Class.extend = arguments.callee;

	    return Class;
	  };
	},
	
	// -- Logging --------------------------------------------------------------
	info: function() {
		if(window.console) {
			var s = ""
			for(var i=0; i<arguments.length; i++)
				s += arguments[i] +' '
				
			window.console.info(s)
		}
	},
	
	warn: function() {
		if(window.console) {
			var s = "WARNING: "
			for(var i=0; i<arguments.length; i++)
				s += arguments[i] +' '
				
			window.console.warn(s)
		}
	},
	
	error: function() {
		if(window.console) {
			var s = "WARNING: "
			for(var i=0; i<arguments.length; i++)
				s += arguments[i] +' '
				
			window.console.error(s)
		}
	},
	
	// -- Packages -------------------------------------------------------------
	include:function(package) {
		// TODO check if package is already defined
		this.info('import', package)
		this.load(this.baseURL() + package +'.js')
	},

	// -- Helpers --------------------------------------------------------------
	load: function(url) {
		this.info('load', url)		
		var suffix = url.substring(url.lastIndexOf('.') + 1)
		
		// load javascript 
		if(suffix == 'js') {
			var head = document.getElementsByTagName('head')[0]; 
			$(document.createElement('script')).attr({
				type: 'text/javascript', 
				src: url
			}).appendTo(head);
		
		// load stylesheet	
		} else if(suffix == 'css') {
			var head = document.getElementsByTagName('head')[0]; 
			$(document.createElement('link')).attr({
				type: 'text/css', 
				href: url, 
				rel: 'stylesheet',
				media: 'screen'
			}).appendTo(head)
		} else {
			this.warn('load: unknown suffix', suffix)
		}
	},

	baseURL: function() {
		if(this._baseURL == null) {
			var url = null
			var script = $("head").children("script:last")
			var src = script.attr('src')
			this._baseURL = src.substring(0, src.lastIndexOf('/')+1)
		}
		return this._baseURL
	},
	
	templateURL: function() {
		if(this._templateURL == null) {
			var url = null
			$("head").children("link").each(function(i) {
				if($(this).attr('rel') == 'stylesheet' && url == null) {
					var href = $(this).attr('href')
					url = href.substring(0, href.lastIndexOf('/')+1)
					return
				}
			})
			this._templateURL = url
		}
		return this._templateURL
	}
}

// define subpackages
fk.particle = {}

// initialize the kit
fk.init()