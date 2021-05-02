package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.*

object AlternatePlugin extends MenuDsl {
  menu("my-plugin") {
    link("google", "https://google.com")
    link("yahoo", "https://yahoo.com", alternate = true)
  }
}
