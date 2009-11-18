/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

import field.kit.math._

/**
 * 
 * Direct port of Karsten Schmidts PointOctree.java to Scala/ FieldKit
 * 
 * Implements a spatial subdivision tree to work efficiently with large numbers
 * of 3D particles. This octree can only be used for particle type objects and
 * does NOT support 3D mesh geometry as other forms of Octrees do.
 * 
 * @see http://code.google.com/p/toxiclibs/source/browse/trunk/toxiclibs/src.core/toxi/geom/PointOctree.java
 */
class Octree(val parent:Octree, val offset:Vec3, val halfSize:Vec3) 
extends AABB(offset + halfSize, halfSize) {
  import scala.collection.mutable.ArrayBuffer
  
  /**
  * Constructs a new Octree root node
  */
  def this(offset:Vec3, size:Float) {
    this(null, offset, Vec3(size/2f))
  }

  /**
   * Alternative tree recursion limit, number of world units when cells are
   * not subdivided any further
   */
  var minSize = 4f
  
  val size = halfSize * 2f
  
  val depth:Int = if(parent == null) 0 else parent.depth + 1
  
  protected var data:ArrayBuffer[Vec] = null
  
  /**
   * Stores the child nodes of this node
   */
  var children:Array[Octree] = null
  
  /**
   * the number of child nodes (max. 8)
   */
  var numChildren = 0
  
  /**
   * Defines wether this node automatically removes branches when a point was removed
   */
  protected val isAutoReducing = false
  
  /**
   * Adds a new point/particle to the tree structure. All points are stored
   * within leaf nodes only. The tree implementation is using lazy
   * instantiation for all intermediate tree levels.
   * @param p
   * @return true, if point has been added successfully
   */
  def insert(p:Vec):Boolean = {
    // check if point is inside cube
    if(this contains p) {
      // only add data to leaves for now
      if(halfSize.x <= minSize || halfSize.y <= minSize || halfSize.z <= minSize) {
        if(data == null)
          data = new ArrayBuffer[Vec]
        
        data += p
        true
      } else {
        if(children == null)
          children = new Array[Octree](8)
        
        val octant = octantID(p.x - offset.x, p.y - offset.y, p.z - offset.z)
        
        if(children(octant) == null) {
          val o = Vec3(offset)
          if((octant & 1) != 0) o.x += halfSize.x
          if((octant & 2) != 0) o.y += halfSize.y
          if((octant & 4) != 0) o.z += halfSize.z
          
          children(octant) = new Octree(this, o, halfSize * 0.5f);
          numChildren += 1
        }
        children(octant) insert p  
      }
    } else {
      false
    }
  }
  
  /**
   * Removes a point from the tree and (optionally) tries to release memory by 
   * reducing now empty sub-branches. 
   * @param p point to delete
   * @return true, if the point was found & removed
   */
  def remove(p:Vec):Boolean = {
    var found = false 
    val leaf = apply(p)
    if(leaf != null) {
      val sizeBefore = leaf.size
      leaf.data -= p
      if(leaf.size != sizeBefore) {
        found = true
        if(isAutoReducing && leaf.data.size == 0)
          leaf.reduceBranch
      }
    }
    found
  }

  /**
   * Tries to release memory by clearing up this branch
   */
  protected def reduceBranch {
    if(data != null && data.size == 0)
      data = null
    
    if(numChildren > 0) {
      for(i <- 0 until 8) {
        val child = children(i)
        if(child != null && child.data == null)
          children(i) = null
      }
    }
    
    if(parent != null)
      parent.reduceBranch
  }
  
  /**
   * Finds the leaf node which spatially relates to the given point
   * 
   * @param p point to check
   * @return leaf node or null if point is outside the tree dimensions
   */
  def apply(p:Vec):Octree = {
    // if not a leaf node...
    if (this contains p) {
      if(numChildren > 0) {
        val octant = octantID(p.x - offset.x, p.y - offset.y, p.z - offset.z)
        if(children(octant) != null)
          return children(octant)(p)
        
      } else if(data != null) {
        return this
      }
    }
    null
  }
  
  /**
   * Selects all stored points within the given axis-aligned bounding box.
   * 
   * @param box AABB
   * @param result the ArrayBuffer
   * @return all points with the box volume
   */
  def apply(box:AABB, result:ArrayBuffer[Vec]):ArrayBuffer[Vec] = {
    val r = if(result == null) new ArrayBuffer[Vec] else result
    
    if (this intersects box) {
      if(data != null) {
        data foreach { p => 
          if(box contains p)
            r += p
        }                       
      } else if(numChildren > 0) {
        for(i <- 0 until 8) {
          val child = children(i)
          if(child != null)
            child(box, result)
        }
      }
    }
    r
  }
  
  /**
   * Selects all stored points within the given sphere volume
   */
  def apply(sphere:Sphere, result:ArrayBuffer[Vec]):ArrayBuffer[Vec] = {
    val r = if(result == null) new ArrayBuffer[Vec] else result
    
    if (this intersects sphere) {
      if(data != null) {
        data foreach { p => 
          if(sphere contains p)
            r += p
        }                       
      } else if(numChildren > 0) {
        for(i <- 0 until 8) {
          val child = children(i)
          if(child != null)
            child(sphere, result)
        }
      }
    }
    r
  }
  
  /**
   * Alias for apply(p:Vec)
   */
  def leafForPoint(p:Vec) = apply(p)
  
  /**
   * Alias for apply(box:AABB, result:ArrayBuffer[Vec])
   */
  def pointsWithinBox(box:AABB, result:ArrayBuffer[Vec]) = apply(box,result)
  
  /**
   * Alias for apply(sphere:Sphere, result:ArrayBuffer[Vec])
   */
  def pointsWithinSphere(sphere:Sphere, result:ArrayBuffer[Vec]) = apply(sphere,result)
  
  /**
   * Clears all children and data of this node
   */
  def clear {
    // TODO consider just clearing the arrays to avoid the cost of recreating them
    numChildren = 0
    children = null
    data = null
  }
  
  /**
   * Computes the local child octant/cube index for the given point
   * @param plocal point in the node-local coordinate system
   * @return octant index
   */
  protected final def octantID(x:Float, y:Float, z:Float):Int = {
    var id = 0
    if(x >= halfSize.x) id += 1
    if(y >= halfSize.y) id += 2
    if(z >= halfSize.z) id += 4
    id
   }
  
  override def toString = 
    "Octree[X"+ x +" Y"+ y +"Z"+ z +" extent X"+ extent.x +" Y"+ extent.y +" Z"+ extent.z +"]"
}
