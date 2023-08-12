package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*

object RuntimePlugin extends PluginDsl {
  menu("my-plugin", shortcut = "ABC") {
    runtime match {
      case None =>
        text("Not running via swiftbar")
      case Some(r) =>
        import r.*

        osVersion > OSVersion.BigSur
        osVersion isNot OSVersion.Monterey
        osVersion is OSVersion.Ventura

        text(s"OS version: ${r.osVersion}")
        text(s"OS theme: ${r.osAppearance}")
        text(s"Plugin file: ${r.pluginFileName}")
    }
  }
}
