package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl._

object AlternatePlugin extends Plugin with MenuDsl {

  override val appMenu = menu("my-plugin") {
    link("google", "https://google.com")
    link("yahoo", "https://yahoo.com", alternate = true)
  }
}
