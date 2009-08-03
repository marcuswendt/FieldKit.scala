/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

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
class Octree(val parent:Octree, val offset:Vec3, val size:Float) 
extends AABB(offset + (size,size,size), new Vec3(size,size,size)) {
  import kit.util.datatype.collection.ArrayBuffer
    
  /**
   * Alternative tree recursion limit, number of world units when cells are
   * not subdivided any further
   */
  var minSize = 4f
  
  val depth:Int = if(parent == null) 0 else parent.depth + 1
  
  protected var data:ArrayBuffer[Vec3] = null
  
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
   * Constructs a new PointOctree
   */
  def this(offset:Vec3, size:Float) {
    this(null, offset, size/2f)
  }
  
  /**
   * Adds a new point/particle to the tree structure. All points are stored
   * within leaf nodes only. The tree implementation is using lazy
   * instantiation for all intermediate tree levels.
   * @param p
   * @return true, if point has been added successfully
   */
  def ::(p:Vec3):Boolean = {
    // check if point is inside cube
    if(this contains p) {
      // only add data to leaves for now
      if (size <= minSize) {
        if(data == null)
          data = new ArrayBuffer[Vec3]
        
        data += p
        true
      } else {
        val plocal = p - offset
        if(children == null)
          children = new Array[Octree](8)
        
        val octant = getOctantID(plocal)
        if(children(octant) == null) {
          val o = new Vec3(offset)
          if((octant & 1) != 0) o.x = size
          if((octant & 2) != 0) o.y = size
          if((octant & 4) != 0) o.z = size
          
          children(octant) = new Octree(this, o, size * 0.5f);
          numChildren += 1
        }
        p :: children(octant)
      }
    } else {
      false
    }
  }

  /**
   * Finds the leaf node which spatially relates to the given point
   * 
   * @param p point to check
   * @return leaf node or null if point is outside the tree dimensions
   */
  def apply(p:Vec3):Octree = {
    // if not a leaf node...
    if (this contains p) {
      if(numChildren > 0) {
        val octant = getOctantID(p - offset)
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
  def apply(box:AABB, result:ArrayBuffer[Vec3]):ArrayBuffer[Vec3] = {
    val r = if(result == null) { 
      new ArrayBuffer[Vec3] 
    } else {
      result.clear
      result
    }
    
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
   * Same as apply(p:Vec3)
   */
  def getLeafForPoint(p:Vec3) = apply(p)
  
  /**
   * Same as apply(box:AABB, result:ArrayBuffer[Vec3])
   */
  def getPointsWithinBox(box:AABB, result:ArrayBuffer[Vec3]) = apply(box,result)
  
  /**
   * Clears all children and data of this node
   */
  def clear {
    numChildren = 0
    children = null
    data = null
  }
  
  /**
   * Computes the local child octant/cube index for the given point
   * @param plocal point in the node-local coordinate system
   * @return octant index
   */
  protected final def getOctantID(plocal:Vec3):Int = {
    var id = 0
    if(plocal.x >= size) id += 1
    if(plocal.y >= size) id += 2
    if(plocal.z >= size) id += 4
    id
   }
}
