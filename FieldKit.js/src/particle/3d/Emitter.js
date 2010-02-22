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

/**
 * 3D Emitter
 */
fk.particle3d.Emitter = fk.particle.Emitter.extend({

	init: function(flock) {
		this._super(flock);
		this.position = new fk.math.Vec3();
	},
})