package com.bilalfazlani.swiftbar4s.dsl

import scala.sys.process.*

trait RefreshDsl { this: Environment =>
  def refresh: Unit = {
    runtime.foreach { r =>
      val baseUrl = s"swiftbar://refreshplugin?name=${r.pluginFileName}"
      s"""open "$baseUrl"""".!!
    }
  }
}
