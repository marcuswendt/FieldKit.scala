/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 12, 2009 */
package field.kit

import processing.core.PApplet

import field.kit.p5.BasicSketch

/**
 * extends the BasicSketch with useful features
 * @author Marcus Wendt
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
    var date:String = "March 12, 2009"
  }

  import javax.swing.JFrame
  protected var aboutMenu:JFrame = null
  protected def showAbout {
    info("showAbout")
    if(aboutMenu == null) {
      import field.kit.util.SwingUtil
      import javax.swing.WindowConstants
      import javax.swing.JEditorPane
      import javax.swing.event.HyperlinkListener
      import javax.swing.event.HyperlinkEvent
      import javax.swing.JScrollPane
      import java.awt.BorderLayout
      
      // create frame to display the html
      val f = new JFrame
      f.setLayout(new BorderLayout)
      f.setAlwaysOnTop(true)
      f.setResizable(false)
      f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
      f.setSize(300, 450)
      
      val ep = new JEditorPane("text/html", htmlAbout.toString)
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
  
  /** @return returns the html document that is displayed within the sketches about window */
  def htmlAbout = <html>
  	<body style="margin: 10px">
     <h1>{meta.name}</h1>
     <em>by <a href={meta.url}>{meta.author}</a>, {meta.date}</em>
     {meta.description}
   </body>
   </html>

  // -- System Tweaks ----------------------------------------------------------
  /** 
  	* Sets a few system dependent tweaks. 
  	* @see <a href="http://developer.apple.com/documentation/Java/Reference/Java_PropertiesRef/Articles/JavaSystemProperties.html#//apple_ref/doc/uid/TP40001975">JavaSystemProperties</a>
  	*/
  protected def applySystemTweaks {
    import field.kit.util.OSUtil
    // osx
    if(OSUtil.isMac) {
      System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", meta.name)
      /*
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
    	    stop
    	    exit
    	  }
      })
      */
    // windows
    } else if(OSUtil.isWindows) {
      import javax.swing.UIManager
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
    }
  }
  
  // apply tweaks
  applySystemTweaks
  
  // -- Menubar ----------------------------------------------------------------
  import java.awt.MenuBar
  import java.awt.Menu
  protected var menuBar:MenuBar = null
  
  /** Called from init to create a system menubar */
  protected def initMenuBar = {
    menuBar = new MenuBar
    
    // file menu
    val file = new Menu("File")
    initFileMenu(file)
    menuBar add file
    
    // help menu
    val help = new Menu("Help")    
    initHelpMenu(help)
    menuBar add help
    
    menuBar
  }
  
  protected def initFileMenu(file:Menu) {
    import field.kit.util.SwingUtil
    import java.awt.event.KeyEvent
    
    file add SwingUtil.menuItem("Present", KeyEvent.VK_0, false, this.toggleFullscreen)
    file add SwingUtil.menuItem("Record Screenshot", KeyEvent.VK_R, false, rec.screenshot)
    file add SwingUtil.menuItem("Record Sequence", KeyEvent.VK_R, true, rec.sequence)
    file add SwingUtil.menuItem("Recording Settings", {
      import javax.swing.JOptionPane
      
      val options = new Array[Object](30)
      for(i <- 0 until options.length)
        options(i) = (i+1)*width +" x "+ (i+1)*height +" px"
      
      val input = JOptionPane.showInputDialog(frame, 
                                              "Select the output dimension:",
                                              "Recording Settings",
                                              JOptionPane.PLAIN_MESSAGE,
                                              null,
                                              options,
                                              options(0)).toString
      
      val outputWidth = input.substring(0, input.indexOf(' ')).toInt
      val numTiles = outputWidth / width
      rec.init(numTiles * width, numTiles * height)
    })
    file add SwingUtil.menuItem("Quit", KeyEvent.VK_Q, false, exit)
  }
  
  protected def initHelpMenu(help:Menu) {
    import field.kit.util.SwingUtil
    help add SwingUtil.menuItem("About", showAbout)
  }
  
  override def init(width:Int, height:Int, fullscreen:Boolean, initializer: => Unit) {
    super.init(width,height,fullscreen,initializer)
    
    // set default recording dimensions
    rec.init(width, height)
    
    // dont show menubar in fullscreen mode, (deactivates keyboard shortcuts on linux)
    if(!fullscreen)
      frame.setMenuBar(initMenuBar)
  }
    
  // -- Screen Recorder---------------------------------------------------------
  import field.kit.p5.Recorder
  var rec = new Recorder(this)
  rec.name = meta.name
  
  /** enables/disabled the vertical sync, prevents tearing */
  var vsyncEnabled = true
  
  override def draw {
    if(vsyncEnabled) gl.setSwapInterval(1)
    
    rec.pre
    render
    rec.post
  }
}
