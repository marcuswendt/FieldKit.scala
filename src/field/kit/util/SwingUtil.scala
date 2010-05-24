/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created April 22, 2009 */
package field.kit.util

/** 
 * utility methods for swing interfaces
 * @author Marcus Wendt
 */
object SwingUtil extends Logger {
  import java.awt.DisplayMode
  import java.awt.GraphicsDevice
  import java.awt.GraphicsEnvironment

  import javax.swing.JFrame
  
  /** Centers the given frame on the default screen. */
  def center(frame:JFrame) {
    val env = GraphicsEnvironment.getLocalGraphicsEnvironment
    val dev = env.getDefaultScreenDevice
    val mode = dev.getDisplayMode
    frame.setLocation(mode.getWidth / 2 - frame.getWidth / 2, mode.getHeight / 2 - frame.getHeight / 2)
  }
  
  /** Opens the given url with the systems default browser. */
  def openURL(url:String ) {
    import field.kit.util.OSUtil
    val errMsg = "Error attempting to launch web browser"
    
    try {
      if(OSUtil.isMac) {
        val fileMgr = Class.forName("com.apple.eio.FileManager")
        val openURL = fileMgr.getDeclaredMethod("openURL", Array(classOf[String]):_*)
        openURL.invoke(null, Array(url):_*)
        
      } else if(OSUtil.isWindows) {
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
  
  // ---------------------------------------------------------------------------
  // menu bars & items
  import java.awt.MenuItem
  def menuItem(name:String, action: => Unit):MenuItem = menuItem(name, -1, false, action)
  
  /** Creates a <code>java.awt.MenuItem</code> */
  def menuItem(name:String, shortcut:Int, useShift:Boolean, action: => Unit) = {
    import java.awt.event.ActionListener
    import java.awt.event.ActionEvent
    
    /*
     * TODO add shortcuts 
     * int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
     * saveMenuItem.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, mask + KeyEvent.SHIFT_MASK)); 
     */
    
    val i = new MenuItem(name)
    
    if(shortcut != -1) {
      import java.awt.Toolkit
      import java.awt.MenuShortcut
      import javax.swing.KeyStroke
      import java.awt.event.KeyEvent
      import java.awt.event.InputEvent

//      val mask = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask
//      val key = KeyStroke.getKeyStroke(KeyEvent.VK_S, mask + InputEvent.SHIFT_MASK)
//      
      i.setShortcut(new MenuShortcut(shortcut, useShift));
    }
    
    i.addActionListener(new ActionListener {
      def actionPerformed(e:ActionEvent) = action
    })
    i
  }
  
  // ---------------------------------------------------------------------------
  // file chooser
  import java.awt.FileDialog
  import java.awt.Frame
  import java.io.File
  import java.io.FilenameFilter
  
  def chooseFile(mode:Int, parent:Frame, suffix:String, title:String) = {
    val fd = new java.awt.FileDialog(parent, title, mode)
    fd.setFilenameFilter(new FilenameFilter {
      def accept(dir:File, name:String) =
        name.toLowerCase.endsWith(suffix.toLowerCase)
    })
    fd.setVisible(true)
    
    if(fd.getFile != null) {
      var path = fd.getDirectory + fd.getFile
      
      // append suffix when necessary
      if(mode == FileDialog.SAVE) {
        if(!path.toLowerCase.endsWith("." + suffix)) path += "." + suffix
      }
      
      path
    } else {
      null
    }
  }
  
  def loadFile(parent:Frame, suffix:String, title:String) =
    chooseFile(FileDialog.LOAD, parent, suffix, title)
    
  def saveFile(parent:Frame, suffix:String, title:String) =
    chooseFile(FileDialog.SAVE, parent, suffix, title)
}
    