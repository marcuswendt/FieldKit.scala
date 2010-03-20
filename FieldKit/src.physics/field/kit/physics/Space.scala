/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created February 24, 2010 */
package field.kit.physics

import field.kit._
import field.kit.math.geometry._

import scala.collection.mutable.ArrayBuffer


/** 
* Represents a cubic space and also provides an interface (through subclassing) 
* for various spatial optimisation techniques (Octree, Quadtree, ...)
* 
* @author Marcus Wendt
*/
abstract class Space(val dimension:Vec3) extends AABB(dimension * 0.5f) {
	
	type T = Particle
}

/**
* A space that uses an Octree to find neighbouring particles 
*/
class OctreeSpace(dimension:Vec3) extends Space(dimension) {
		
	val tree = new Octree[Particle](Vec3(), dimension)
	
	def clear = tree.clear
	
	def insert(p:Particle) = tree insert p
	
	def apply(point:Vec, radius:Float, result:ArrayBuffer[Particle]) {
		result.clear
		tree(new Sphere(point, radius), result)
	}
	
	def apply(bounds:BoundingVolume, result:ArrayBuffer[Particle]) {
		result.clear
		tree(bounds, result)
	}
}



///**
//* A space that uses a Quadtree to find neighbouring particles 
//*/
//class QuadtreeSpace(dimension:Vec3) extends Space(dimension) {
//	
//	var tree = new Quadtree(null, (x,y), (width/2f, height/2f))
//
//	override def apply(point:Vec, radius:Float) = {
//		result.clear
//		tree(new Circle(point, radius), result)
//	}
//
//	override def insert(particle:Vec) = tree.insert(particle)
//
//	override def clear = tree.clear  
//}
