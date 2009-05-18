/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl.render.state

import field.kit.gl.render.RenderState
import field.kit.gl.render.objects.Texture

/** Companion object to <code>TextureState</code> */
object TextureState {
  import javax.media.opengl.GL
  import java.net.URL

  /** Creates a new <code>TextureState</code> with a single Texture loaded from the give path */
  def apply(file:String) = load(file)

  /** Creates a new <code>TextureState</code> with a single Texture loaded from the give URL */
  def apply(file:URL) = load(file)
  
  /** Creates a new <code>TextureState</code> with a single Texture and a blank Image with the given properties */
  def apply(width:Int, height:Int, alpha:Boolean) = create(width, height, alpha)
  
  // -- Loading & Creating -----------------------------------------------------
  /** Creates a new <code>TextureState</code> with a single Texture loaded from the give path */
  def load(file:String) = new TextureState(Texture(file))
  
  /** Creates a new <code>TextureState</code> with a single Texture loaded from the give URL */
  def load(file:URL) = new TextureState(Texture(file))
  
  /** Creates a new <code>TextureState</code> with a single Texture and a blank Image with the given properties */
  def create(width:Int, height:Int, alpha:Boolean) =
    new TextureState(Texture(width, height, alpha))
}

/** Applies a texture to an object */
class TextureState extends RenderState {
  import javax.media.opengl.GL
  import field.kit.util.datatype.collection.ArrayBuffer
  import field.kit.gl.scene.Geometry

  protected var textures = new ArrayBuffer[Texture]
  
  def this(texture:Texture) = {
    this()
    this() = texture
  }
  
  /** Enables all registered Textures */
  def enable(geo:Geometry) {
    textures foreach (t => {
      activateUnit(t)
      t.bind
    })
  }
  
  /** Disables all registered Textures */
  def disable(geo:Geometry) {
    textures foreach (t => {
      activateUnit(t)
      t.unbind
    })
    
    gl.glActiveTexture(GL.GL_TEXTURE0)
  }
  
  /** Destroys all registered Textures */
  def destroy {
    textures foreach (t => {
      activateUnit(t)
      t.destroy
    })
  }
  
  /** Activates the texture unit for the given texture */
  protected def activateUnit(texture:Texture) {
    val unit = textures.indexOf(texture)
    gl.glActiveTexture(GL.GL_TEXTURE0 + unit)
  }
  
  /** Sets the given texture for the first texture unit */
  def update(texture:Texture):Unit = update(0, texture)
  
  /** Sets the given texture for the given texture unit */
  def update(unit:Int, texture:Texture) {
    if(unit >= 0) {
      while (unit >= textures.size) 
        textures += null
      
      textures(unit) = texture
    }
  }
  
  // -- Helpers ----------------------------------------------------------------
  /** @return The Texture Unit as int for the given texture */
  def unit(texture:Texture) = textures indexOf texture
}
