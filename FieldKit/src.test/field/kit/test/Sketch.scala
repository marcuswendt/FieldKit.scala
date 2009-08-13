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
  // general meta information for all test sketches
  meta.author = "FIELD"
  meta.url = "http://www.field.io"
  meta.description = <div>
  	<p>This test is part of the <a href="http://code.google.com/p/fieldkit/">FieldKit</a>
     open-source project.</p>
  </div>
   
  // standard size for all test sketches
  val DEFAULT_WIDTH = 1280
  val DEFAULT_HEIGHT = 720
}
