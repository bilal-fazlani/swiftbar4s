package com.bilalfazlani.swiftbar4s.dsl

import scala.sys.process.*

trait NotificationDsl { this: Environment =>
  def notify(
      title: String,
      subtitle: Option[String] = None,
      body: Option[String] = None,
      href: Option[String] = None,
      silent: Boolean = false
  ): Unit = {
    runtime.foreach { r =>
      val baseUrl = s"swiftbar://notify?plugin=${r.pluginFileName}&title=$title"
      val sub     = subtitle.map(x => s"&subtitle=$x").getOrElse("")
      val b       = body.map(x => s"&body=$x").getOrElse("")
      val link    = href.map(x => s"&href=$x").getOrElse("")
      val s       = if silent then "&silent=true" else ""
      val url     = baseUrl + sub + b + link + s
      s"""open "$url"""".!!
    }
  }
}
