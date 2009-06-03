/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created June 03, 2009 */
package field.kit.test

/**
 * Base class for all test sketches 
 */
abstract class Sketch extends field.kit.Sketch {
  val DEFAULT_WIDTH = 1280
  val DEFAULT_HEIGHT = 720
  
  def init(initializer: => Unit):Unit = 
    init(DEFAULT_WIDTH, DEFAULT_HEIGHT, initializer)
}
