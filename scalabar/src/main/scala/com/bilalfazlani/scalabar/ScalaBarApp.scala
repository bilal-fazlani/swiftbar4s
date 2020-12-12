package com.bilalfazlani.scalabar

import com.bilalfazlani.scalabar.models._
import com.bilalfazlani.scalabar.models.MenuItem._
import com.bilalfazlani.scalabar.parser.{Parser, Renderer}
import com.bilalfazlani.scalabar.dsl._

import java.util.Base64

type Handler = PartialFunction[(String, Option[String]), Unit]

abstract class ScalaBarApp {
  val appMenu: MenuItem | MenuBuilder
  val pluginName: String

  type SimpleType = Text | Link | DispatchAction | ShellCommand

  def menuBuilderToMenu(menuBuilder:MenuBuilder): Menu = Menu(menuBuilder.textItem, menuBuilder.items.map{
    case x:MenuBuilder => menuBuilderToMenu(x)
    case a:SimpleType  => a
  }.toSeq)

  def refine(a: MenuItem | MenuBuilder): MenuItem = a match {
    case x:MenuItem => x
    case x:MenuBuilder => menuBuilderToMenu(x)
  }

  val handler: Handler = defaultCase

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  val defaultCase: Handler = {
      case (str: String, o: Option[String]) => ()
    }

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "dispatch" :: action :: Nil =>
        handler.orElse(defaultCase)(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        handler.orElse(defaultCase)(decode(action), Some(decode(metadata)))
      case _ =>
        new Parser(new Renderer(new SelfPath(pluginName)))
          .parse(refine(appMenu))
          .lines
          .foreach(println)
    }
  }
}
