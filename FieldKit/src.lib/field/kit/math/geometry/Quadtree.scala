/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 03, 2009 */
package field.kit.math.geometry

///** Companion object to class <code>Quadtree</code> */
//object Quadtree {
//  def apply(offset:Vec2, size:Float) = 
//	  new Quadtree(null, offset, Vec2(size/2f))
//  
//  def apply(size:Float) = 
//	  new Quadtree(null, Vec2(size / 2f), Vec2(size))
//  
//  def apply(size:Vec2) = 
//    new Quadtree(null, size / 2f, size)
//}


/**
 * Implements a spatial subdivision tree to work efficiently with large numbers of 2D particles.
 * 
 * @see http://code.google.com/p/toxiclibs/source/browse/trunk/toxiclibs/src.core/toxi/geom/PointQuadree.java
 */
class Quadtree(val parent:Quadtree, val offset:Vec2, val halfSize:Vec2) 
extends AABR(offset + halfSize, halfSize) {
  import scala.collection.mutable.ArrayBuffer
  
  /**
   * Alternative tree recursion limit, number of world units when cells are
   * not subdivided any further
   */
  var minSize = 4f
  
  var maxNeighbours = 100
  
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
  
  def this(parent:Quadtree, offset:Vec2, halfSize:Vec2, minSize:Float, maxNeighbours:Int) = {
	  this(parent, offset, halfSize)
	  this.minSize = minSize
	  this.maxNeighbours = maxNeighbours
  }
  
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
      if(halfSize.x <= minSize || halfSize.y <= minSize) {
        if(data == null) {
          // import kit.util.datatype.collection.ArrayBuffer
          data = new ArrayBuffer[Vec]
        }
        
        data += p
        true
      } else {
        if(children == null)
          children = new Array[Quadtree](4)
        
        val quadrant = quadrantID(p.x - offset.x, p.y - offset.y)
        
        if(children(quadrant) == null) {
          val o = Vec2(offset)
          if((quadrant & 1) != 0) o.x += halfSize.x
          if((quadrant & 2) != 0) o.y += halfSize.y
          
          children(quadrant) = createSubtree(o, halfSize * 0.5f);
          numChildren += 1
        }
        children(quadrant) insert p  
      }
    } else {
      false
    }
  }
  
  protected def createSubtree(offset:Vec2, halfSize:Vec2) =
    new Quadtree(this, offset, halfSize, minSize, maxNeighbours)
  
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
  final def apply(p:Vec):Quadtree = {
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
    if (this intersects box) {
      if(data != null) {
        data foreach { p => 
          if(box contains p)
            result += p
        }                       
      } else if(numChildren > 0) {
        for(i <- 0 until 4) {
          val child = children(i)
          if(child != null)
            child(box, result)
        }
      }
    }
    result
  }
  
  /**
   * Selects all stored points within the given sphere volume
   */
   def apply(circle:Circle, result:ArrayBuffer[Vec]):ArrayBuffer[Vec] = {
    if(!(this intersects circle)) return result
    
    if(data != null) {
      var i = 0
      while(i < data.size) {
        result += data(i)
        i += 1
        if(i == maxNeighbours)
          return result
      }
      
    } else if(numChildren > 0) {
      var i = 0
      while(i < 4) {
        val child = children(i)
        if(child != null)
          child(circle, result)
        i += 1
      }
    }
    result
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
    if(x >= halfSize.x) id += 1
    if(y >= halfSize.y) id += 2
    id
   }
  
  override def toString = 
    "Quadtree[X"+ x +" Y"+ y +" extent X"+ extent.x +" Y"+ extent.y +"]"
}
/*
 tested this simplified insert, but doesnt seem to improve performance a lot
 
  def fastInsert(p:Vec):Boolean = {
    // check if point is inside cube
    if(this contains p) {
      if(isLeafNode) {
        data += p
        true
      } else {
        val quadrant = quadrantID(p.x - offset.x, p.y - offset.y)
        children(quadrant) insert p
      }
    } else {
      false
    }
  }
  
  var isLeafNode = false
  fastInit
  def fastInit {
    import kit.math.Common._
    
    val childSize = halfSize * .5f
    if(childSize <= minSize) {
      isLeafNode = true
      data = new ArrayBuffer[Vec]
      
    } else {
      isLeafNode = false
      children = new Array[Quadtree](4)
      numChildren = 4
      children(0) = new Quadtree(this, offset, childSize)
      children(1) = new Quadtree(this, offset + (halfSize, 0), childSize)
      children(2) = new Quadtree(this, offset + (0, halfSize), childSize)
      children(3) = new Quadtree(this, offset + (halfSize, halfSize), childSize)
    }
  }
 
  def fastClear {
   	if(data != null) data.clear
    
    for(i <- 0 until numChildren) {
      val child = children(i)
      if(child != null)
        child.clear
    }
  }

*/
