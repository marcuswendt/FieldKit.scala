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
	filename: 'fieldkit.js',
	
	// private fields
	_baseURL: null,
	_templateURL: null,
		
	// -- Logging ----------------------------------------------------------------
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
	
	// -- Packages ---------------------------------------------------------------
	include: function(package) {
		// TODO check if package is already defined
		this.info('import', package)
		this.load(this.baseURL() + package +'.js')
	},

	// -- Helpers ----------------------------------------------------------------
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
			var filename = this.filename
			var url = null
			$("head").children("script").each(function(i) {
				var src = $(this).attr('src')
				if(src.substring(src.length - filename.length) == filename 
					 &&	url == null) {
					url = src.substring(0, src.lastIndexOf('/')+1)
					return
				}
			})
			this._baseURL = url
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
	},
	
	// -- Initialisation ---------------------------------------------------------
	init: function() {
		this.info('init')
		// this.include('fk.math')
		// this.include('fk.particle')
	}
}

fk.init()