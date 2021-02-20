package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl._

object SimplePluginWithImage extends Plugin with MenuDsl {

  def getImage = {
    val bytes  = getClass.getResourceAsStream("/success.png").readAllBytes
    val base64 = java.util.Base64.getEncoder.encodeToString(bytes)
    base64
  }

  override val appMenu = menu("my-plugin") {
    text("success", image = getImage)

    runtime.foreach { r =>
      if (r.osVersion < OSVersion.BigSur)
        text(s"your os version is: ${r.osVersion}")
      text(s"Please upgrade to BigSur")
    }

    if (runtime.isEmpty)
      text("no runtime")
  }
}
