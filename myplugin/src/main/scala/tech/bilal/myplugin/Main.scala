package tech.bilal.myplugin

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.dsl.BitBarDsl2
import tech.bilal.bitbar4s.models.MenuItem

object Main extends BitBarApp with BitBarDsl2 {

  override val pluginName: String = "myplugin"

  override val handler: Handler = {
    case ("send-email", Some(email)) => 
      sendEmail(email)
    
    case ("push", Some(id)) => 
      httpPost(id)
    
    case ("trigger-something", Some(something)) => 
      interactWithMobileDevice(something)
  }

  override val appMenu = menu("my-plugin", color = "red", textSize = 20) {
    action("send email", "send-email", Some("abc@xyz.com"), true)
    text("item 1", font = "Times")
    ---
    text("item 2", textSize = 15)
    subMenu("submenu"){
      text("item 3")
      text("item 4")
      Range(20,30).foreach{ i =>
        link(s"item_$i", "http://google.com")
      }
      subMenu("nested", color = "orange"){
        text("item 5")
        ---
        text("item 6")
        shellCommand("item 7", "echo", showTerminal = true ,params = "hello world", "sds")
      }
    }
  }

  def sendEmail(email:String) = ???
  def httpPost(payload:String) = ???
  def interactWithMobileDevice(id:String) = ???
}
