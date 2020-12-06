package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.parser.{Parser, Renderer}

import java.util.Base64

abstract class BitBarApp {
  val menu: MenuItem
  val pluginName: String

  def handler(action: String, metadata: Option[String]): Unit = ()

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "dispatch" :: action :: Nil => handler(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        handler(decode(action), Some(decode(metadata)))
      case _ =>
        new Parser(new Renderer(new SelfPath(pluginName)))
          .parse(menu)
          .lines
          .foreach(println)
    }

  }
}
