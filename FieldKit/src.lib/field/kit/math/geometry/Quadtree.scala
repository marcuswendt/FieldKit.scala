/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math.geometry


/**
 * Implements a spatial subdivision tree to work efficiently with large numbers of 2D particles.
 * 
 * @see http://code.google.com/p/toxiclibs/source/browse/trunk/toxiclibs/src.core/toxi/geom/PointQuadree.java
 */
class Quadtree(val parent:Quadtree, val offset:Vec2, val halfSize:Float) 
extends AABR(offset + halfSize, halfSize) {
  import kit.util.datatype.collection.ArrayBuffer

  /**
  * Constructs a new Quadtree root node
  */
  def this(offset:Vec2, size:Float) {
    this(null, offset, size/2f)
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
  var children:Array[Quadtree] = null
  
  /**
   * the number of child nodes (max. 4)
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
      if(halfSize <= minSize || halfSize <= minSize) {
        if(data == null)
          data = new ArrayBuffer[Vec]
        
        data += p
        true
      } else {
        if(children == null)
          children = new Array[Quadtree](4)
        
        val quadrant = quadrantID(p.x - offset.x, p.y - offset.y)
        
        if(children(quadrant) == null) {
          val o = Vec2(offset)
          if((quadrant & 1) != 0) o.x += halfSize
          if((quadrant & 2) != 0) o.y += halfSize
          
          children(quadrant) = new Quadtree(this, o, halfSize * 0.5f);
          numChildren += 1
        }
        children(quadrant) insert p  
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
  def remove(p:Vec2):Boolean = {
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
      for(i <- 0 until 4) {
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
  def apply(p:Vec):Quadtree = {
    // if not a leaf node...
    if (this contains p) {
      if(numChildren > 0) {
        val quadrant = quadrantID(p.x - offset.x, p.y - offset.y)
        if(children(quadrant) != null)
          return children(quadrant)(p)
        
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
  def apply(box:AABR, result:ArrayBuffer[Vec]):ArrayBuffer[Vec] = {
    val r = if(result == null) new ArrayBuffer[Vec] else result
    
    if (this intersects box) {
      if(data != null) {
        data foreach { p => 
          if(box contains p)
            r += p
        }                       
      } else if(numChildren > 0) {
        for(i <- 0 until 4) {
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
  def apply(circle:Circle, result:ArrayBuffer[Vec]):ArrayBuffer[Vec] = {
    val r = if(result == null) new ArrayBuffer[Vec] else result
    
    if (this intersects circle) {
      if(data != null) {
        data foreach { p => 
          if(circle contains p)
            r += p
        }                       
      } else if(numChildren > 0) {
        for(i <- 0 until 4) {
          val child = children(i)
          if(child != null)
            child(circle, result)
        }
      }
    }
    r
  }
  
  /**
   * Alias for apply(p:Vec2)
   */
  def leafForPoint(p:Vec2) = apply(p)
  
  /**
   * Alias for apply(rect:AABR, result:ArrayBuffer[Vec])
   */
  def pointsWithinBox(rect:AABR, result:ArrayBuffer[Vec]) = apply(rect, result)
  
  /**
   * Alias for apply(circle:Circle, result:ArrayBuffer[Vec])
   */
  def pointsWithinCircle(circle:Circle, result:ArrayBuffer[Vec]) = apply(circle, result)
  
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
   * Computes the local child quadrant/cube index for the given point
   * @param plocal point in the node-local coordinate system
   * @return quadrant index
   */
  protected final def quadrantID(x:Float, y:Float):Int = {
    var id = 0
    if(x >= halfSize) id += 1
    if(y >= halfSize) id += 2
    id
   }
  
  override def toString = 
    "Quadtree[X"+ x +" Y"+ y +" extent X"+ extent.x +" Y"+ extent.y +"]"
}
