package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.SwiftBarApp
import com.bilalfazlani.swiftbar4s.dsl._

// object Main extends App {
//   val bytes = ???
//   val base64 = java.util.Base64.getEncoder.encodeToString(bytes)
//   println(base64)
// }

object SimplePlugin extends SwiftBarApp with MenuDsl {
  override val appMenu = menu("my-plugin") {
    runtime.foreach { r =>
      if (r.osVersion < OSVersion.BigSur)
        text(s"your os version is: ${r.osVersion}")
      text(s"Please upgrade to BigSur")
    }

    if (runtime.isEmpty)
      text("no runtime")
  }
}
