package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.*

object SimplePluginWithImage extends Plugin with MenuDsl {
  override val appMenu = menu("my-plugin", templateImage = Image.ResourceImage("success.png")) {
    text("success", image =  Image.Base64Image("img1"))

    runtime.foreach { r =>
      if (r.osVersion < OSVersion.BigSur)
        text(s"your os version is: ${r.osVersion}")
      text(s"Please upgrade to BigSur", image = Image.None)
    }

    if (runtime.isEmpty)
      text("no runtime")
  }
}
