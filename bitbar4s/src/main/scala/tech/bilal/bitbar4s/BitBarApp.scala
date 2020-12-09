package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models._
import tech.bilal.bitbar4s.models.MenuItem._
import tech.bilal.bitbar4s.parser.{Parser, Renderer}
import tech.bilal.bitbar4s.dsl._

import java.util.Base64

abstract class BitBarApp {
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
  
  def refine(a: Handler | HandlerBuilder): Handler = a match {
    case x:HandlerBuilder => handlerBuilderToHandler(x)
    case x: Handler => x
  }

  type Handler = PartialFunction[(String, Option[String]), Unit]

  def handlerBuilderToHandler(handlerBuilder: HandlerBuilder):Handler = 
    handlerBuilder.handlers.foldLeft(PartialFunction.empty[(String, Option[String]), Unit])((acc, cur) => {
      val p:Handler = cur._2 match {
        case SimpleHandlerFunction(a, f) => {
            case (a: String, _) if a == cur._1 => f()
          }
        case MetadataHandlerFunction(a, f) => {
          case (a: String, m: Option[String]) if a == cur._1 => f(m)
        }
      }
      acc orElse p
      }
    ).orElse(PartialFunction.fromFunction(_ => ()))

  val handler: Handler | HandlerBuilder = PartialFunction.fromFunction(_ => ())

  private def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def main(args: Array[String]): Unit = {

    val defaultCase: Handler = {
      case (str: String, o: Option[String]) => ()
    }

    args.toList match {
      case "dispatch" :: action :: Nil =>
        refine(handler).orElse(defaultCase)(decode(action), None)
      case "dispatch" :: action :: metadata :: Nil =>
        refine(handler).orElse(defaultCase)(decode(action), Some(decode(metadata)))
      case _ =>
        new Parser(new Renderer(new SelfPath(pluginName)))
          .parse(refine(appMenu))
          .lines
          .foreach(println)
    }
  }
}
