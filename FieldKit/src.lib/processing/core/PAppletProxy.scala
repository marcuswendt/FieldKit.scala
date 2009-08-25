/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created August 25, 2009 */
package processing.core

/**
 * adds a few methods to access fields in PApplet that otherwise
 * would be invisible due to package restrictions
 */
class PAppletProxy extends PApplet {
  def getThread = this.thread
  
  def setThread(t:Thread) = this.thread = t
  
  override def exit2() = super.exit2
}
