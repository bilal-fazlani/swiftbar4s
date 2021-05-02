package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.*

object AlternatePlugin extends Plugin with MenuDsl {

  override val menu = menu("my-plugin") {
    link("google", "https://google.com")
    link("yahoo", "https://yahoo.com", alternate = true)
  }
}
