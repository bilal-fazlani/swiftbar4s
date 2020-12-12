package com.bilalfazlani.myplugin

import com.bilalfazlani.scalabar.ScalaBarApp
import com.bilalfazlani.scalabar.dsl.HandlerDsl
import com.bilalfazlani.scalabar.dsl.MenuDsl
import scala.language.implicitConversions


object Main extends ScalaBarApp with MenuDsl with HandlerDsl  {

  override val pluginName: String = "myplugin"

  override val handler = handler {
    handle("send-email") { emailMayBe =>
      emailMayBe.map(sendEmail)
    }

    handle("print-hello") {
      println("hello world")
    }
  }

  override val appMenu = menu("my-plugin", color = if(isDarkMode) "white" else "red", textSize = 20) {
    action("send email", "send-email", Some("abc@xyz.com"), true)
    action("print hello", "print-hello", showTerminal = true)
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

  def sendEmail(email:String) = println(s"email sent to $email")
}
