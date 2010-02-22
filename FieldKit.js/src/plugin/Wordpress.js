/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: June 20, 2009
 */

// =============================================================================
// WORDPRESS RELATED UTILITIES
// =============================================================================
fk.wp = {
	// -- Wordpress related ----------------------------------------------------
	baseURL: function(){
		if (this._baseURL == null) {
			var url = null
			var script = $("head").children("script:last")
			var src = script.attr('src')
			this._baseURL = src.substring(0, src.lastIndexOf('/') + 1)
		}
		return this._baseURL
	},
	
	templateURL: function(){
		if (this._templateURL == null) {
			var url = null
			$("head").children("link").each(function(i){
				if ($(this).attr('rel') == 'stylesheet' && url == null) {
					var href = $(this).attr('href')
					url = href.substring(0, href.lastIndexOf('/') + 1)
					return
				}
			})
			this._templateURL = url
		}
		return this._templateURL
	},
};