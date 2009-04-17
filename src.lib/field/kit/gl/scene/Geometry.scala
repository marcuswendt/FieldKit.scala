/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.structure.graph.Node

/** base class for all geometric objects in the scene-graph */
abstract class Geometry(name:String) extends Spatial(name) {
  import field.kit._
  import field.kit.math._
  import field.kit.gl.render.RenderState
  import field.kit.util.BufferUtil
  
  import java.nio.FloatBuffer
  import scala.collection.mutable.ArrayBuffer
  
  var colour = new Colour(1,1,1)
  
  // TODO consider switching to a datastructure with a predictable order e.g. ArrayList
  var states = new ArrayBuffer[RenderState]
  
  var vertices:FloatBuffer = _
  var coords:FloatBuffer = _
  var colours:FloatBuffer = _
  
  /** the maximum number of vertices this geometry object can hold */
  var capacity = 0
  
  /** the number of actual vertices in the buffer */
  var vertexCount = 0

  // Buffer Management
  def allocate(capacity:Int) {
    this.capacity = capacity
    vertices = allocateVertices
    coords = allocateCoords
    colours = allocateColours
    solidColour(colour)
  }
  
  protected def allocateVertices = BufferUtil.vec3(capacity)
  protected def allocateCoords = BufferUtil.vec2(capacity)
  protected def allocateColours = BufferUtil.colour(capacity)
  
  def clear {
    vertexCount = 0
    vertices.clear
    coords.clear
    colours.clear
  }
  
  // Render States
  def enableStates = states foreach(_.enable(this))
  
  def disableStates = states foreach(_.disable(this))
  
  // Colours
  def solidColour(c:Colour) {
    colour.set(c)
    if(colours!=null) {
      colours.clear
      for(i <- 0 until colours.capacity/4) {
        colours.put(c.r)
        colours.put(c.g)
        colours.put(c.b)
        colours.put(c.a)
      }
      colours.rewind
    }
  }
  
  def randomizeColours {
    colours.clear
    for(i <- 0 until colours.capacity/4) {
      colours.put(Random())
      colours.put(Random())
      colours.put(Random())
      colours.put(1f)
    }
    colours.rewind
  }
}
