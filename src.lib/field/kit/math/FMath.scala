/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.math

/**
 * various helpers
 */
object FMath {
  
  // --------------------------------------------------------------------------
  // implements various interpolation and easing functions
  // @see http://en.wikipedia.org/wiki/Interpolation
  // --------------------------------------------------------------------------
  /** 
   * spherical linear interpolation
   * @see http://en.wikipedia.org/wiki/Slerp
   */
  def slerp(current:Float, target:Float, delta:Float) = 
    current * (1 - delta) + target * delta
}
