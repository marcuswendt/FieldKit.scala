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
// JQUERY RELATED UTILITIES
// =============================================================================
fk.jquery = {
	
	// -- Helpers --------------------------------------------------------------
	load: function(url) {
		fk.info('load', url);
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
			}).appendTo(head);
		} else {
			fk.warn('load: unknown suffix', suffix);
		}
	},
};