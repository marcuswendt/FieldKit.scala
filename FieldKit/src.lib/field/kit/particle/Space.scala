/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle

import math._
import math.geometry.AABB

import scala.collection.mutable.ArrayBuffer

/** 
 * Represents a cubic space and also provides an interface (through subclassing) 
 * for various spatial optimisation techniques (Octree, Quadtree, ...)
 * 
 * @author Marcus Wendt
 */
class Space(val width:Float, val height:Float, val depth:Float) 
extends AABB(width, height, depth) {
  
  val dimension = Vec3(width, height, depth)
  
  /** inserts another particle into this space */
  def insert(particle:Vec) {}
  
  /** @return a list of particles at the given position */
  def apply(point:Vec, radius:Float, result:ArrayBuffer[Vec]) = {
    result.clear
    result
  }
  
  /** removes all registered particles from this space */
  def clear {}
  
  // helpers
  override def toString = "Space("+ width +" "+ height +" "+ depth +")"
}


/**
 * A space that uses a Quadtree to find neighbouring particles 
 */
class QuadtreeSpace(width:Float, height:Float, depth:Float) 
extends Space(width, height, depth) {
  import math.geometry._
  import math.Common._
  
  val tree = new Quadtree(null, (-width/2f, -height/2f), (width, height))
  
  override def apply(point:Vec, radius:Float, result:ArrayBuffer[Vec]) = 
    tree(new Circle(point, radius), result)

  override def insert(particle:Vec) = tree.insert(particle)

  override def clear = tree.clear  
}


/**
 * A space that uses an Octree to find neighbouring particles 
 */
class OctreeSpace(width:Float, height:Float, depth:Float) 
extends Space(width, height, depth) {
  import math.geometry._
  import math.Common._
  
  val tree = new Octree(null, 
                        (-width/2f, -height/2f, -depth/2f), 
                        (width, height, depth))
  
  override def apply(point:Vec, radius:Float, result:ArrayBuffer[Vec]) = 
    tree(new Sphere(point, radius), result)

  override def insert(particle:Vec) = tree.insert(particle)
  
  override def clear = tree.clear
}