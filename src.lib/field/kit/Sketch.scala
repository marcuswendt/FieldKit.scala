/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 12, 2009 */
package field.kit

import field.kit.p5.BasicSketch
import processing.core.PApplet

/**
 * contains global constants 
 */
object Sketch extends PApplet {
}

/**
 * extends the BasicSketch with useful features
 */
abstract class Sketch extends BasicSketch {
  
  // -- Metadata ---------------------------------------------------------------
  protected var meta = new MetaData
  protected class MetaData {
    import scala.xml.Elem
    var name:String = logName
    var description:Elem = _
    var author:String = _
    var url:String = _
  }

  import javax.swing.JFrame
  protected var aboutMenu:JFrame = null
  protected def showAbout {
    if(aboutMenu == null) {
      import field.kit.util.SwingUtil
      import javax.swing.WindowConstants
      import javax.swing.JEditorPane
      import javax.swing.event.HyperlinkListener
      import javax.swing.event.HyperlinkEvent
      import javax.swing.JScrollPane
      import java.awt.BorderLayout
      
      // create about html document
      val about = <html>
      <head>
      	<title>test</title>
      </head>
      <body style="margin: 10px">
      	<h1>{meta.name}</h1>
        <em>by <a href={meta.url}>{meta.author}</a></em>
      	{meta.description}
      </body>
      </html>
      
      // create frame to display the html
      val f = new JFrame
      f.setLayout(new BorderLayout)
      f.setAlwaysOnTop(true)
      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      f.setSize(300, 450)
      
      val ep = new JEditorPane("text/html", about.toString)
      ep.setEditable(false)  
      ep.setOpaque(false)  
      ep.addHyperlinkListener(new HyperlinkListener() {
        override def hyperlinkUpdate(e:HyperlinkEvent) {  
          if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType)) {
            SwingUtil.openURL(e.getDescription)
            aboutMenu.setVisible(false)
          }  
        }  
      })
      val sp = new JScrollPane
      sp.setViewportView(ep)
      f.add(sp, BorderLayout.CENTER)
      
      f.pack
      SwingUtil.center(f)
      aboutMenu = f
    }
    aboutMenu.setVisible(true)
  }

  // -- System Tweaks ----------------------------------------------------------
  def osMac = System.getProperty("os.name").toLowerCase.startsWith("mac os x")
  def osWindows = System.getProperty("os.name").startsWith("Windows")
  
  /** 
  	* Sets a few system dependent tweaks. 
  	* @see <a href="http://developer.apple.com/documentation/Java/Reference/Java_PropertiesRef/Articles/JavaSystemProperties.html#//apple_ref/doc/uid/TP40001975">JavaSystemProperties</a>
  	*/
  protected def systemTweaks {
    // osx
    if(osMac) {
      System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", meta.name)
      
      import com.apple.eawt._
      var app = new Application
      
      app.addApplicationListener(new ApplicationAdapter() {
    	  override def handleAbout(e:ApplicationEvent) {
    	    fine("system: about")
    	    showAbout
    	    e.setHandled(true)
    	  }
       
    	  override def handleOpenFile(e:ApplicationEvent) {
    	    fine("system: open file")
    	    e.setHandled(true)
    	  }
       
    	  override def handlePreferences(e:ApplicationEvent) {
    	    fine("system: preferences")
    	    e.setHandled(true)
    	  }
       
    	  override def handleQuit(e:ApplicationEvent) {
    	    fine("system: quit")
    	    e.setHandled(true)
    	    exit
    	  }
      })
    
    // windows
    } else if(osWindows) {
      import javax.swing.UIManager
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
    }
  }
  
  // apply tweaks
  systemTweaks
  
//  override def init(w:Int, h:Int, initializer: => Unit) {
//    super.init(w,h,initializer)
//  }
}
