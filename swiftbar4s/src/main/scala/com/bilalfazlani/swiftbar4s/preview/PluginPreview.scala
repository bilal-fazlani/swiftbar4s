package com.bilalfazlani.swiftbar4s.preview

import com.bilalfazlani.swiftbar4s.Plugin

abstract class PluginPreview(pluginPath: String) extends Plugin {
  override val menuRenderer = DevMenuRenderer(parser, DevPrinter(pluginPath))
}
