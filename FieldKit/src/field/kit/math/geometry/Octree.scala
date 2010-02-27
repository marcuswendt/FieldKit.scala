/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created August 03, 2009 */
package field.kit.math.geometry

import field.kit.math._
import scala.collection.mutable.ArrayBuffer
import scala.annotation._ 

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
class Octree[T <: Vec3](parent:Octree[T], val offset:Vec3, halfSize:Vec3)
extends AABB(offset + halfSize, halfSize) {
	
	if(parent != null)
		this.minSize = parent.minSize 
		
	/**
	 * Constructs a new Octree root node
	 */
	def this(offset:Vec3, size:Vec3) = this(null, offset, size*0.5f)
		
	// -------------------------------------------------------------------------

	/**
	* Alternative tree recursion limit, number of world units when cells are
	* not subdivided any further
	*/
	var minSize = 10f

	val depth:Int = if(parent == null) 0 else parent.depth + 1

	protected var data:ArrayBuffer[T] = null

	/**
	* Stores the child nodes of this node
	*/
	var children:Array[Octree[T]] = null

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
	def insert(p:T):Boolean = {
		// tail-recursion optimised insert
		@tailrec def insertElement(n:Octree[T], p:T):Boolean = {
			if( !(n contains p) ) return false
				
			if(n.extent.x <= minSize || n.extent.y <= minSize || n.extent.z <= minSize) {
				if(n.data == null)
					n.data = new ArrayBuffer[T]
			
				n.data += p
				true
				
			} else {
				if(n.children == null)
					n.children = new Array[Octree[T]](8)
		
		        val octant = n.octantID(p.x - n.offset.x, p.y - n.offset.y, p.z - n.offset.z)
		
				if(n.children(octant) == null) {
					val o = Vec3(n.offset)
					if((octant & 1) != 0) o.x += n.extent.x
					if((octant & 2) != 0) o.y += n.extent.y
					if((octant & 4) != 0) o.z += n.extent.z
			
					n.children(octant) = new Octree[T](this, o, n.extent * 0.5f)
					n.numChildren += 1
				}
				
				insertElement(n.children(octant), p)
			}
		}
		insertElement(this, p)
		
		/*
		// check if point is inside cube
		if(!(this contains p)) return false
		
		// only add data to leaves for now
		if(extent.x <= minSize || extent.y <= minSize || extent.z <= minSize) {
			if(data == null)
				data = new ArrayBuffer[T]
		
			data += p
			return true
			
		} else {
			if(children == null)
				children = new Array[Octree[T]](8)
	
	        val octant = octantID(p.x - offset.x, p.y - offset.y, p.z - offset.z)
	
			if(children(octant) == null) {
				val o = Vec3(offset)
				if((octant & 1) != 0) o.x += extent.x
				if((octant & 2) != 0) o.y += extent.y
				if((octant & 4) != 0) o.z += extent.z
		
				children(octant) = new Octree[T](this, o, extent * 0.5f)
				numChildren += 1
			}
			children(octant) insert p  
		}
		*/
	}

	/**
	* Removes a point from the tree and (optionally) tries to release memory by 
	* reducing now empty sub-branches. 
	* @param p point to delete
	* @return true, if the point was found & removed
	*/
	def remove(p:T):Boolean = {
		var found = false 
		val leaf = findLeaf(p)
		if(leaf != null) {
			val sizeBefore = leaf.data.size
			leaf.data -= p
			if(leaf.data.size != sizeBefore) {
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
	* Selects all stored points within the given axis-aligned bounding box.
	* 
	* @param box AABB
	* @param result the ArrayBuffer
	* @return all points with the box volume
	*/
	def find(bounds:BoundingVolume, result:ArrayBuffer[T]):ArrayBuffer[T] = {
		
		// find using tail-recursion optimisation
		val r = if(result == null) new ArrayBuffer[T] else result
		
		@tailrec def findPoints(n:Octree[T]) {
			if(n == null) return
			
			if(n intersects bounds) {
				if(n.data != null) {
					var i = 0
					while(i < n.data.length) {
						val p = n.data(i)
						if(bounds contains p)
							r += p
						i += 1
					}              
					
				} else if(n.numChildren > 0) {
					findPoints(n.children(0))
					findPoints(n.children(1))
					findPoints(n.children(2))
					findPoints(n.children(3))
					findPoints(n.children(4))
					findPoints(n.children(5))
					findPoints(n.children(6))
					findPoints(n.children(7))
				}
			}
		}
		findPoints(this)
		r
		
		/*
		val r = if(result == null) new ArrayBuffer[T] else result

		if (this intersects box) {
			if(data != null) {
				var i = 0
				while(i < data.length) {
					val p = data(i)
					if(box contains p)
						r += p
					i += 1
				}              
			} else if(numChildren > 0) {
				var i = 0
				while(i < 8) {
					val child = children(i)
					if(child != null)
						child(box, result)
					i += 1
				}
			}
		}
		r
		*/
	}

	/**
	 * Alias for find
	 */
	def apply(bounds:BoundingVolume, result:ArrayBuffer[T]) = find(bounds, result)
	
//	/**
//	* Selects all stored points within the given sphere volume
//	*/
//	def apply(sphere:Sphere, result:ArrayBuffer[T]):ArrayBuffer[T] = {
//		val r = if(result == null) new ArrayBuffer[T] else result
//
//		if (this intersects sphere) {
//			if(data != null) {
//				var i = 0
//				while(i < data.length) {
//					val p = data(i)
//					if(sphere contains p)
//						r += p
//					i += 1
//				}                       
//			} else if(numChildren > 0) {
//				var i = 0
//				while(i < 8) {
//					val child = children(i)
//					if(child != null)
//						child(sphere, result)
//					i += 1
//				}
//			}
//		}
//		r
//	}

	/**
	* Finds the leaf node which spatially relates to the given point
	* 
	* @param p point to check
	* @return leaf node or null if point is outside the tree dimensions
	*/
	def findLeaf(p:T):Octree[T] = {
		// if not a leaf node...
		if (this contains p) {
			if(numChildren > 0) {
				val octant = octantID(p.x - x, p.y - y, p.z - z)
				if(children(octant) != null)
				return children(octant).findLeaf(p)
	
			} else if(data != null) {
				return this
			}
		}
		null
	}

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
		if(x >= extent.x) id += 1
		if(y >= extent.y) id += 2
		if(z >= extent.z) id += 4
		id
	}

	override def toString = 
		"Octree[X"+ x +" Y"+ y +"Z"+ z +" extent X"+ extent.x +" Y"+ extent.y +" Z"+ extent.z +"]"
}
