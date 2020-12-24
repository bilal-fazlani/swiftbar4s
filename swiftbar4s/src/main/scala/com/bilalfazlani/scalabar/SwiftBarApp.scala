package com.bilalfazlani.swiftbar4s

import com.bilalfazlani.swiftbar4s.models._
import com.bilalfazlani.swiftbar4s.models.MenuItem._
import com.bilalfazlani.swiftbar4s.parser.{Parser, Renderer}
import com.bilalfazlani.swiftbar4s.dsl._

import java.util.Base64

type Handler = PartialFunction[(String, Option[String]), Unit]

abstract class SwiftBarApp {
  val pluginName: String

  val appMenu: MenuBuilder
  val handler: HandlerBuilder

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "dispatch" :: action :: Nil =>
        handler.build()(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        handler.build()(decode(action), Some(decode(metadata)))
      case _ =>
        new Parser(new Renderer(new SelfPath(pluginName)))
          .parse(appMenu.build)
          .lines
          .foreach(println)
    }
  }
}
