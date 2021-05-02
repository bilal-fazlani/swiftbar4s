package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.devtools.PluginPreview
import com.bilalfazlani.swiftbar4s.dsl.*

object DevSamplePlugin
    extends PluginPreview("/Users/bilal/projects/bitbar-plugins/sample.5m.sh")
    with MenuDsl {
  override val menu = menu("sample!!") {
    link("1easdsd", "https://google.com")
    link("yahoo", "https://yahoo.com", alternate = true)
  }
}
