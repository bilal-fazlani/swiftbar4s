package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*
import com.bilalfazlani.rainbowcli.*

object AnsiPlugin extends PluginDsl {

  given ColorContext = ColorContext(true)

  menu("colorful", ansi = true) {
    text(s"I am ${"red".red}", ansi = true)
    text("I have default colors", ansi = true)
    text(s"I am ${"yellow".yellow} and ${"green".green}", ansi = true)
  }
}
