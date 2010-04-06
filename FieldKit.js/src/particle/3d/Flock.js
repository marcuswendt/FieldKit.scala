/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2010 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Created: June 20, 2009
 */

/**
 * 3D Flock
 */
fk.particle3d.Flock = fk.particle.Flock.extend({

	init: function() {
		this._super();
		this.emitter = new fk.particle3d.Emitter(this);
	},
	
	toString: function() { return 'Flock3D'; },
});