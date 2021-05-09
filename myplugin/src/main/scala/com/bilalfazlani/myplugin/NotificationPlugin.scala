package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.PluginDsl

object NotificationPlugin extends PluginDsl {
  handler {
    handle("notify") { name =>
      notify("clicked!", name)
    }
  }

  menu("my-plugin") {
    link("open google", "https://google.com")
    action("notify", "notify", Some("abc"), alternate = true, refresh = true)
  }
}
