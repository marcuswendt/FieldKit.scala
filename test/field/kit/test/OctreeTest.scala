/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.test

import field.kit._
import field.kit.p5._
import field.kit.math.geometry._

import scala.collection.mutable.ArrayBuffer

/**
* Test for the Octree geometry class 
* 
* This is a direct port of Karsten Schmidts OctreeDemo
* @see http://www.toxiclibs.org
*/
object OctreeTest extends Sketch {
	import processing.core.PConstants._


	class VisibleOctree(offset:Vec3, size:Vec3) extends Octree[Vec3](offset, size) {
		
		def draw = drawNode(this)

		def drawNode(n:Octree[Vec3]) {
			if(n.numChildren > 0) {
				noFill
				stroke(n.depth, 20)
				pushMatrix
				translate(n.x, n.y, n.z)
				box(n.extent.x * 2f, n.extent.y * 2f, n.extent.z * 2f)
				popMatrix

				for(i <- 0 until 8) {
					val child = n.children(i)
						if(child != null)
							drawNode(child)
				}
			}
		}
	}

	// -- Fields -----------------------------------------------------------------
	// sphere clip radius
	val RADIUS = 20f

	// number of particles to add at once
	val NUM = 100

	// octree dimensions
	val DIM = 100f
	val DIM2 = DIM/2f

	// setup empty octree so that it's centered around the world origin
	//val octree = new VisibleOctree(new Vec3(-DIM2,-DIM2,-DIM2), Vec3(DIM))
	//val octree = new VisibleOctree(Vec3(-DIM2), Vec3(DIM2))
	val octree = new VisibleOctree(Vec3(-DIM2), Vec3(DIM))
	
	// add an initial particle at the origin
	octree insert new Vec3    

	// start with one particle
	var numParticles = 1

	// show octree debug info
	var showOctree = true

	// use clip sphere or axis aligned bounding box
	var useSphere = false

	val pointer = new Vec3

	// view rotation
	var xrot = THIRD_PI
	var zrot = 0.1f

	var points = new ArrayBuffer[Vec3]

	override def setup {
		size(1024, 768, OPENGL)
	}

	override def draw {
		// -- update -----------------------------------------------------------
		// rotate view on mouse drag
		if (mousePressed) {
			xrot += (mouseY*0.01f-xrot)*0.1f
			zrot += (mouseX*0.01f-zrot)*0.1f

		// or move cursor
		} else {
			pointer.x = -(width*0.5f-mouseX)/(width/2)*DIM2
			pointer.y = -(height*0.5f-mouseY)/(height/2)*DIM2
		}

		// -- render -----------------------------------------------------------
		beginRecord
		
		background(255)

		pushMatrix
		lights
		translate(width/2,height/2,0)
		rotateX(xrot)
		rotateZ(zrot)
		scale(4)

		// show debug view of tree
		if (showOctree) octree.draw

		// show crosshair 3D cursor
		drawCursor

		// show selected points
		drawPoints

		// show clipping sphere
		drawSphere

		//text("total: "+numParticles,10,30);
		//text("clipped: "+numClipped+" (time: "+dt+"ms)",10,50);
		popMatrix
		
		endRecord
	}

	def drawCursor {
		stroke(255,0,0)
		noFill
		beginShape(LINES)
		vertex(pointer.x,-DIM2,0)
		vertex(pointer.x,DIM2,0)
		vertex(-DIM2,pointer.y,0)
		vertex(DIM2,pointer.y,0)
		endShape
		noStroke
	}

	def drawPoints {
		points.clear

		if(useSphere)
			octree(new Sphere(pointer, RADIUS), points)
		else
			octree(new AABB(pointer, RADIUS), points)

		points foreach { p =>
			pushMatrix
			translate(p.x,p.y,p.z)
			fill(abs(p.x)*8, abs(p.y)*8, abs(p.z)*8)
			box(2)
			popMatrix
		}
	}

	def drawSphere {
		fill(0,30)
		pushMatrix
		translate(pointer.x,pointer.y,0)
		sphere(RADIUS)
		popMatrix
	}

	override def keyPressed {
		super.keyPressed()
		
		key match {
			case ' ' =>
			// add NUM new particles within a sphere of radius DIM2
			val insertNum = random(NUM).asInstanceOf[Int]
			for(i <- 0 until insertNum)
				octree insert (Vec3.random() *= random(DIM2))

			numParticles += insertNum

			info("added", insertNum, "particles => total:", numParticles)
			case 't' =>
			var v = new Vec3(-75f, -25f, -50f)
			octree insert v
			numParticles += 1

			case 'o' => showOctree = !showOctree
			case 's' => useSphere = !useSphere
			case _ =>
		}
	}
}
