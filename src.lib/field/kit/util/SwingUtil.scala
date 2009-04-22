/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.util

import field.kit.Logger

/** utility methods for swing interfaces */
object SwingUtil extends Logger {
  import java.awt.DisplayMode
  import java.awt.GraphicsDevice
  import java.awt.GraphicsEnvironment

  import javax.swing.JFrame
  
  def center(frame:JFrame) {
    val env = GraphicsEnvironment.getLocalGraphicsEnvironment
    val dev = env.getDefaultScreenDevice
    val mode = dev.getDisplayMode
    frame.setLocation(mode.getWidth / 2 - frame.getWidth / 2, mode.getHeight / 2 - frame.getHeight / 2)
  }
  
  def openURL(url:String ) {
    val osName = System.getProperty("os.name")
    val errMsg = "Error attempting to launch web browser"
    
    try {
      if (osName.startsWith("Mac OS")) {
        val fileMgr = Class.forName("com.apple.eio.FileManager")
        val openURL = fileMgr.getDeclaredMethod("openURL", Array(classOf[String]):_*)
        openURL.invoke(null, Array(url):_*)
        
      } else if(osName.startsWith("Windows")) {
        Runtime.getRuntime.exec("rundll32 url.dll,FileProtocolHandler " + url)
      
      } else { //assume Unix or Linux
        val browsers = Array("firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape")
        var browser:String = null
        for(count <- 0 until browsers.length) {
          //for (int count = 0 count < browsers.length && browser == null count++)
          if (Runtime.getRuntime.exec(Array("which", browsers(count))).waitFor == 0)
            browser = browsers(count)
        }
        
        if (browser == null)
          throw new Exception("Could not find web browser")
        else
          Runtime.getRuntime.exec(Array(browser,url))
      }
  	} catch {
  	  case e:Exception => 
  	    import javax.swing.JOptionPane
        JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage)
        warn(errMsg, e.getMessage)
  	}
  }
  	
}
    