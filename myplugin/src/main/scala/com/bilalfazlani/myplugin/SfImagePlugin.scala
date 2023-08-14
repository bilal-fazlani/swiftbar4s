package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*

object SfImagePlugin extends PluginDsl {
  menu("Jobs", sfImage = SfImageDsl.Monochrome("gear", "white")) {
    text(
      "Job 1 success",
      sfImage = SfImageDsl.Palette("checkmark.circle.fill", "white", "green")
    )
    text(
      "Job 2 failed",
      sfImage = SfImageDsl.Palette("xmark.circle.fill", "white", "red")
    )
  }
}
