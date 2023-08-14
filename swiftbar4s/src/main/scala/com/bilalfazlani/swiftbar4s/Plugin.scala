package com.bilalfazlani.swiftbar4s

import com.bilalfazlani.swiftbar4s.models.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import com.bilalfazlani.swiftbar4s.parser.{
  Parser,
  Renderer,
  StreamingMenuRenderer,
  Printer
}
import com.bilalfazlani.swiftbar4s.dsl.*
import java.util.Base64
import org.reactivestreams.Publisher
import scala.concurrent.Promise
import scala.concurrent.Await
import scala.concurrent.duration.Duration

type Handler = PartialFunction[(String, Option[String]), Unit]

abstract class Plugin {
  def appMenu: MenuItem | Publisher[MenuItem]
  def appHandler: Handler = ???
  val parser = new Parser(
    new Renderer(sys.env.getOrElse("SWIFTBAR_PLUGIN_PATH", "."))
  )
  val menuRenderer        = StreamingMenuRenderer(parser, Printer())
  lazy val promise        = Promise[Unit]()
  lazy val menuSubscriber = MenuSubscriber(menuRenderer, promise)

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "dispatch" :: action :: Nil =>
        appHandler(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        appHandler(decode(action), Some(decode(metadata)))
      case _ =>
        appMenu match {
          case mb: MenuItem =>
            menuRenderer.renderMenu(mb, false)
          case mbp: Publisher[MenuItem] =>
            mbp.subscribe(menuSubscriber)
            Await.result(promise.future, Duration.Inf)
        }
    }
  }
}
