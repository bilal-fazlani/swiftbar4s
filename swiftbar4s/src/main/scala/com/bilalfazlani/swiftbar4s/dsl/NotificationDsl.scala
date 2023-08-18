package com.bilalfazlani.swiftbar4s.dsl

import scala.sys.process.*

enum NotificationAction:
  case RefreshPlugin
  case OpenUrl(url: String)
  // case PluginAction(action: String, metadata: Option[String])
  case None

trait NotificationDsl { this: Environment =>
  def notify(
      title: String,
      subtitle: Option[String] = None,
      body: Option[String] = None,
      action: NotificationAction = NotificationAction.None,
      silent: Boolean = false
  ): Unit = {
    runtime.foreach { r =>
      val baseUrl = s"swiftbar://notify?plugin=${r.pluginFileName}&title=$title"
      val sub     = subtitle.map(x => s"&subtitle=$x").getOrElse("")
      val b       = body.map(x => s"&body=$x").getOrElse("")
      val link = action match
        case NotificationAction.RefreshPlugin =>
          s"&href=swiftbar://refreshplugin?name=${r.pluginFileName}"
        case NotificationAction.OpenUrl(url) => s"&href=$url"
        // case NotificationAction.PluginAction(action, metadata) =>
        //   s"&href=swiftbar://dispatch?name=${r.pluginFileName}&action=$action${metadata
        //     .map(x => s"&metadata=$x")
        //     .getOrElse("")}"
        case NotificationAction.None => ""
      val s   = if silent then "&silent=true" else ""
      val url = baseUrl + sub + b + link + s
      s"""open "$url"""".!!
    }
  }
}
