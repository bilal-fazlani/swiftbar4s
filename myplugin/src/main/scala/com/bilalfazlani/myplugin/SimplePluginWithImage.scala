package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.*

object SimplePluginWithImage extends Plugin with MenuDsl {
  override val menu = menu("my-plugin", templateImage = Image.Resource("success.png")) {
    text("success", image =  Image.Base64("img1"))

    runtime.foreach { r =>
      if (r.osVersion < OSVersion.BigSur)
        text(s"your os version is: ${r.osVersion}")
      text(s"Please upgrade to BigSur", image = Image.None)
    }

    if (runtime.isEmpty)
      text("no runtime")
  }
}
