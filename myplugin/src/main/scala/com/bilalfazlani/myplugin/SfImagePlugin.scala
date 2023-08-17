package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*

object SfImagePlugin extends PluginDsl {
  menu("Jobs", sfImage = SFImage.Monochrome("gear", "white")) {
    text(
      "Job 1 success",
      sfImage = SFImage.Palette("checkmark.circle.fill", "white", "green")
    )
    text(
      "Job 2 failed",
      sfImage = SFImage.Palette("xmark.circle.fill", "white", "red")
    )
  }
}

object SfImagePlugin2 extends PluginDsl {
  topLevel.sfSymbol(SFImage.Palette("checkmark.circle.fill", "white", "green"))
}
