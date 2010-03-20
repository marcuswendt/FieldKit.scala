/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle

import field.kit._
import field.kit.math.geometry._
import field.kit.math.geometry.AABB

import scala.collection.mutable.ArrayBuffer

object Space {
  val DEFAULT = new Vec3(1000f, 1000f, 1000f)
}

/** 
 * Represents a cubic space and also provides an interface (through subclassing) 
 * for various spatial optimisation techniques (Octree, Quadtree, ...)
 * 
 * @author Marcus Wendt
 */
class Space(width:Float, height:Float, depth:Float) 
extends AABB(Vec3(0, 0, -depth/2f), Vec3(width, height, depth)) {

	type T = Particle
	
  val dimension = Vec3(width, height, depth)
  
  /** inserts another particle into this space */
  def insert(p:Particle) {}
  
  /** @return a list of particles at the given position */
  def apply(point:Vec3, radius:Float):ArrayBuffer[_ <: Vec] = null
  
  /** removes all registered particles from this space */
  def clear {}
  
  /** converts a positive normalized float [0, 1] to an absolute length in the particle space */
  def toAbsolute(normalized:Float) = width * normalized
  
  // helpers
  override def toString = "Space("+ width +" "+ height +" "+ depth +")"
}


/**
 * A space that uses a Quadtree to find neighbouring particles 
 */
class QuadtreeSpace(width:Float, height:Float, depth:Float) 
extends Space(width, height, depth) {
  var tree = new Quadtree(null, (x,y), (width/2f, height/2f))
  
  protected val result = new ArrayBuffer[Vec]
  
  override def apply(point:Vec3, radius:Float) = {
    result.clear
    tree(new Circle(point, radius), result)
  }

  override def insert(p:Particle) = tree.insert(p)

  override def clear = tree.clear  
}


/**
 * A space that uses an Octree to find neighbouring particles 
 */
class OctreeSpace(width:Float, height:Float, depth:Float) 
extends Space(width, height, depth) {
	
  val tree = new Octree[T](this, (width/2f, height/2f, depth/2f))
  
  protected val result = new ArrayBuffer[T]
  
  override def apply(point:Vec3, radius:Float) = {
    result.clear
    tree(new Sphere(point, radius), result)
  }

  override def insert(p:Particle) = tree.insert(p)
  
  override def clear = tree.clear
}