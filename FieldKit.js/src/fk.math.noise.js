/*!
 * FieldKit JavaScript Library
 * http://code.google.com/p/fieldkit
 *
 * Copyright (c) 2009 Marcus Wendt
 * Licensed under the LGPL license.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Date: July 1, 2009
 */

// =============================================================================
// Simplex Noise in 2D, 3D and 4D
//
// Original code from ToxicLibs/SimplexNoise.java by Karsten Schmidt 
// http://www.toxiclibs.org
//
// Which was based based on the example code from Stefan Gustavson's paper: 
// http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
//
// =============================================================================
fk.math.noise = {	
	Simplex: function() {
		var SQRT3 = Math.sqrt(3.0)
		var SQRT5 = Math.sqrt(3.0)
	
		// Skewing and unskewing factors for 2D, 3D and 4D, some of them pre-multiplied
		var F2 = 0.5 * (SQRT3 - 1.0)
		var G2 = (3.0 - SQRT3) / 6.0
		var G22 = (3.0 - SQRT3) / 6.0
	
		var F3 = 1.0 / 3.0
		var G3 = 1.0 / 6.0
	
		var F4 = (SQRT5 - 1.0) / 4.0
		var G4 = (5.0 - SQRT5) / 20.0
		var G42 = G4 * 2.0
		var G43 = G4 * 3.0
		var G44 = G4 * 4.0 - 1.0
	
		// Arrays
		var p = [ 151, 160, 137, 91, 90, 15, 131, 13, 201,
				95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
				240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
				94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
				87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
				48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
				230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25,
				63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200,
				196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
				64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255,
				82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
				223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153,
				101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
				113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
				193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
				14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
				176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
				114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
		]
	
		var grad3 = [ 
			  [ 1, 1, 0 ], [ -1, 1, 0 ], [ 1, -1, 0 ], 
				[ -1, -1, 0 ], [ 1, 0, 1 ], [ -1, 0, 1 ],
				[ 1, 0, -1 ], [ -1, 0, -1 ], [ 0, 1, 1 ], 
				[ 0, -1, 1 ], [ 0, 1, -1 ], [ 0, -1, -1 ] 
		]
	
		var perm = new Array(p.length * 2)
		for(var i=0; i<perm.length; i++) 
			perm[i] = p[i & 0xff]
				
		var fastfloor = function(x) { return x > 0 ? x : x - 1 }
	
		// Computes dot product in 2D.
		var dot2d = function(g, x, y) { return g[0] * x + g[1] * y }
	
		this.sample2d = function(x,y) {
			var n0 = 0, n1 = 0, n2 = 0 // Noise contributions from the three corners
		
			// fk.info('-------------------------------------------------')
		
			// Skew the input space to determine which simplex cell we're in
			var s = (x + y) * F2 // Hairy factor for 2D
			var i = fastfloor(x + s)
			var j = fastfloor(y + s)
	 		var t = (i + j) * G2
	 		var x0 = x - (i - t) // The x,y distances from the cell origin
	 		var y0 = y - (j - t)
		
			// For the 2D case, the simplex shape is an equilateral triangle.
			// Determine which simplex we are in.
			var i1, j1 // Offsets for second (middle) corner of simplex in (i,j)
			if(x0 > y0) {
				i1 = 1
				j1 = 0
			} // lower triangle, XY order: (0,0)->(1,0)->(1,1)
			else {
				i1 = 0
				j1 = 1
			} // upper triangle, YX order: (0,0)->(0,1)->(1,1)

			// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
			// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
			// c = (3-sqrt(3))/6
			var x1 = x0 - i1 + G2 // Offsets for middle corner in (x,y) unskewed
			var y1 = y0 - j1 + G2
			var x2 = x0 + G22 // Offsets for last corner in (x,y) unskewed
			var y2 = y0 + G22
		
			// Work out the hashed gradient indices of the three simplex corners
			var ii = i & 0xff
			var jj = j & 0xff

			// Calculate the contribution from the three corners
			var t0 = 0.5 - x0 * x0 - y0 * y0
			if (t0 > 0) {
				t0 *= t0
				var gi0 = perm[ii + perm[jj]] % 12
				n0 = t0 * t0 * dot2d(grad3[gi0], x0, y0) // (x,y) of grad3 used for 2D gradient
			}
			var t1 = 0.5 - x1 * x1 - y1 * y1
			if (t1 > 0) {
				t1 *= t1
				var gi1 = perm[ii + i1 + perm[jj + j1]] % 12
				n1 = t1 * t1 * dot2d(grad3[gi1], x1, y1)
			}
			var t2 = 0.5 - x2 * x2 - y2 * y2
			if (t2 > 0) {
				t2 *= t2
				var gi2 = perm[ii + 1 + perm[jj + 1]] % 12
				n2 = t2 * t2 * dot2d(grad3[gi2], x2, y2)
			}
		
			// Add contributions from each corner to get the final noise value.
			// The result is scaled to return values in the interval [-1,1].
			return 70.0 * (n0 + n1 + n2)
		}
	}
}

fk.math.noise2d = function(x,y) {
	if(!this.simplex) this.simplex = new fk.math.noise.Simplex()
	return this.simplex.sample2d(x,y) 
}