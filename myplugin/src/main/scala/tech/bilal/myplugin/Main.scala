package tech.bilal.myplugin

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.dsl.BitBarDsl2
import tech.bilal.bitbar4s.models.MenuItem

object Main extends BitBarApp with BitBarDsl2 {

  override val pluginName: String = "myplugin"

  override val handler: Handler = {
    case (action, metaData) =>
      println(action)
      println(metaData)
  }

  override val appMenu = menu("my-plugin", color = "red", textSize = 20) {
    actionDispatch("print something", "print", Some("hello world"), true)
    text("item 1", font = "Times")
    text("item 2", textSize = 15)
    subMenu("submenu"){
      text("item 3")
      text("item 4")
      subMenu("nested", color = "orange"){
        text("item 5")
        text("item 6")
        shellCommand("item 7", "echo")
      }
    }
  }
}
