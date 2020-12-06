package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.parser.{Parser, Renderer}

import java.util.Base64

abstract class BitBarApp {
  val menu: MenuItem
  val pluginName: String

  type Handler = PartialFunction[(String, Option[String]), Unit]

  val handler: Handler = PartialFunction.empty

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {

    val defaultCase: Handler = {
      case (str: String, o: Option[String]) => ()
    }

    args.toList match {
      case "dispatch" :: action :: Nil =>
        handler.orElse(defaultCase)(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        handler.orElse(defaultCase)(decode(action), Some(decode(metadata)))
      case _ =>
        new Parser(new Renderer(new SelfPath(pluginName)))
          .parse(menu)
          .lines
          .foreach(println)
    }
  }
}
