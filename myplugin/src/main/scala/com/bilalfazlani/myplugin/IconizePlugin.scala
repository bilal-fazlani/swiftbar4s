package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*

object IconizePlugin extends PluginDsl {
  menu("icons!!") {
    text("line1 :mushroom: :gamecontroller.fill:", iconize = Iconize.Default)
    text("line2 :mushroom: :gamecontroller.fill:", iconize = Iconize.Disabled)
    text("line3 :mushroom: :gamecontroller.fill:", iconize = Iconize.EmojiOnly)
    text("line4 :mushroom: :gamecontroller.fill:", iconize = Iconize.SFSymbolOnly)
  }
}
