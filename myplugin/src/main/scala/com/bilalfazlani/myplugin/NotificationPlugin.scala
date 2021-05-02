package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.*

object NotificationPlugin extends Plugin with MenuDsl with HandlerDsl {

  handler {
    handle("notify") { name =>
      println(s"clicked on $name")
      notify("clicked!", name)
    }
  }

  override val menu = menu("my-plugin") {
    link("open google", "https://google.com")
    action("notify", "notify", Some("abc"), alternate = true, refresh = true)
  }
}
