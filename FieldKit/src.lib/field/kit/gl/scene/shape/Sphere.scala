/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 07, 2009 */
package field.kit.gl.scene.shape

object Sphere {
  object TextureMode extends Enumeration {
    val LINEAR = Value
    val PROJECTED = Value
    val POLAR = Value
  }
}

/**
 * Sphere represents a 3D object with all points equi-distance from a center point.
 * 
 * Based on com.ardor3d.scenegraph.shape.Sphere from the Ardor3D engine
 * @see http://ardor3d.com
 */
class Sphere(name:String) extends Mesh(name) {
  import math._
    
  var radius = 0f
  var center = new Vec3
  var zSamples = 0
  var radialSamples = 0
  var textureMode = Sphere.TextureMode.LINEAR
  
  protected var viewInside = false
  
  def this(name:String, radius:Float, samples:Int) {
    this(name)
    init(radius, samples, samples)
  }
    
  def init(radius:Float, zSamples:Int, radialSamples:Int) {
    this.radius = radius
    this.zSamples = zSamples
    this.radialSamples = radialSamples
    
    initGeometry
    initIndices
  }
  
  // TODO currently this is completely missing normal and texture coords!
  /**
   * init vertices, texture coords and normals
   */
  protected def initGeometry {
    import math.FMath._
    import util.Buffer
    
    vertexCount = (zSamples - 2) * (radialSamples + 1) + 2
    vertices = Buffer.vec3(vertexCount)
    normals = Buffer.vec3(vertexCount)
    coords = Buffer.vec2(vertexCount)
    
    // generate geometry
    val fInvRS = 1.0f / radialSamples
    val zFactor = 2.0f / (zSamples - 1f)
    
    // Generate points on the unit circle to be used in computing the mesh
    // points on a sphere slice.
    val lutSin = new Array[Float](radialSamples + 1)
    val lutCos = new Array[Float](radialSamples + 1)
    for(iR <- 0 until radialSamples) {
      val angle = TWO_PI * fInvRS * iR
      lutCos(iR) = cos(angle)
      lutSin(iR) = sin(angle)
    }
    lutSin(radialSamples) = lutSin(0)
    lutCos(radialSamples) = lutCos(0)
    
    // generate the sphere itself
    var i=0
    val tempA = new Vec3
    val tempB = new Vec3
    val tempC = new Vec3
    
    for(iZ <- 1 until zSamples-1) {
      val aFraction = HALF_PI * (-1.0f + zFactor * iZ) // in (-pi/2, pi/2)
      val zFraction = sin(aFraction) // in (-1,1)
      val z = radius * zFraction

      // compute center of slice
      tempB := center
      val sliceCenter = tempB
      sliceCenter.z += z
      
      // compute radius of slice
      val sliceRadius = sqrt(abs(radius * radius - z * z))
      
      // compute slice vertices with duplication at end point
      var normal:Vec3 = null
      var iSave = i

      for(iR <- 0 until radialSamples) {
        val radialFraction = iR * fInvRS
        tempA := (lutCos(iR), lutSin(iR), 0f) *= sliceRadius

        // vertex
        vertices put (sliceCenter.x + tempA.x)
        vertices put (sliceCenter.y + tempA.y)
        vertices put (sliceCenter.z + tempA.z)
        
        // normal
        tempA := (vertices, i)
        normal = tempA -= center
        normal.normalize
        
        if(!viewInside) {
          normals put normal.x
          normals put normal.y
          normals put normal.z
        } else {
          normals put -normal.x
          normals put -normal.y
          normals put -normal.z
        }
        
        // texture coordinate
        textureMode match {
          case Sphere.TextureMode.LINEAR =>
            coords put radialFraction
            coords put 0.5f * (zFraction + 1f)
            
          case Sphere.TextureMode.PROJECTED =>
            coords put radialFraction
            coords put INV_PI * (HALF_PI + asin(zFraction))
            
          case Sphere.TextureMode.POLAR =>
            val r = HALF_PI - abs(aFraction) / PI
            val u = r * lutCos(iR) + 0.5f
            val v = r * lutSin(iR) + 0.5f
            coords put u put v
        }
        
        i += 1
      }
      Buffer.copyVec3(vertices, iSave, i)
      Buffer.copyVec3(normals, iSave, i)
      
      textureMode match {
        case Sphere.TextureMode.LINEAR =>
          coords put 1f 
          coords put 0.5f * (zFraction + 1f)
        
        case Sphere.TextureMode.PROJECTED =>
          coords put 1f
          coords put INV_PI * (HALF_PI + asin(zFraction))
        
        case Sphere.TextureMode.POLAR =>
          val r = HALF_PI - abs(aFraction) / PI
          coords put (r + 0.5f) put 0.5f
      }
      i += 1
    }
    
    // south pole
    vertices.position(i * 3)
    vertices put center.x
    vertices put center.y
    vertices put center.z - radius
    
    normals.position(i * 3)
    if(!viewInside) {
      normals put 0 put 0 put -1
    } else {
      normals put 0 put 0 put 1
    }
  
    coords.position(i * 2)
    textureMode match {
      case Sphere.TextureMode.POLAR =>
        coords put 0.5f put 0.5f
      case _ =>
        coords put 0.5f put 0.0f
    }
    i += 1
    
    // north pole
    vertices put center.x
    vertices put center.y
    vertices put center.z + radius
    
    if(!viewInside) {
      normals put 0 put 0 put 1
    } else {
      normals put 0 put 0 put -1
    }
    
    textureMode match {
      case Sphere.TextureMode.POLAR =>
        coords put 0.5f put 0.5f
      case _ =>
        coords put 0.5f put 1.0f
    }
    
    // clean up
    vertices.rewind
    normals.rewind
    coords.rewind
  }
  
  /**
   * setup connections between vertices
   */
  protected def initIndices {
    import util.Buffer
    
    val tris = 2 * (zSamples - 2) * radialSamples
    indexCount = 3 * tris
    indices = Buffer.int(indexCount)
    
    var iZStart = 0
    for(iZ <- 0 until zSamples -3) {
      var i0 = iZStart
      var i1 = i0 + 1
      
      iZStart += (radialSamples + 1)
      var i2 = iZStart
      var i3 = i2 + 1
      
      for(i <- 0 until radialSamples) {
        // outside view
        indices put i0; i0 += 1
        indices put i1
        indices put i2
        
        indices put i1; i1 += 1
        indices put i3; i3 += 1
        indices put i2; i2 += 1
      }
    }
    
    // south pole triangles
    for(i <- 0 until radialSamples) {
      // outside view
      indices put i
      indices put vertexCount - 2
      indices put i + 1
    }
    
    // north pole triangles
    val iOffset = (zSamples - 3) * (radialSamples + 1)
    for(i <- 0 until radialSamples) {
      // outside view
      indices put i + iOffset
      indices put i + 1 + iOffset
      indices put vertexCount - 1
    }
    
    indices.rewind
  }
}
