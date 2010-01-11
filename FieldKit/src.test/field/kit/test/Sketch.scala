/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created June 03, 2009 */
package field.kit.test

/**
 * Base class for all test sketches 
 */
abstract class Sketch extends field.kit.Sketch {
  // standard size for all test sketches
  val DEFAULT_WIDTH = 1280
  val DEFAULT_HEIGHT = 720
  val DEFAULT_FULLSCREEN = false
  val DEFAULT_AA = 0
}
