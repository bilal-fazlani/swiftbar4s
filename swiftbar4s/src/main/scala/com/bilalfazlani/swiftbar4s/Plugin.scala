package com.bilalfazlani.swiftbar4s

import com.bilalfazlani.swiftbar4s.models.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import com.bilalfazlani.swiftbar4s.parser.{
  Parser,
  Renderer,
  MenuRenderer,
  Printer
}
import com.bilalfazlani.swiftbar4s.dsl.*
import java.util.Base64
import org.reactivestreams.Publisher

type Handler = PartialFunction[(String, Option[String]), Unit]

abstract class Plugin extends Environment with Notifications {
  def appMenu: Menu | Publisher[Menu]
  val handler: HandlerBuilder = HandlerBuilder()
  val parser = new Parser(
    new Renderer(sys.env.getOrElse("SWIFTBAR_PLUGIN_PATH", "."))
  )
  val menuRenderer   = MenuRenderer(parser, Printer())
  val menuSubscriber = MenuSubscriber(menuRenderer)

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "dispatch" :: action :: Nil =>
        handler.build()(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        handler.build()(decode(action), Some(decode(metadata)))
      case _ =>
        appMenu match {
          case mb: Menu   => menuRenderer.renderMenu(mb, false)
          case mbp: Publisher[?] => mbp.subscribe(menuSubscriber.asInstanceOf)
        }
    }
  }
}