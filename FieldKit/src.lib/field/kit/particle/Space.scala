/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 02, 2009 */
package field.kit.particle

import kit.math.Vec3
import kit.math.geometry.AABB

/**
 * Companion object to Space
 */
object Space {
  def apply() = new Space(1000f, 1000f, 1000f)
}

/** 
 * Represents a cubic space
 * @author Marcus Wendt
 */
class Space(val width:Float, val height:Float, val depth:Float) 
extends AABB(Vec3(width, height, depth)) {
//class Space(val width:Float, val height:Float, val depth:Float) 
//extends Octree(null, 
//               Vec3(-width/2f, -height/2f, -depth/2f),
//               Vec3(width, height, depth)) {
//  
//  import math._
//  import math.Common._
//  import math.geometry._
  
  val dimension = Vec3(width, height, depth)
  
  /*
  val dimension = Vec3()
  var octree:Octree = _

  // set initial dimensions
  set(w,h,d) 
  */
  
  // -- Constructors -----------------------------------------------------------  
  /** initialize default dimensions */
//  def this() = this(1000f, 1000f, 1000f)
  
  /*
  /** sets the new extent and updates the space dimensions */
  def set(w:Float, h:Float, d:Float) {
    dimension := (w, h, d)
    extent := dimension /= 2f
    octree = 
    updateBounds
  }
  
  // -- Getters ----------------------------------------------------------------
  def width = dimension.x
  def height = dimension.y
  def depth = dimension.z
  
  // -- Helpers ----------------------------------------------------------------
  override def toString = "Space("+ width +" "+ height +" "+ depth +")"
  */
}